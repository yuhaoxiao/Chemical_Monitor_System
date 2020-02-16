package cn.nju.edu.chemical_monitor_system.utils.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author 肖宇豪
 */
public class JWTToken implements AuthenticationToken{

    // 密钥
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
