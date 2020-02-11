package cn.nju.edu.chemical_monitor_system.config;

import cn.nju.edu.chemical_monitor_system.interceptor.LoginHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginConfig implements WebMvcConfigurer {

    @Autowired
    LoginHandlerInterceptor loginHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
/*
        registry.addInterceptor(loginHandlerInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login", "/user/register")//登录注册页面不进行拦截
                .excludePathPatterns("/static/**");//静态资源不进行拦截，具体路径之后进行相应更改
*/

    }

}