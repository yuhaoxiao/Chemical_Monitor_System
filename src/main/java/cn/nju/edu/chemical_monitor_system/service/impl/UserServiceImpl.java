package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.dao.UserDao;
import cn.nju.edu.chemical_monitor_system.entity.UserEntity;
import cn.nju.edu.chemical_monitor_system.service.UserService;
import cn.nju.edu.chemical_monitor_system.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public UserVO login(String name, String password) {
        UserEntity user = userDao.findFirstByName(name);

        if (user == null) {
            return new UserVO("用户不存在");
        }

        if (!user.getPassword().equals(password)) {
            return new UserVO("密码错误");
        }

        return new UserVO(user);
    }

    public UserVO register(String name, String password) {
        UserEntity u = userDao.findFirstByName(name);

        if (u != null) {
            return new UserVO("用户名重复");
        }

        UserEntity user = new UserEntity();
        user.setName(name);
        user.setPassword(password);
        userDao.saveAndFlush(user);

        return new UserVO(user);
    }

    public UserVO getUser(int uid) {
        Optional<UserEntity> user = userDao.findById(uid);

        if (!user.isPresent()) {
            return new UserVO("用户id不存在");
        }

        return new UserVO(user.get());
    }
}
