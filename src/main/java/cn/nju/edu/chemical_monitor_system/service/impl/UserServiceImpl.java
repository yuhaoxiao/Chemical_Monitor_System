package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.dao.BatchDao;
import cn.nju.edu.chemical_monitor_system.dao.ExpressDao;
import cn.nju.edu.chemical_monitor_system.dao.UserDao;
import cn.nju.edu.chemical_monitor_system.entity.BatchEntity;
import cn.nju.edu.chemical_monitor_system.entity.ExpressEntity;
import cn.nju.edu.chemical_monitor_system.entity.UserEntity;
import cn.nju.edu.chemical_monitor_system.service.UserService;
import cn.nju.edu.chemical_monitor_system.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BatchDao batchDao;

    @Autowired
    private ExpressDao expressDao;

    public UserVO login(String name, String password) {
        UserEntity user = userDao.findFirstByName(name);

        if (user == null) {
            return new UserVO("用户不存在");
        }

        if (!user.getPassword().equals(password)) {
            return new UserVO("密码错误");
        }

        if (user.getEnable() == 0) {
            return new UserVO("账号被删除，无法登录");
        }

        return new UserVO(user);
    }

    public UserVO register(String name, String password, String type) {
        UserEntity u = userDao.findFirstByName(name);

        if (u != null) {
            return new UserVO("用户名重复");
        }

        UserEntity user = new UserEntity();
        user.setName(name);
        user.setPassword(password);
        user.setType(type);
        userDao.saveAndFlush(user);

        return new UserVO(user);
    }

    public UserVO getUser(int uid) {
        Optional<UserEntity> user = userDao.findById(uid);

        if (!user.isPresent()) {
            return new UserVO("用户id不存在");
        }

        UserVO userVO = new UserVO(user.get());

        try {
            DateFormat dateFormat;
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            java.util.Date timeDate = dateFormat.parse("2000-01-01");//util类型
            Timestamp last = new Timestamp(timeDate.getTime());

            List<ExpressEntity> inputExpress = expressDao.findByInputUserId(uid);
            List<ExpressEntity> outputExpress = expressDao.findByOutputUserId(uid);
            List<BatchEntity> batchEntitiess = batchDao.findByUserEntity(user.get());

            for (ExpressEntity e : inputExpress) {
                if (e.getInputTime().after(last)) {
                    last = e.getInputTime();
                }
            }

            for (ExpressEntity e : outputExpress) {
                if (e.getOutputTime().after(last)) {
                    last = e.getOutputTime();
                }
            }

            for (BatchEntity batch : batchEntitiess) {
                if (batch.getTime().after(last)) {
                    last = batch.getTime();
                }
            }

            userVO.setLastOperationTime(last);
        } catch (Exception e) {
        }
        return userVO;
    }

    @Override
    public UserVO deleteUser(int uid) {
        Optional<UserEntity> user = userDao.findById(uid);

        if (!user.isPresent()) {
            return new UserVO("用户id不存在");
        }

        UserEntity userEntity = user.get();
        userEntity.setEnable(0);
        userDao.saveAndFlush(userEntity);
        return new UserVO(userEntity);
    }

    @Override
    public UserVO updateUser(UserVO userVO) {
        Optional<UserEntity> user = userDao.findById(userVO.getUserId());

        if (!user.isPresent()) {
            return new UserVO("用户id不存在");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setEnable(userVO.getEnable());
        userEntity.setPassword(userVO.getPassword());
        userEntity.setType(userVO.getType());
        userEntity.setUserId(userVO.getUserId());
        userEntity.setName(userVO.getName());
        userDao.saveAndFlush(userEntity);
        return new UserVO(userEntity);
    }
}
