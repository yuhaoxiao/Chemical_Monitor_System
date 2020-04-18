package cn.nju.edu.chemical_monitor_system.utils.shiro.cache;

import cn.nju.edu.chemical_monitor_system.constant.ConstantVariables;
import cn.nju.edu.chemical_monitor_system.utils.common.SpringContextUtil;
import cn.nju.edu.chemical_monitor_system.utils.redis.RedisUtil;
import cn.nju.edu.chemical_monitor_system.utils.shiro.jwt.JWTUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import java.util.*;

public class UserCache<K, V> implements Cache<K, V> {
    private String getKey(Object key) {
        return ConstantVariables.PREFIX_SHIRO_CACHE + JWTUtil.getClaim(key.toString(), ConstantVariables.USERNAME);
    }

    @Override
    public Object get(Object key) throws CacheException {
        RedisUtil redisUtil = (RedisUtil) SpringContextUtil.getBean("redisUtil");
        return redisUtil.get(getKey(key));
    }

    @Override
    public Object put(Object key, Object value) throws CacheException {
        RedisUtil redisUtil = (RedisUtil) SpringContextUtil.getBean("redisUtil");
        return redisUtil.set(getKey(key), value, ConstantVariables.SHIRO_CACHE_EXPIRE_TIME);
    }

    @Override
    public Object remove(Object key) throws CacheException {
        RedisUtil redisUtil = (RedisUtil) SpringContextUtil.getBean("redisUtil");
        if (get(key) != null) {
            redisUtil.del(getKey(key));
        }
        return null;
    }

    @Override
    public void clear() throws CacheException {
        RedisUtil redisUtil = (RedisUtil) SpringContextUtil.getBean("redisUtil");
        redisUtil.clearAll();
    }

    @Override
    public int size() {
        RedisUtil redisUtil = (RedisUtil) SpringContextUtil.getBean("redisUtil");
        return redisUtil.size();
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }
}
