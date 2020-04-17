import cn.nju.edu.chemical_monitor_system.dao.BatchDao;
import cn.nju.edu.chemical_monitor_system.dao.InoutBatchDao;
import cn.nju.edu.chemical_monitor_system.dao.StoreDao;
import cn.nju.edu.chemical_monitor_system.entity.BatchEntity;
import cn.nju.edu.chemical_monitor_system.entity.InOutBatchEntity;
import cn.nju.edu.chemical_monitor_system.entity.StoreEntity;
import cn.nju.edu.chemical_monitor_system.service.InOutBatchService;
import cn.nju.edu.chemical_monitor_system.service.impl.InOutBatchServiceImpl;
import cn.nju.edu.chemical_monitor_system.utils.rfid.RfidUtil;
import cn.nju.edu.chemical_monitor_system.vo.InOutBatchVO;
import lombok.ToString;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class InputBatchTest {

    @InjectMocks
    private InOutBatchService inOutBatchService = new InOutBatchServiceImpl();

    @Mock
    private BatchDao batchDao;

    @Mock
    private InoutBatchDao inoutBatchDao;

    @Mock
    private StoreDao storeDao;

    @Mock
    private RfidUtil rfidUtil;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(batchDao.findFirstByBatchId(any(Integer.class))).thenReturn(new BatchEntity());
        Mockito.when(batchDao.saveAndFlush(any(BatchEntity.class))).thenReturn(new BatchEntity());
        List<InOutBatchEntity> inOutBatchEntities = new ArrayList<>();
        Mockito.when(inoutBatchDao.findByBatchIdAndInout(any(Integer.class), any(Integer.class))).thenReturn(inOutBatchEntities);
        Mockito.when(inoutBatchDao.saveAndFlush(any(InOutBatchEntity.class))).thenReturn(new InOutBatchEntity());
        Mockito.when(storeDao.findFirstByStoreId(any(Integer.class))).thenReturn(new StoreEntity());
        Mockito.when(rfidUtil.read(any(String.class))).thenReturn("");
    }

    @Test
    public void testInputBatch(){
        InOutBatchVO inOutBatchVO = inOutBatchService.inputBatch(1,1);
        Assert.assertEquals(inOutBatchVO.getCode(),2);
    }
}
