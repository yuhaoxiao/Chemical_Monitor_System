package cn.nju.edu.chemical_monitor_system.constant;

public class ConstantVariables {

    public final static int featureNums = 8;//特征值个数
    public final static int iterationNum = 100;//迭代次数
    public final static int clusterNum = 3;//簇的个数
    public final static int maxFusionPoint = 9;
    public final static int maxBoilingPoint = 9;
    public final static int maxExistType = 2;
    public final static int maxIsOrganic = 1;
    public final static int maxOxidation = 1;
    public final static int maxReducibility = 1;
    public final static int maxInflammability = 1;
    public final static int maxExplosion = 1;

    //消息队列topic
    public final static String MANAGER_MESSAGE = "ManagerMessage";
    public final static String KAFKA_ID="1";

    public final static String PREFIX_SHIRO_CACHE="shiro.cache";
    public static final String PREFIX_SHIRO_REFRESH_TOKEN = "shiro:refresh_token:";
    public static final String PREFIX_SHIRO_REFRESH_TOKEN_OLD = "shiro:refresh_token_old:";
    //shiroCache缓存时间
    public static final Integer SHIRO_CACHE_EXPIRE_TIME=300;
    //refresh_token过期时间30分钟
    public static final Integer REFRESH_TOKEN_EXPIRE_TIME=1800;
    //防止并发的临时refresh_token过期时间为15秒
    public static final Integer REFRESH_TOKEN_EXPIRE_TIME_OLD=30;
    //token过期时间5分钟
    public static final Integer EXPIRE_TIME = 5*60*1000;
    //jwt秘钥
    public static final String ENCRYPT_JWT="U0JBUElKV1RkV2FuZzkyNjQ1NA==";
    public static final String USERNAME="USERNAME";
    public static final String CURRENT_TIME_MILLIS = "currentTimeMillis";
    //强制所有url都得登录
    public static final boolean MUST_LOGIN=true;
}
