import cn.nju.edu.chemical_monitor_system.utils.batch_history_util.BatchHistoryUtil;
import cn.nju.edu.chemical_monitor_system.utils.batch_history_util.HistoryNode;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HistoryTest extends BaseTest{
    @Autowired
    BatchHistoryUtil batchHistoryUtil;
    @Test
    public void testSetAndGet() {
        HistoryNode historyNode=batchHistoryUtil.getBeforeHistory(2);
        System.out.println(JSON.toJSONString(historyNode));
    }
}
