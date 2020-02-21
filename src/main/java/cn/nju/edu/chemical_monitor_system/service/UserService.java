package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {

    UserVO login(String name, String password, HttpServletResponse httpServletResponse);

    UserVO register(String name, String password, String type);

    UserVO getUser(int uid);

    UserVO deleteUser(int uid);

    UserVO updateUser(UserVO userVO);

    void logout(HttpServletRequest httpServletRequest);
}
