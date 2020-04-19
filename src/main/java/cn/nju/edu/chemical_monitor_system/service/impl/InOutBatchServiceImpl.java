package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.constant.BatchStatusEnum;
import cn.nju.edu.chemical_monitor_system.constant.InOutBatchStatusEnum;
import cn.nju.edu.chemical_monitor_system.dao.*;
import cn.nju.edu.chemical_monitor_system.entity.*;
import cn.nju.edu.chemical_monitor_system.service.InOutBatchService;
import cn.nju.edu.chemical_monitor_system.utils.rfid.RfidUtil;
import cn.nju.edu.chemical_monitor_system.vo.InOutBatchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    private StoreProductDao storeProductDao;

    @Autowired
    private RfidUtil rfidUtil;

    @Override
    public InOutBatchVO inputBatch(int batchId, int storeId) {
        Optional<BatchEntity> batchOpt = batchDao.findById(batchId);
        Optional<StoreEntity> storeOpt = storeDao.findById(storeId);

        if (!batchOpt.isPresent()) {
            return new InOutBatchVO("批次id不存在");
        }

        if (!storeOpt.isPresent()) {
            return new InOutBatchVO("仓库id不存在");
        }

        BatchEntity batchEntity = batchOpt.get();
        if (batchEntity.getStatus() == BatchStatusEnum.NOT_START.getCode()) {
            batchEntity.setStatus(BatchStatusEnum.IN_BATCH.getCode());
            batchDao.saveAndFlush(batchEntity);
        }

        List<InOutBatchEntity> inOutBatchs = inoutBatchDao.findByBatchIdAndInout(batchId, 1);

        if (inOutBatchs == null || inOutBatchs.size() == 0) {
            return new InOutBatchVO("该批次没有原材料");
        }

        StoreEntity storeEntity = storeOpt.get();
        String port = storeEntity.getPort();
        String rfidInfo = rfidUtil.read(batchId + "");
//        String rfidInfo = rfidUtil.read(port);
        if(rfidInfo.equals("-1")){
            return new InOutBatchVO("RFID未读取到标签内容");
        }
        RfidInfoEntity rfidInfoEntity = new RfidInfoEntity(rfidInfo);
        int productId = rfidInfoEntity.getProductId();

        int finishCount = 0;
        InOutBatchEntity iob = null;

        for (InOutBatchEntity inOutBatchEntity : inOutBatchs) {
            if (inOutBatchEntity.getProductId() == productId) {
                iob = inOutBatchEntity;
                Double thisNumber = rfidInfoEntity.getNumber();
                Double finishedNumber = inOutBatchEntity.getFinishedNumber() + thisNumber;

                if (finishedNumber > inOutBatchEntity.getNumber()) {
                    return new InOutBatchVO("上线了数量为" + rfidInfoEntity.getNumber() + "的产品id为" + productId +
                            "的产品，超出了所需的" + (inOutBatchEntity.getNumber() - finishedNumber) + "的数量");
                } else {
                    if (iob.getStatus() == InOutBatchStatusEnum.NOT_START.getCode()) {
                        iob.setStatus(InOutBatchStatusEnum.ING.getCode());
                    }

                    iob.setFinishedNumber(finishedNumber);
                    iob.setFinishedNumber(finishedNumber);

                    if (finishedNumber.equals(iob.getNumber())) {
                        iob.setStatus(InOutBatchStatusEnum.COMPLETED.getCode());
                    }
                }
            }

            if (iob.getStatus() == InOutBatchStatusEnum.COMPLETED.getCode()) {
                finishCount++;
            }
        }

        if (iob == null) {
            return new InOutBatchVO("该批次没有产品id为" + productId + "的原材料");
        }

        inoutBatchDao.saveAndFlush(iob);
        InOutBatchVO inOutBatchVO = new InOutBatchVO(iob, productDao.findByProductId(iob.getProductId()), rfidInfoEntity.getNumber());
        if (finishCount == inOutBatchs.size()) {
            inOutBatchVO.setCode(2);
            batchEntity.setStatus(BatchStatusEnum.IN_PROCESS.getCode());
            batchDao.saveAndFlush(batchEntity);
        }

        List<StoreProductEntity> storeProductEntities = storeEntity.getStoreProductEntities();
        for (StoreProductEntity storeProductEntity : storeProductEntities) {
            if (storeProductEntity.getProductEntity().getProductId() == productId) {
                double number = storeProductEntity.getNumber();
                number -= rfidInfoEntity.getNumber();
                if (number == 0) {
                    storeProductDao.delete(storeProductEntity);
                } else if (number < 0) {
                    return new InOutBatchVO("系统出错，数据不一致");
                } else {
                    storeProductEntity.setNumber(number);
                    storeProductDao.saveAndFlush(storeProductEntity);
                }
            }
        }

        return inOutBatchVO;
    }

    @Override
    public InOutBatchVO outputBatch(int batchId, int storeId, int productId, double number) {
        if (number < 0) {
            return new InOutBatchVO("数量不能为负数");
        }

        Optional<BatchEntity> batchOpt = batchDao.findById(batchId);
        Optional<ProductEntity> productOpt = productDao.findById(productId);
        Optional<StoreEntity> storeOpt = storeDao.findById(storeId);

        if (!batchOpt.isPresent()) {
            return new InOutBatchVO("批次id不存在");
        }

        BatchEntity batchEntity = batchOpt.get();
        if (batchEntity.getStatus() == BatchStatusEnum.IN_PROCESS.getCode()) {
            batchEntity.setStatus(BatchStatusEnum.OUT_BATCH.getCode());
            batchDao.saveAndFlush(batchEntity);
        }

        if (!productOpt.isPresent()) {
            return new InOutBatchVO("产品id不存在");
        }

        if (!storeOpt.isPresent()) {
            return new InOutBatchVO("仓库id不存在");
        }

        List<InOutBatchEntity> inOutBatchs = inoutBatchDao.findByBatchIdAndInout(batchId, 0);

        if (inOutBatchs == null || inOutBatchs.size() == 0) {
            return new InOutBatchVO("该批次没有产品");
        }

        StoreEntity storeEntity = storeOpt.get();
        String port = storeEntity.getPort();

        ProductEntity productEntity = productOpt.get();
        RfidInfoEntity rfidInfoEntity = new RfidInfoEntity();
        rfidInfoEntity.setBatchId(batchId);
        rfidInfoEntity.setProductId(productId);
        rfidInfoEntity.setCasId(productEntity.getCasEntity().getCasId());
        rfidInfoEntity.setExpressId(0);
        rfidInfoEntity.setNumber(number);

        InOutBatchEntity iob = null;
        int finishCount = 0;

        for (InOutBatchEntity inOutBatchEntity : inOutBatchs) {
            if (inOutBatchEntity.getProductId() == productId) {
                iob = inOutBatchEntity;
                Double thisNumber = rfidInfoEntity.getNumber();
                Double finishedNumber = inOutBatchEntity.getFinishedNumber() + thisNumber;

                if (iob.getStatus() == InOutBatchStatusEnum.NOT_START.getCode()) {
                    iob.setStatus(InOutBatchStatusEnum.ING.getCode());
                }

                if (finishedNumber > inOutBatchEntity.getNumber()) {
                    return new InOutBatchVO("下线了数量为" + rfidInfoEntity.getNumber() + "的产品id为" + productId +
                            "的产品，超出了所需的" + (inOutBatchEntity.getNumber() - finishedNumber) + "的数量");
                } else {
                    iob.setFinishedNumber(finishedNumber);
//                    String writeRfid = rfidUtil.write(rfidInfoEntity.toString(), port);
//                    if (writeRfid.equals("-1")) {
//                        return new InOutBatchVO("写入失败");
//                    }
                    if (finishedNumber.equals(inOutBatchEntity.getNumber())) {
                        iob.setStatus(InOutBatchStatusEnum.COMPLETED.getCode());
                    }
                }
            }

            if (inOutBatchEntity.getStatus() == InOutBatchStatusEnum.COMPLETED.getCode()) {
                finishCount++;
            }
        }

        if (iob == null) {
            return new InOutBatchVO("该批次没有产品id为" + productId + "的产品");
        }

        inoutBatchDao.saveAndFlush(iob);
        InOutBatchVO inOutBatchVO = new InOutBatchVO(iob, productDao.findByProductId(iob.getProductId()), rfidInfoEntity.getNumber());
        if (finishCount == inOutBatchs.size()) {
            inOutBatchVO.setCode(2);
            batchEntity.setStatus(BatchStatusEnum.COMPLETE.getCode());
            batchDao.saveAndFlush(batchEntity);
        }

        List<StoreProductEntity> storeProductEntities = storeEntity.getStoreProductEntities();
        boolean exist = false;
        for (StoreProductEntity storeProductEntity : storeProductEntities) {
            if (storeProductEntity.getProductEntity().getProductId() == productId) {
                exist = true;
                double previousNumber = storeProductEntity.getNumber();
                previousNumber += number;
                storeProductEntity.setNumber(previousNumber);
                storeProductDao.saveAndFlush(storeProductEntity);
            }
        }

        if (!exist) {
            StoreProductEntity storeProductEntity = new StoreProductEntity();
            storeProductEntity.setProductEntity(productEntity);
            storeProductEntity.setStoreEntity(storeEntity);
            storeProductEntity.setNumber(number);
            storeProductDao.saveAndFlush(storeProductEntity);
        }

        return inOutBatchVO;
    }

}
