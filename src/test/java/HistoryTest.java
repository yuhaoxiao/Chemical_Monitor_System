import cn.nju.edu.chemical_monitor_system.service.HistoryService;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class HistoryTest extends BaseTest{
    @Autowired
    private HistoryService historyService;
    @Test
    public void testSetAndGet() {
        Map<String, Map> result=historyService.getHistory(2);
        System.out.println(JSON.toJSONString(result));
    }
}
