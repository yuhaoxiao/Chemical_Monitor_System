package cn.nju.edu.chemical_monitor_system.interceptor;


import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("User");
        if (user == null) {
            System.out.println("没有权限请先登陆");
            request.setAttribute("msg", "没有权限请先登陆");
            //request.getRequestDispatcher("/user/login").forward(request, response);//重定向到登录界面
            return false;
        } else {
            return true;
        }
    }
}