package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.dao.UserDao;
import cn.nju.edu.chemical_monitor_system.entity.UserEntity;
import cn.nju.edu.chemical_monitor_system.service.UserService;
import cn.nju.edu.chemical_monitor_system.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public UserVO login(int id, String password) {
        UserEntity user = userDao.findFirstByUserId(id);

        if (user == null || !user.getPassword().equals(password)) {
            return new UserVO();
        }

        return new UserVO(user);
    }

    public String register(String username, String password) {
        return null;
    }

    public UserVO getUser(int uid) {
        return null;
    }
}
