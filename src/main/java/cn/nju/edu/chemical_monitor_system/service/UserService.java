package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.vo.UserVO;

import java.util.List;

public interface UserService {

    UserVO login(int id, String password);

    String register(String username, String password);

    UserVO getUser(int uid);
}
