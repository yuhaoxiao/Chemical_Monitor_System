package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.response.BaseResponse;
import cn.nju.edu.chemical_monitor_system.service.UserService;
import cn.nju.edu.chemical_monitor_system.vo.UserVO;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public BaseResponse login(@RequestBody Map<String, String> map, HttpServletResponse httpServletResponse)  {
        String name = map.get("name");
        String password = map.get("password");
        return new BaseResponse(200,"登陆成功",userService.login(name,password,httpServletResponse));
    }

    @PostMapping(value = "/logout")
    public BaseResponse logout(HttpServletRequest httpServletRequest)  {
        userService.logout(httpServletRequest);
        return new BaseResponse(200,"注销成功",null);
    }

    @RequiresRoles(value={"administrator"})
    @PostMapping(value = "/register")
    public BaseResponse addUser(@RequestBody UserVO userVO) {
        String name = userVO.getName();
        String password = userVO.getPassword();
        String type = userVO.getType(); // 改成 int
        return new BaseResponse(200,"注册成功",userService.register(name, password, type));
    }

    @GetMapping(value = "/get_user")
    @RequiresRoles(logical = Logical.OR,value={"operator","administrator","monitor"})
    @RequiresAuthentication
    public BaseResponse getUser(int uid)  {
        return new BaseResponse(200,"成功",userService.getUser(uid));
    }

    @RequiresRoles(value={"administrator"})
    @PostMapping(value = "/delete_user")
    public BaseResponse deleteUser(int uid){
        return new BaseResponse(200,"成功",userService.deleteUser(uid));
    }

    @RequiresRoles(value={"administrator"})
    @PostMapping(value = "/update_user")
    public BaseResponse updateUser(UserVO userVO){
        return new BaseResponse(200,"成功",userService.updateUser(userVO));
    }
}
