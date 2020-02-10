import cn.nju.edu.chemical_monitor_system.dao.ExpressDao;
import cn.nju.edu.chemical_monitor_system.entity.ExpressEntity;
import cn.nju.edu.chemical_monitor_system.utils.redis_util.RedisUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RedisTest extends BaseTest{
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    ExpressDao expressDao;
    @Test
    public void testSetAndGet()
    {
        ExpressEntity expressEntity=expressDao.findFirstByExpressId(3);
        redisUtil.set("express3",expressEntity);
        System.out.println(((ExpressEntity)redisUtil.get("express3")).getInputStoreId());
    }

}
