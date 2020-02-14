package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.constant.BatchStatusEnum;
import cn.nju.edu.chemical_monitor_system.constant.InOutBatchStatusEnum;
import cn.nju.edu.chemical_monitor_system.dao.*;
import cn.nju.edu.chemical_monitor_system.entity.*;
import cn.nju.edu.chemical_monitor_system.service.InOutBatchService;
import cn.nju.edu.chemical_monitor_system.utils.rfid_util.RfidUtil;
import cn.nju.edu.chemical_monitor_system.vo.InOutBatchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InOutBatchServiceImpl implements InOutBatchService {

    @Autowired
    private InoutBatchDao inoutBatchDao;

    @Autowired
    private BatchDao batchDao;

    @Autowired
    private StoreDao storeDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CasDao casDao;

    @Autowired
    private RfidUtil rfidUtil;

    @Override
    public InOutBatchVO getInout(int ioId) {
        Optional<InOutBatchEntity> inOutBatchOpt = inoutBatchDao.findById(ioId);

        if (!inOutBatchOpt.isPresent()) {
            return new InOutBatchVO("上下线id不存在");
        }

        return new InOutBatchVO(inOutBatchOpt.get());
    }

    @Override
    public InOutBatchVO inputBatch(int batchId) {
        Optional<BatchEntity> batchOpt = batchDao.findById(batchId);

        if (!batchOpt.isPresent()) {
            return new InOutBatchVO("批次id不存在");
        }

        BatchEntity batchEntity = batchOpt.get();
        if (batchEntity.getStatus().equals(BatchStatusEnum.NOT_START.getName())) {
            batchEntity.setStatus(BatchStatusEnum.IN_BATCH.getName());
            batchDao.saveAndFlush(batchEntity);
        }

        List<InOutBatchEntity> inOutBatchs = inoutBatchDao.findByBatchIdAndInout(batchId, 1);

        if (inOutBatchs == null || inOutBatchs.size() == 0) {
            return new InOutBatchVO("该批次没有原材料");
        }

        int storeId = inOutBatchs.get(0).getStoreId();
        String port = storeDao.findById(storeId).get().getPort();
        String rfidInfo = rfidUtil.read(port);
        RfidInfoEntity rfidInfoEntity = new RfidInfoEntity(rfidInfo);
        int productId = rfidInfoEntity.getProductId();

        for (InOutBatchEntity inOutBatchEntity : inOutBatchs) {
            if (inOutBatchEntity.getProductId() != productId) {
                continue;
            }

            Double finishedNumber = inOutBatchEntity.getFinishedNumber() + rfidInfoEntity.getNumber();

            if (finishedNumber > inOutBatchEntity.getNumber()) {
                return new InOutBatchVO("上线了数量为" + rfidInfoEntity.getNumber() + "的产品id为" + productId +
                        "的产品，超出了所需的" + (inOutBatchEntity.getNumber() - finishedNumber) + "的数量");
            } else {
                inOutBatchEntity.setFinishedNumber(finishedNumber);

                if (finishedNumber < inOutBatchEntity.getNumber()) {
                    inoutBatchDao.saveAndFlush(inOutBatchEntity);
                    return new InOutBatchVO(inOutBatchEntity);
                }

                inOutBatchEntity.setStatus(InOutBatchStatusEnum.COMPLETED.getName());
                inoutBatchDao.saveAndFlush(inOutBatchEntity);
                List<InOutBatchEntity> newInOutBatchs = inoutBatchDao.findByBatchIdAndInout(batchId, 1);

                for (InOutBatchEntity newInoutBatch : newInOutBatchs) {
                    if (!newInoutBatch.getStatus().equals(InOutBatchStatusEnum.COMPLETED.getName())) {
                        return new InOutBatchVO(inOutBatchEntity);
                    }
                }

                InOutBatchVO inOutBatchVO = new InOutBatchVO(inOutBatchEntity);
                inOutBatchVO.setCode(2);
                BatchEntity batch = batchDao.findById(batchId).get();
                batch.setStatus(BatchStatusEnum.IN_PROCESS.getName());
                batchDao.saveAndFlush(batch);
                return inOutBatchVO;
            }
        }

        return new InOutBatchVO("该批次没有产品id为" + productId + "的原材料");
    }

    @Override
    public InOutBatchVO outputBatch(int batchId, int productId, double number) {
        Optional<BatchEntity> batchOpt = batchDao.findById(batchId);
        Optional<ProductEntity> productOpt = productDao.findById(productId);

        if (!batchOpt.isPresent()) {
            return new InOutBatchVO("批次id不存在");
        }

        BatchEntity batchEntity = batchOpt.get();
        if (batchEntity.getStatus().equals(BatchStatusEnum.IN_PROCESS.getName())) {
            batchEntity.setStatus(BatchStatusEnum.OUT_BATCH.getName());
            batchDao.saveAndFlush(batchEntity);
        }

        if (!productOpt.isPresent()) {
            return new InOutBatchVO("产品id不存在");
        }

        List<InOutBatchEntity> inOutBatchs = inoutBatchDao.findByBatchIdAndInout(batchId, 0);

        if (inOutBatchs == null || inOutBatchs.size() == 0) {
            return new InOutBatchVO("该批次没有产品");
        }

        int storeId = inOutBatchs.get(0).getStoreId();
        String port = storeDao.findById(storeId).get().getPort();

        ProductEntity productEntity = productOpt.get();
        RfidInfoEntity rfidInfoEntity = new RfidInfoEntity();
        rfidInfoEntity.setBatchId(batchId);
        rfidInfoEntity.setProductId(productId);
        rfidInfoEntity.setCasId(productEntity.getCasEntity().getCasId());
        rfidInfoEntity.setExpressId(0);
        rfidInfoEntity.setNumber(number);

        for (InOutBatchEntity inOutBatchEntity : inOutBatchs) {
            if (inOutBatchEntity.getProductId() != productId) {
                continue;
            }

            Double finishedNumber = inOutBatchEntity.getFinishedNumber() + rfidInfoEntity.getNumber();

            if (finishedNumber > inOutBatchEntity.getNumber()) {
                return new InOutBatchVO("下线了数量为" + rfidInfoEntity.getNumber() + "的产品id为" + productId +
                        "的产品，超出了所需的" + (inOutBatchEntity.getNumber() - finishedNumber) + "的数量");
            } else {
                inOutBatchEntity.setFinishedNumber(finishedNumber);

                if (finishedNumber < inOutBatchEntity.getNumber()) {
                    inoutBatchDao.saveAndFlush(inOutBatchEntity);
                    rfidUtil.write(rfidInfoEntity.toString(), port);
                    return new InOutBatchVO(inOutBatchEntity);
                }

                inOutBatchEntity.setStatus(InOutBatchStatusEnum.COMPLETED.getName());
                inoutBatchDao.saveAndFlush(inOutBatchEntity);
                List<InOutBatchEntity> newInOutBatchs = inoutBatchDao.findByBatchIdAndInout(batchId, 0);

                for (InOutBatchEntity newInoutBatch : newInOutBatchs) {
                    if (!newInoutBatch.getStatus().equals(InOutBatchStatusEnum.COMPLETED.getName())) {
                        return new InOutBatchVO(inOutBatchEntity);
                    }
                }

                InOutBatchVO inOutBatchVO = new InOutBatchVO(inOutBatchEntity);
                inOutBatchVO.setCode(2);
                BatchEntity batch = batchDao.findById(batchId).get();
                batch.setStatus(BatchStatusEnum.COMPLETE.getName());
                batchDao.saveAndFlush(batch);
                rfidUtil.write(rfidInfoEntity.toString(), port);
                return inOutBatchVO;
            }
        }

        return new InOutBatchVO("该批次没有产品id为" + productId + "的产品");
    }

    @Override
    public List<InOutBatchVO> addProduct(int batchId, Map<Integer, Double> casNumberMap, int storeId) {
        Optional<BatchEntity> batchOpt = batchDao.findById(batchId);
        Optional<StoreEntity> storeOpt = storeDao.findById(storeId);

        if (!batchOpt.isPresent() || !storeOpt.isPresent()) {
            return null;
        }

        List<ProductEntity> productEntities = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : casNumberMap.entrySet()) {
            int casId = entry.getKey();
            double number = entry.getValue();

            Optional<CasEntity> casOpt = casDao.findById(casId);
            if (!casOpt.isPresent()) {
                return null;
            }

            ProductEntity productEntity = new ProductEntity();
            productEntity.setBatchId(batchId);
            productEntity.setCasEntity(casOpt.get());
            productEntity.setNumber(number);
            productEntities.add(productEntity);
        }

        productDao.saveAll(productEntities);
        List<InOutBatchEntity> inOutBatchEntities = new ArrayList<>();

        for (ProductEntity productEntity : productEntities) {
            InOutBatchEntity inOutBatchEntity = new InOutBatchEntity();
            inOutBatchEntity.setInout(0);
            inOutBatchEntity.setFinishedNumber(0.0);
            inOutBatchEntity.setStatus(InOutBatchStatusEnum.NOT_START.getName());
            inOutBatchEntity.setNumber(productEntity.getNumber());
            inOutBatchEntity.setStoreId(storeId);
            inOutBatchEntity.setProductId(productEntity.getProductId());
            inOutBatchEntity.setBatchId(batchId);
            inOutBatchEntities.add(inOutBatchEntity);
        }

        inoutBatchDao.saveAll(inOutBatchEntities);
        return inOutBatchEntities.stream().map(InOutBatchVO::new).collect(Collectors.toList());
    }
}
