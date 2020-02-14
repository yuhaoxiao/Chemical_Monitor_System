package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.constant.InOutBatchStatusEnum;
import cn.nju.edu.chemical_monitor_system.dao.ExpressDao;
import cn.nju.edu.chemical_monitor_system.dao.InoutBatchDao;
import cn.nju.edu.chemical_monitor_system.dao.StoreDao;
import cn.nju.edu.chemical_monitor_system.entity.ExpressEntity;
import cn.nju.edu.chemical_monitor_system.entity.ExpressProductEntity;
import cn.nju.edu.chemical_monitor_system.entity.InOutBatchEntity;
import cn.nju.edu.chemical_monitor_system.entity.StoreEntity;
import cn.nju.edu.chemical_monitor_system.service.StoreService;
import cn.nju.edu.chemical_monitor_system.vo.StoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreDao storeDao;

    @Autowired
    private InoutBatchDao inoutBatchDao;

    @Autowired
    private ExpressDao expressDao;

    @Override
    public List<Integer> getAllStoreId() {
        List<StoreEntity> stores = storeDao.findAll();
        return stores.stream().map(StoreEntity::getStoreId).collect(Collectors.toList());
    }

    @Override
    public StoreVO getStoreById(int sid) {
        Optional<StoreEntity> storeOpt = storeDao.findById(sid);

        if (!storeOpt.isPresent()) {
            return new StoreVO("仓库id不存在");
        }

        return new StoreVO(storeOpt.get());
    }

    @Override
    public Map<Integer, Double> getStoreProduct(int sid) {
        Optional<StoreEntity> storeOpt = storeDao.findById(sid);

        if (!storeOpt.isPresent()) {
            return null;
        }

        Map<Integer, Double> productNumber = new HashMap<>();

        for (ExpressEntity in : expressDao.findByInputStoreId(sid)) {
            for (ExpressProductEntity inep : in.getExpressProductEntities()) {
                int productId = inep.getProductId();
                if (productNumber.containsKey(productId)) {
                    Double number = productNumber.get(productId);
                    productNumber.put(productId, number + inep.getNumber());
                } else {
                    productNumber.put(productId, inep.getNumber());
                }
            }
        }

        for (ExpressEntity out : expressDao.findByOutputStoreId(sid)) {
            for (ExpressProductEntity outep : out.getExpressProductEntities()) {
                int productId = outep.getProductId();
                if (productNumber.containsKey(productId)) {
                    Double number = productNumber.get(productId);
                    productNumber.put(productId, number - outep.getNumber());
                } else {
                    productNumber.put(productId, -outep.getNumber());
                }
            }
        }

        for (InOutBatchEntity inout : inoutBatchDao.findByStoreId(sid)) {
            if (inout.getStatus() != InOutBatchStatusEnum.COMPLETED.getName()) {
                continue;
            }

            int productId = inout.getProductId();
            if (productNumber.containsKey(productId)) {
                Double number = productNumber.get(productId);
                productNumber.put(productId, number + inout.getNumber() * (inout.getInout() == 1 ? 1 : -1));
            } else {
                productNumber.put(productId, inout.getNumber() * (inout.getInout() == 1 ? 1 : -1));
            }
        }

        return productNumber;
    }

    @Override
    public StoreVO addStore(int eid, String name) {
        List<StoreEntity> existed = storeDao.findByEnterpriseIdAndName(eid, name);

        if (existed != null && existed.size() != 0) {
            return new StoreVO("该企业下已有同名仓库");
        }

        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setEnable(1);
        storeEntity.setEnterpriseId(eid);
        storeEntity.setName(name);
        storeDao.saveAndFlush(storeEntity);
        return new StoreVO(storeEntity);
    }

    @Override
    public StoreVO deleteStore(int sid) {
        Optional<StoreEntity> storeOpt = storeDao.findById(sid);

        if (!storeOpt.isPresent()) {
            return new StoreVO("仓库id不存在");
        }

        StoreEntity storeEntity = storeOpt.get();
        storeEntity.setEnable(0);
        storeDao.saveAndFlush(storeEntity);
        return new StoreVO(storeEntity);
    }

    @Override
    public StoreVO updateStore(StoreVO storeVO) {
        Optional<StoreEntity> storeOpt = storeDao.findById(storeVO.getStoreId());

        if (!storeOpt.isPresent()) {
            return new StoreVO("仓库id不存在");
        }

        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setName(storeVO.getName());
        storeEntity.setEnterpriseId(storeVO.getEnterpriseId());
        storeEntity.setEnable(storeVO.getEnable());
        storeDao.saveAndFlush(storeEntity);
        return new StoreVO(storeEntity);
    }

    @Override
    public List<StoreVO> searchStore(String s) {
        List<StoreEntity> storeEntities = storeDao.findByNameLike("%" + s + "%");

        try {
            int sid = Integer.parseInt(s);
            Optional<StoreEntity> storeOpt = storeDao.findById(sid);

            if (storeOpt.isPresent()) {
                storeEntities.add(storeOpt.get());
            }
        } catch (Exception e) {

        }

        return storeEntities.stream().map(StoreVO::new).collect(Collectors.toList());
    }


}
