import cn.nju.edu.chemical_monitor_system.utils.redis_util.RedisUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RedisTest extends BaseTest{
    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void testGetEntFileById(){
        redisUtil.set("xyh","123");
    }

}
