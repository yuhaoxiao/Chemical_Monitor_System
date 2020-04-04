package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.constant.BatchStatusEnum;
import cn.nju.edu.chemical_monitor_system.constant.BatchTypeEnum;
import cn.nju.edu.chemical_monitor_system.constant.InOutBatchStatusEnum;
import cn.nju.edu.chemical_monitor_system.dao.*;
import cn.nju.edu.chemical_monitor_system.entity.*;
import cn.nju.edu.chemical_monitor_system.request.BatchOutRequest;
import cn.nju.edu.chemical_monitor_system.request.CreateBatchRequest;
import cn.nju.edu.chemical_monitor_system.service.BatchService;
import cn.nju.edu.chemical_monitor_system.vo.BatchVO;
import cn.nju.edu.chemical_monitor_system.vo.InOutBatchVO;
import cn.nju.edu.chemical_monitor_system.vo.ProductVO;
import cn.nju.edu.chemical_monitor_system.vo.StoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
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
    private StoreDao storeDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CasDao casDao;

    private String[] batchTypes = {BatchTypeEnum.PRODUCE.getName(), BatchTypeEnum.IN_PARK.getName(), BatchTypeEnum.OUT_PARK.getName(), BatchTypeEnum.DESTROY.getName()};

    @Override
    public BatchVO createBatch(int productlineId, int type, List<CreateBatchRequest.RawRequest> raws, int userId) {
        Optional<ProductionLineEntity> productionLineOpt = productionLineDao.findById(productlineId);

        if (!productionLineOpt.isPresent()) {
            return new BatchVO("生产线id不存在");
        }

        if (type < 0 || type > 3) {
            return new BatchVO("种类不合法");
        }

        BatchEntity batchEntity = new BatchEntity();
        batchEntity.setProductionLineId(productlineId);
        batchEntity.setTime(new Timestamp(System.currentTimeMillis()));
        batchEntity.setStatus(BatchStatusEnum.NOT_START.getName());
        batchEntity.setType(batchTypes[type]);
        batchEntity.setUserEntity(userDao.findById(userId).get());
        batchDao.saveAndFlush(batchEntity);
        BatchVO batchVO = new BatchVO(batchEntity);

        if (raws == null || raws.size() == 0) {
            return new BatchVO(batchEntity);
        }

        List<InOutBatchEntity> inOutBatchEntities = new ArrayList<>();

        for (CreateBatchRequest.RawRequest request : raws) {
            InOutBatchEntity inOutBatchEntity = new InOutBatchEntity();
            inOutBatchEntity.setBatchId(batchEntity.getBatchId());
            inOutBatchEntity.setProductId(request.getProductId());
            inOutBatchEntity.setStoreId(request.getStoreId());
            inOutBatchEntity.setNumber(request.getNumber());
            inOutBatchEntity.setStatus(InOutBatchStatusEnum.NOT_START.getName());
            inOutBatchEntity.setFinishedNumber(0.0);
            inOutBatchEntity.setInout(1);
            inOutBatchEntities.add(inOutBatchEntity);
        }

        inoutBatchDao.saveAll(inOutBatchEntities);
        List<InOutBatchVO> InOutBatchVOs = inOutBatchEntities.stream().map(InOutBatchVO::new).collect(Collectors.toList());
        batchVO.setInOutBatchVOS(InOutBatchVOs);

        return new BatchVO(batchEntity);
    }

    @Override
    public BatchVO batchOut(int batchId, List<BatchOutRequest.ProductRequest> products) {
        Optional<BatchEntity> batchOpt = batchDao.findById(batchId);

        if (!batchOpt.isPresent()) {
            return new BatchVO("批次id不存在");
        }

        List<ProductEntity> productEntities = new ArrayList<>();
        HashMap<Integer, Integer> casStoreMap = new HashMap<>();
        for (BatchOutRequest.ProductRequest request : products) {
            int casId = request.getCasId();
            int storeId = request.getStoreId();
            double number = request.getNumber();

            Optional<CasEntity> casOpt = casDao.findById(casId);
            Optional<StoreEntity> storeOpt = storeDao.findById(storeId);

            if (!casOpt.isPresent()) {
                return new BatchVO("CAS id " + casId + " 不存在");
            }

            if (!storeOpt.isPresent()) {
                return new BatchVO("仓库id " + storeId + " 不存在");
            }

            ProductEntity productEntity = new ProductEntity();
            productEntity.setBatchId(batchId);
            productEntity.setCasEntity(casOpt.get());
            productEntity.setNumber(number);
            productEntities.add(productEntity);
            casStoreMap.put(casId, storeId);
        }

        productDao.saveAll(productEntities);
        List<InOutBatchEntity> inOutBatchEntities = new ArrayList<>();

        for (ProductEntity productEntity : productEntities) {
            InOutBatchEntity inOutBatchEntity = new InOutBatchEntity();
            inOutBatchEntity.setInout(1);
            inOutBatchEntity.setFinishedNumber(0.0);
            inOutBatchEntity.setStatus(InOutBatchStatusEnum.NOT_START.getName());
            inOutBatchEntity.setNumber(productEntity.getNumber());
            inOutBatchEntity.setStoreId(casStoreMap.get(productEntity.getCasEntity().getCasId()));
            inOutBatchEntity.setProductId(productEntity.getProductId());
            inOutBatchEntity.setBatchId(batchId);
            inOutBatchEntities.add(inOutBatchEntity);
        }

        inoutBatchDao.saveAll(inOutBatchEntities);
        BatchVO batchVO = new BatchVO(batchOpt.get());
        List<InOutBatchEntity> inOuts = inoutBatchDao.findByBatchId(batchId);
        batchVO.setInOutBatchVOS(inOuts.stream().map(InOutBatchVO::new).collect(Collectors.toList()));
        return batchVO;
    }

    @Override
    public BatchVO getBatch(int batchId) {
        Optional<BatchEntity> batchOpt = batchDao.findById(batchId);

        return batchOpt.map(BatchVO::new).orElseGet(() -> new BatchVO("批次id不存在"));

    }

    @Override
    public List<StoreVO> getBatchStores(int batchId, int isIn) {
        Optional<BatchEntity> batchOpt = batchDao.findById(batchId);

        if (!batchOpt.isPresent()) {
            return null;
        }

        List<InOutBatchEntity> inOutBatchEntities = inoutBatchDao.findByBatchIdAndInout(batchId, isIn);
        List<Integer> storeIds = inOutBatchEntities.stream().map(InOutBatchEntity::getStoreId).distinct().collect(Collectors.toList());
        List<StoreVO> storeVOS = new ArrayList<>();
        for (Integer storeId : storeIds) {
            Optional<StoreEntity> storeOpt = storeDao.findById(storeId);
            if(storeOpt.isPresent()){
                storeVOS.add(new StoreVO(storeOpt.get()));
            }
        }
        return storeVOS;
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
