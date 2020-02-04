package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.constant.BatchStatusEnum;
import cn.nju.edu.chemical_monitor_system.dao.*;
import cn.nju.edu.chemical_monitor_system.entity.BatchEntity;
import cn.nju.edu.chemical_monitor_system.entity.ProductionLineEntity;
import cn.nju.edu.chemical_monitor_system.service.BatchService;
import cn.nju.edu.chemical_monitor_system.vo.BatchVO;
import cn.nju.edu.chemical_monitor_system.vo.InOutBatchVO;
import cn.nju.edu.chemical_monitor_system.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BatchServiceImpl implements BatchService {

    @Autowired
    private BatchDao batchDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private InoutBatchDao inoutBatchDao;

    @Autowired
    private ProductionLineDao productionLineDao;

    @Autowired
    private UserDao userDao;

    @Override
    public BatchVO createBatch(int productlineId, Timestamp time, String type, int userId) {
        Optional<ProductionLineEntity> productionLineOpt = productionLineDao.findById(productlineId);

        if (!productionLineOpt.isPresent()) {
            return new BatchVO("生产线id不存在");
        }

        BatchEntity batchEntity = new BatchEntity();
        batchEntity.setProductionLineId(productlineId);
        batchEntity.setTime(time);
        batchEntity.setStatus(BatchStatusEnum.NOT_START.getName());
        batchEntity.setType(type);
        batchEntity.setUserEntity(userDao.findById(userId).get());
        batchDao.saveAndFlush(batchEntity);
        return new BatchVO(batchEntity);
    }

    @Override
    public BatchVO updateBatch(int batchId, String status) {
        Optional<BatchEntity> batchOpt = batchDao.findById(batchId);

        if(!batchOpt.isPresent()){
            return new BatchVO("批次id不存在");
        }

        BatchEntity batchEntity = batchOpt.get();
        batchEntity.setStatus(status);
        batchDao.saveAndFlush(batchEntity);
        return new BatchVO(batchEntity);
    }

    @Override
    public BatchVO getBatch(int batchId) {
        Optional<BatchEntity> batchOpt = batchDao.findById(batchId);

        if (!batchOpt.isPresent()) {
            return new BatchVO("批次id不存在");
        }

        return new BatchVO(batchOpt.get());
    }

    @Override
    public List<ProductVO> getBatchProduct(int batchId) {
        Optional<BatchEntity> batchOpt = batchDao.findById(batchId);

        if (!batchOpt.isPresent()) {
            return null;
        }

        return productDao.findByBatchId(batchId).stream().map(ProductVO::new).collect(Collectors.toList());
    }

    @Override
    public List<InOutBatchVO> getBatchInout(int batchId, boolean isIn) {
        Optional<BatchEntity> batchOpt = batchDao.findById(batchId);

        if (!batchOpt.isPresent()) {
            return null;
        }

        return inoutBatchDao.findByBatchIdAndInout(batchId, isIn ? 1 : 0).stream().map(InOutBatchVO::new).collect(Collectors.toList());
    }

}
