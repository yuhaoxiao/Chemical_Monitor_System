package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.dao.UserDao;
import cn.nju.edu.chemical_monitor_system.entity.BatchEntity;
import cn.nju.edu.chemical_monitor_system.entity.UserEntity;
import cn.nju.edu.chemical_monitor_system.service.UserService;
import cn.nju.edu.chemical_monitor_system.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        //以下为测试代码，测试级联保存，保存user同时会自动带上batch
        UserEntity userEntity=new UserEntity();
        BatchEntity b=new BatchEntity();
        b.setProductionLineId(1);
        b.setStatus("1");
        b.setTime(new Timestamp(System.currentTimeMillis()));
        b.setUserEntity(userEntity);
        List<BatchEntity> l=new ArrayList<>();
        l.add(b);
        userEntity.setUserId(1);
        userEntity.setBatchEntities(l);
        userEntity.setType("1");
        userEntity.setPassword(password);
        UserEntity result=userDao.save(userEntity);
        return null;
    }

    public UserVO getUser(int uid) {
        return null;
    }
}
