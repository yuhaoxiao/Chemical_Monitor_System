package cn.nju.edu.chemical_monitor_system.utils.shiro.jwt;

import cn.nju.edu.chemical_monitor_system.constant.ConstantVariables;
import cn.nju.edu.chemical_monitor_system.response.BaseResponse;
import cn.nju.edu.chemical_monitor_system.utils.common.SpringContextUtil;
import cn.nju.edu.chemical_monitor_system.utils.redis.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class JWTFilter extends BasicHttpAuthenticationFilter {

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization");
        return authorization != null;
    }


    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");

        JWTToken token = new JWTToken(authorization);
        getSubject(request, response).login(token);
        return true;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
            } catch (Exception e) {
                String msg = e.getMessage();
                Throwable throwable = e.getCause();
                if (throwable instanceof SignatureVerificationException) {
                    msg = "Token或者密钥不正确(" + throwable.getMessage() + ")";
                } else if (throwable instanceof JWTDecodeException) {
                    msg = "Token解析出错";
                } else if (throwable instanceof TokenExpiredException) {
                    if (refreshToken(request, response)) {
                        return true;
                    } else {
                        msg = "Token已过期(" + throwable.getMessage() + ")";
                    }
                } else {
                    if (throwable != null) {
                        msg = throwable.getMessage();
                    }
                }
                this.response401(response, msg);
                return false;
            }
        } else {
            final boolean mustLoginFlag = ConstantVariables.MUST_LOGIN;
            if (mustLoginFlag) {
                this.response401(response, "请先登录");
                return false;
            }
        }
        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 将非法请求跳转到 /401
     */
    private void response401(ServletResponse resp, String msg) {
        HttpServletResponse httpServletResponse = WebUtils.toHttp(resp);
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        try (OutputStream outputStream = httpServletResponse.getOutputStream()) {
            String data = JSON.toJSONString(new BaseResponse(HttpStatus.UNAUTHORIZED.value(), "无权访问(Unauthorized):" + msg, null));
            outputStream.write(data.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean refreshToken(ServletRequest request, ServletResponse response) {
        RedisUtil redisUtil = (RedisUtil) SpringContextUtil.getBean("redisUtil");
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        String username = JWTUtil.getClaim(token, ConstantVariables.USERNAME);
        //这里用分布式锁避免并发请求都更新token
        while(true){
            boolean success=redisUtil.getLock(username+"-Lock");
            if(success){
                if (redisUtil.get(ConstantVariables.PREFIX_SHIRO_REFRESH_TOKEN + username) != null) {
                    String currentTimeMillisRedis = redisUtil.get(ConstantVariables.PREFIX_SHIRO_REFRESH_TOKEN + username).toString();
                    if (JWTUtil.getClaim(token, ConstantVariables.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
                        int refreshTokenExpireTime = ConstantVariables.REFRESH_TOKEN_EXPIRE_TIME;
                        int refreshTokenExpireTimeOld=ConstantVariables.REFRESH_TOKEN_EXPIRE_TIME_OLD;
                        redisUtil.set(ConstantVariables.PREFIX_SHIRO_REFRESH_TOKEN + username, currentTimeMillis, refreshTokenExpireTime);
                        redisUtil.set(ConstantVariables.PREFIX_SHIRO_REFRESH_TOKEN_OLD + username, currentTimeMillisRedis, refreshTokenExpireTimeOld);
                        token = JWTUtil.sign(username, currentTimeMillis);
                        JWTToken jwtToken = new JWTToken(token);
                        this.getSubject(request, response).login(jwtToken);
                        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
                        httpServletResponse.setHeader("Authorization", token);
                        httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
                        return true;
                    }else if(JWTUtil.getClaim(token, ConstantVariables.CURRENT_TIME_MILLIS).equals(redisUtil.get(ConstantVariables.PREFIX_SHIRO_REFRESH_TOKEN_OLD + username).toString())){
                        JWTToken jwtToken=new JWTToken(token);
                        this.getSubject(request,response).login(jwtToken);
                        return true;
                    }
                }
                redisUtil.releaseLock(username+"-Lock");
                break;
            }
        }

        return false;
    }
}