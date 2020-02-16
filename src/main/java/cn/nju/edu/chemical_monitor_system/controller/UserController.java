package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.constant.UserStatusEnum;
import cn.nju.edu.chemical_monitor_system.response.BaseResponse;
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
    public BaseResponse login(String name, String password, HttpServletRequest request) {
        return userService.login(name,password);
    }

    @PostMapping(value = "/user/register")
    public UserVO register(String name, String password, String type) {
        return userService.register(name, password, type);
    }

    @GetMapping(value = "/user/get_user")
    public UserVO getUser(int uid) {
        return userService.getUser(uid);
    }

    @PostMapping(value = "/user/delete_user")
    public UserVO deleteUser(int uid){
        return userService.deleteUser(uid);
    }

    @PostMapping(value = "/user/update_user")
    public UserVO updateUser(UserVO userVO){
        return userService.updateUser(userVO);
    }
}
