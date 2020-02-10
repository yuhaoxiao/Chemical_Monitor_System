package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.constant.InOutBatchStatusEnum;
import cn.nju.edu.chemical_monitor_system.dao.BatchDao;
import cn.nju.edu.chemical_monitor_system.dao.InoutBatchDao;
import cn.nju.edu.chemical_monitor_system.dao.ProductDao;
import cn.nju.edu.chemical_monitor_system.dao.StoreDao;
import cn.nju.edu.chemical_monitor_system.entity.*;
import cn.nju.edu.chemical_monitor_system.service.InOutBatchService;
import cn.nju.edu.chemical_monitor_system.utils.rfid_util.RfidUtil;
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
    public InOutBatchVO InputBatch(int batchId) {
        Optional<BatchEntity> batchOpt = batchDao.findById(batchId);

        if (!batchOpt.isPresent()) {
            return new InOutBatchVO("批次id不存在");
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
                return inOutBatchVO;
            }
        }

        return new InOutBatchVO("该批次没有产品id为" + productId + "的原材料");
    }

    @Override
    public InOutBatchVO OutputBatch(int batchId, int productId, double number) {
        Optional<BatchEntity> batchOpt = batchDao.findById(batchId);
        Optional<ProductEntity> productOpt = productDao.findById(productId);

        if (!batchOpt.isPresent()) {
            return new InOutBatchVO("批次id不存在");
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
                rfidUtil.write(rfidInfoEntity.toString(), port);
                return inOutBatchVO;
            }
        }

        return new InOutBatchVO("该批次没有产品id为" + productId + "的产品");
    }
}
