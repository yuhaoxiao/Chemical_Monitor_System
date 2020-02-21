package cn.nju.edu.chemical_monitor_system.utils.shiro;

import cn.nju.edu.chemical_monitor_system.constant.ConstantVariables;
import cn.nju.edu.chemical_monitor_system.dao.UserDao;
import cn.nju.edu.chemical_monitor_system.entity.PermissionEntity;
import cn.nju.edu.chemical_monitor_system.entity.RoleEntity;
import cn.nju.edu.chemical_monitor_system.entity.UserEntity;
import cn.nju.edu.chemical_monitor_system.utils.redis.RedisUtil;
import cn.nju.edu.chemical_monitor_system.utils.shiro.jwt.JWTToken;
import cn.nju.edu.chemical_monitor_system.utils.shiro.jwt.JWTUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserDao userDao;
    @Autowired
    RedisUtil redisUtil;

    private static final Logger logger = LoggerFactory.getLogger(MyRealm.class);

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 需要检测用户权限的时候会调用该方法
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = JWTUtil.getClaim(principals.toString(), ConstantVariables.USERNAME);
        logger.info("调用授权接口,调用用户为{}",username);
        UserEntity userEntity = userDao.findFirstByName(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (RoleEntity role : userEntity.getRoleEntities()) {
            simpleAuthorizationInfo.addRole(role.getRoleName());
            for (PermissionEntity permission : role.getPermissionEntities()) {
                simpleAuthorizationInfo.addStringPermission(permission.getPermissionName());
            }
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        String username = JWTUtil.getClaim(token, ConstantVariables.USERNAME);
        if (username == null) {
            throw new AuthenticationException("Token格式出错");
        }
        UserEntity userEntity = userDao.findFirstByName(username);
        if (userEntity == null) {
            throw new AuthenticationException("用户不存在!");
        }
        if (redisUtil.get(ConstantVariables.PREFIX_SHIRO_REFRESH_TOKEN_OLD + username)!=null) {
            String oldTime = redisUtil.get(ConstantVariables.PREFIX_SHIRO_REFRESH_TOKEN_OLD + username).toString();
            String time=JWTUtil.getClaim(token,ConstantVariables.CURRENT_TIME_MILLIS);
            // 判断旧Token是否一致
            if (time.equals(oldTime)) {
                return new SimpleAuthenticationInfo(token, token, "userRealm");
            }
        }
        if (JWTUtil.verify(token) && redisUtil.get(ConstantVariables.PREFIX_SHIRO_REFRESH_TOKEN + username) != null) {
            String currentTimeMillisRedis = redisUtil.get(ConstantVariables.PREFIX_SHIRO_REFRESH_TOKEN + username).toString();
            if (JWTUtil.getClaim(token, ConstantVariables.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                return new SimpleAuthenticationInfo(token, token, "userRealm");
            }
        }
        throw new AuthenticationException("Token已过期(Token expired or incorrect.)");
    }
}