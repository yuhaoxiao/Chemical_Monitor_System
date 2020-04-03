package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.constant.ConstantVariables;
import cn.nju.edu.chemical_monitor_system.constant.UserTypeEnum;
import cn.nju.edu.chemical_monitor_system.dao.BatchDao;
import cn.nju.edu.chemical_monitor_system.dao.ExpressDao;
import cn.nju.edu.chemical_monitor_system.dao.RoleDao;
import cn.nju.edu.chemical_monitor_system.dao.UserDao;
import cn.nju.edu.chemical_monitor_system.entity.BatchEntity;
import cn.nju.edu.chemical_monitor_system.entity.ExpressEntity;
import cn.nju.edu.chemical_monitor_system.entity.RoleEntity;
import cn.nju.edu.chemical_monitor_system.entity.UserEntity;
import cn.nju.edu.chemical_monitor_system.service.UserService;
import cn.nju.edu.chemical_monitor_system.utils.redis.RedisUtil;
import cn.nju.edu.chemical_monitor_system.utils.shiro.jwt.JWTUtil;
import cn.nju.edu.chemical_monitor_system.exception.UnauthorizedException;
import cn.nju.edu.chemical_monitor_system.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BatchDao batchDao;

    @Autowired
    private ExpressDao expressDao;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RoleDao roleDao;


    public UserVO login(String name, String password, HttpServletResponse httpServletResponse) {
        UserEntity user = userDao.findFirstByName(name);
        if (user.getPassword().equals(password)) {
            if (redisUtil.get(ConstantVariables.PREFIX_SHIRO_CACHE + user.getName()) != null) {
                redisUtil.del(ConstantVariables.PREFIX_SHIRO_CACHE + user.getName());
            }
            String currentTimeMillis = String.valueOf(System.currentTimeMillis());
            redisUtil.set(ConstantVariables.PREFIX_SHIRO_REFRESH_TOKEN + user.getName(), currentTimeMillis, ConstantVariables.REFRESH_TOKEN_EXPIRE_TIME);
            String token = JWTUtil.sign(user.getName(), currentTimeMillis);
            httpServletResponse.setHeader("Authorization", token);
            httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
            return new UserVO(user);
        } else {
            throw new UnauthorizedException("密码错误");
        }
    }

    public UserVO register(String name, String password, String type) {
        UserEntity u = userDao.findFirstByName(name);

        if (u != null) {
            throw new UnauthorizedException("用户名重复");
        }

        UserEntity user = new UserEntity();
        user.setName(name);
        user.setPassword(password);
        user.setType(type);
        user.setEnable(1);
        RoleEntity roleEntity=roleDao.findById(Integer.parseInt(type));
        roleEntity.getUserEntities().add(user);
        roleDao.saveAndFlush(roleEntity);
        user=userDao.findFirstByName(name);
        user.getRoleEntities().add(roleEntity);
        userDao.saveAndFlush(user);
        return new UserVO(user);
    }

    public UserVO getUser(int uid) {
        Optional<UserEntity> user = userDao.findById(uid);

        if (!user.isPresent()) {
            throw new UnauthorizedException("用户不存在");
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
            List<BatchEntity> batchEntities = batchDao.findByUserEntity(user.get());

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

            for (BatchEntity batch : batchEntities) {
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
        //userEntity.setEnable(userVO.getEnable());
        userEntity.setPassword(userVO.getPassword());
        userEntity.setType(userVO.getType());  // TODO: 修改权限
        //userEntity.setUserId(userVO.getUserId());
        userEntity.setName(userVO.getName());
        userDao.saveAndFlush(userEntity);
        return new UserVO(userEntity);
    }

    @Override
    public void logout(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        String username=JWTUtil.getClaim(authorization,ConstantVariables.USERNAME);
        if (redisUtil.get(ConstantVariables.PREFIX_SHIRO_CACHE + username) != null) {
            redisUtil.del(ConstantVariables.PREFIX_SHIRO_CACHE + username);
        }
        if(redisUtil.get(ConstantVariables.PREFIX_SHIRO_REFRESH_TOKEN + username)!=null){
            redisUtil.del(ConstantVariables.PREFIX_SHIRO_REFRESH_TOKEN + username);
        }
        if(redisUtil.get(ConstantVariables.PREFIX_SHIRO_REFRESH_TOKEN_OLD + username)!=null){
            redisUtil.del(ConstantVariables.PREFIX_SHIRO_REFRESH_TOKEN_OLD + username);
        }
    }

    @Override
    public List<UserVO> getAll() {
        return userDao.findAll().stream().map(UserVO::new).collect(Collectors.toList());
    }
}
