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
    public InOutBatchVO inputBatch(int batchId, int storeId) {
        Optional<BatchEntity> batchOpt = batchDao.findById(batchId);

        if (!batchOpt.isPresent()) {
            return new InOutBatchVO("批次id不存在");
        }

        BatchEntity batchEntity = batchOpt.get();
        if (batchEntity.getStatus()==BatchStatusEnum.NOT_START.getCode()) {
            batchEntity.setStatus(BatchStatusEnum.IN_BATCH.getCode());
            batchDao.saveAndFlush(batchEntity);
        }

        List<InOutBatchEntity> inOutBatchs = inoutBatchDao.findByBatchIdAndInout(batchId, 1);

        if (inOutBatchs == null || inOutBatchs.size() == 0) {
            return new InOutBatchVO("该批次没有原材料");
        }

        String port = storeDao.findById(storeId).get().getPort();
        String rfidInfo = rfidUtil.read(port);
        RfidInfoEntity rfidInfoEntity = new RfidInfoEntity(rfidInfo);
        int productId = rfidInfoEntity.getProductId();

        for (InOutBatchEntity inOutBatchEntity : inOutBatchs) {
            if (inOutBatchEntity.getProductId() != productId) {
                continue;
            }
            Double thisNumber = rfidInfoEntity.getNumber();
            Double finishedNumber = inOutBatchEntity.getFinishedNumber() + thisNumber;

            if (finishedNumber > inOutBatchEntity.getNumber()) {
                return new InOutBatchVO("上线了数量为" + rfidInfoEntity.getNumber() + "的产品id为" + productId +
                        "的产品，超出了所需的" + (inOutBatchEntity.getNumber() - finishedNumber) + "的数量");
            } else {
                inOutBatchEntity.setFinishedNumber(finishedNumber);
                ProductEntity productEntity = productDao.findById(productId).get();
                if (finishedNumber < inOutBatchEntity.getNumber()) {
                    inoutBatchDao.saveAndFlush(inOutBatchEntity);
                    return new InOutBatchVO(inOutBatchEntity, productEntity, thisNumber);
                }

                inOutBatchEntity.setStatus(InOutBatchStatusEnum.COMPLETED.getCode());
                inoutBatchDao.saveAndFlush(inOutBatchEntity);
                List<InOutBatchEntity> newInOutBatchs = inoutBatchDao.findByBatchIdAndInout(batchId, 1);

                for (InOutBatchEntity newInoutBatch : newInOutBatchs) {
                    if (newInoutBatch.getStatus()!=InOutBatchStatusEnum.COMPLETED.getCode()) {
                        return new InOutBatchVO(inOutBatchEntity, productEntity, thisNumber);
                    }
                }

                InOutBatchVO inOutBatchVO = new InOutBatchVO(inOutBatchEntity, productEntity, thisNumber);
                inOutBatchVO.setCode(2);
                BatchEntity batch = batchDao.findById(batchId).get();
                batch.setStatus(BatchStatusEnum.IN_PROCESS.getCode());
                batchDao.saveAndFlush(batch);
                return inOutBatchVO;
            }
        }

        return new InOutBatchVO("该批次没有产品id为" + productId + "的原材料");
    }

    @Override
    public InOutBatchVO outputBatch(int batchId, int storeId, int productId, double number) {
        Optional<BatchEntity> batchOpt = batchDao.findById(batchId);
        Optional<ProductEntity> productOpt = productDao.findById(productId);

        if (!batchOpt.isPresent()) {
            return new InOutBatchVO("批次id不存在");
        }

        BatchEntity batchEntity = batchOpt.get();
        if (batchEntity.getStatus()==BatchStatusEnum.IN_PROCESS.getCode()) {
            batchEntity.setStatus(BatchStatusEnum.OUT_BATCH.getCode());
            batchDao.saveAndFlush(batchEntity);
        }

        if (!productOpt.isPresent()) {
            return new InOutBatchVO("产品id不存在");
        }

        List<InOutBatchEntity> inOutBatchs = inoutBatchDao.findByBatchIdAndInout(batchId, 0);

        if (inOutBatchs == null || inOutBatchs.size() == 0) {
            return new InOutBatchVO("该批次没有产品");
        }

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

            Double thisNumber = rfidInfoEntity.getNumber();
            Double finishedNumber = inOutBatchEntity.getFinishedNumber() + thisNumber;

            if (finishedNumber > inOutBatchEntity.getNumber()) {
                return new InOutBatchVO("下线了数量为" + rfidInfoEntity.getNumber() + "的产品id为" + productId +
                        "的产品，超出了所需的" + (inOutBatchEntity.getNumber() - finishedNumber) + "的数量");
            } else {
                inOutBatchEntity.setFinishedNumber(finishedNumber);

                if (finishedNumber < inOutBatchEntity.getNumber()) {
                    inoutBatchDao.saveAndFlush(inOutBatchEntity);
                    rfidUtil.write(rfidInfoEntity.toString(), port);
                    return new InOutBatchVO(inOutBatchEntity, productEntity, thisNumber);
                }

                inOutBatchEntity.setStatus(InOutBatchStatusEnum.COMPLETED.getCode());
                inoutBatchDao.saveAndFlush(inOutBatchEntity);
                List<InOutBatchEntity> newInOutBatchs = inoutBatchDao.findByBatchIdAndInout(batchId, 0);

                for (InOutBatchEntity newInoutBatch : newInOutBatchs) {
                    if (newInoutBatch.getStatus()!=InOutBatchStatusEnum.COMPLETED.getCode()) {
                        return new InOutBatchVO(inOutBatchEntity, productEntity, thisNumber);
                    }
                }

                InOutBatchVO inOutBatchVO = new InOutBatchVO(inOutBatchEntity, productEntity, thisNumber);
                inOutBatchVO.setCode(2);
                BatchEntity batch = batchDao.findById(batchId).get();
                batch.setStatus(BatchStatusEnum.COMPLETE.getCode());
                batchDao.saveAndFlush(batch);
                rfidUtil.write(rfidInfoEntity.toString(), port);
                return inOutBatchVO;
            }
        }

        return new InOutBatchVO("该批次没有产品id为" + productId + "的产品");
    }

}
