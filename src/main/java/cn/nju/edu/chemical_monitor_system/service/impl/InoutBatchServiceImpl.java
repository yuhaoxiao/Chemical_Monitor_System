package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.dao.BatchDao;
import cn.nju.edu.chemical_monitor_system.dao.InoutBatchDao;
import cn.nju.edu.chemical_monitor_system.dao.ProductDao;
import cn.nju.edu.chemical_monitor_system.dao.StoreDao;
import cn.nju.edu.chemical_monitor_system.entity.BatchEntity;
import cn.nju.edu.chemical_monitor_system.entity.InOutBatchEntity;
import cn.nju.edu.chemical_monitor_system.entity.ProductEntity;
import cn.nju.edu.chemical_monitor_system.entity.StoreEntity;
import cn.nju.edu.chemical_monitor_system.service.InoutBatchService;
import cn.nju.edu.chemical_monitor_system.vo.InOutBatchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InoutBatchServiceImpl implements InoutBatchService {

    @Autowired
    private InoutBatchDao inoutBatchDao;

    @Autowired
    private BatchDao batchDao;

    @Autowired
    private StoreDao storeDao;

    @Autowired
    private ProductDao productDao;

    @Override
    public InOutBatchVO getInout(int ioId) {
        Optional<InOutBatchEntity> inOutBatchOpt = inoutBatchDao.findById(ioId);

        if (!inOutBatchOpt.isPresent()) {
            return new InOutBatchVO("上下线id不存在");
        }

        return new InOutBatchVO(inOutBatchOpt.get());
    }

    @Override
    public InOutBatchVO createInout(int batchId, int storeId, int productId, double number, boolean isIn) {
        Optional<BatchEntity> batchOpt = batchDao.findById(batchId);
        Optional<StoreEntity> storeOpt = storeDao.findById(storeId);
        Optional<ProductEntity> productOpt = productDao.findById(productId);

        if (!batchOpt.isPresent()) {
            return new InOutBatchVO("批次id不存在");
        }

        if (!storeOpt.isPresent()) {
            return new InOutBatchVO("仓库id不存在");
        }

        if (!productOpt.isPresent()) {
            return new InOutBatchVO("产品id不存在");
        }

        InOutBatchEntity inOutBatchEntity = new InOutBatchEntity();
        inOutBatchEntity.setBatchId(batchId);
        inOutBatchEntity.setProductId(productId);
        inOutBatchEntity.setStoreId(storeId);
        inOutBatchEntity.setStatus("not start");
        inOutBatchEntity.setNumber(number);
        inOutBatchEntity.setInout(isIn ? 1 : 0);
        inoutBatchDao.saveAndFlush(inOutBatchEntity);
        return new InOutBatchVO(inOutBatchEntity);
    }

    @Override
    public InOutBatchVO updateInout(int ioId, String status) {
        Optional<InOutBatchEntity> inOutBatchOpt = inoutBatchDao.findById(ioId);

        if (!inOutBatchOpt.isPresent()) {
            return new InOutBatchVO("上下线id不存在");
        }

        InOutBatchEntity inOutBatchEntity = inOutBatchOpt.get();
        inOutBatchEntity.setStatus(status);
        inoutBatchDao.saveAndFlush(inOutBatchEntity);
        return new InOutBatchVO(inOutBatchEntity);
    }
}
