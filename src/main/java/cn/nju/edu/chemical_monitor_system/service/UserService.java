package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.vo.UserVO;

import java.util.List;

public interface UserService {

    UserVO login(String name, String password);

    UserVO register(String name, String password);

    UserVO getUser(int uid);
}
