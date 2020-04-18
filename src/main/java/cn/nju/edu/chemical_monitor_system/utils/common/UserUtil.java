package cn.nju.edu.chemical_monitor_system.utils.common;

import cn.nju.edu.chemical_monitor_system.constant.ConstantVariables;
import cn.nju.edu.chemical_monitor_system.dao.UserDao;
import cn.nju.edu.chemical_monitor_system.entity.UserEntity;
import cn.nju.edu.chemical_monitor_system.exception.UserException;
import cn.nju.edu.chemical_monitor_system.utils.shiro.jwt.JWTUtil;
import cn.nju.edu.chemical_monitor_system.vo.UserVO;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {

    @Autowired
    UserDao userDao;

    public UserVO getUser() {
        String token = SecurityUtils.getSubject().getPrincipal().toString();
        String username = JWTUtil.getClaim(token, ConstantVariables.USERNAME);
        UserEntity userEntity = userDao.findFirstByName(username);
        if (userEntity == null) {
            throw new UserException("该帐号不存在");
        }
        return new UserVO(userEntity);
    }

    public Integer getUserId() {
        return getUser().getUserId();
    }

    public String getToken() {
        return SecurityUtils.getSubject().getPrincipal().toString();
    }

    public String getUsername() {
        String token = SecurityUtils.getSubject().getPrincipal().toString();
        // 解密获得Account
        return JWTUtil.getClaim(token, ConstantVariables.USERNAME);
    }
}
