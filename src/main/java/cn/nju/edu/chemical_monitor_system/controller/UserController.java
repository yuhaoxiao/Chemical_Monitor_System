package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.service.UserService;
import cn.nju.edu.chemical_monitor_system.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/user/login")
    public UserVO login(int id, String password, HttpServletRequest request) {

        UserVO user = userService.login(id, password);
        if (user.getCode() == 1) {
            if (request.getSession(false) != null)
                request.getSession(false).invalidate();
            request.getSession(true);
            request.getSession().setAttribute("User", user);
        }
        System.out.println("login happens!" + new Date());
        return user;
    }

    @PostMapping(value = "/user/register")
    public String register(String userId, String password) {
        return userService.register(userId, password);
    }

    @GetMapping(value = "/user/get_user")
    public UserVO getUser(int uid) {
        return userService.getUser(uid);
    }
}
