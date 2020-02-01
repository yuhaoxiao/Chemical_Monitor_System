package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.dao.StoreDao;
import cn.nju.edu.chemical_monitor_system.entity.ExpressEntity;
import cn.nju.edu.chemical_monitor_system.entity.ExpressProductEntity;
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

    @Override
    public List<Integer> getAllStoreId() {
        List<StoreEntity> stores = storeDao.findAll();
        return stores.stream().map(StoreEntity::getStoreId).collect(Collectors.toList());
    }

    @Override
    public StoreVO getStore(int sid) {
        Optional<StoreEntity> storeOpt = storeDao.findById(sid);

        if (!storeOpt.isPresent()) {
            return new StoreVO("仓库id不存在");
        }

        return new StoreVO(storeOpt.get());
    }

    @Override
    public Map<Integer, Double> getStoreProduct(int sid) {
        StoreEntity store = storeDao.findById(sid).get();
        Map<Integer, Double> productNumber = new HashMap<>();

        for (ExpressEntity in : store.getInExpressEntities()) {
            for (ExpressProductEntity inep : in.getExpressProductEntities()) {
                int productId = inep.getProductEntity().getProductId();
                if (productNumber.containsKey(productId)) {
                    Double number = productNumber.get(productId);
                    productNumber.put(inep.getProductEntity().getProductId(), number + inep.getNumber());
                } else {
                    productNumber.put(productId, inep.getNumber());
                }
            }
        }

        for (ExpressEntity out : store.getOutExpressEntities()) {
            for (ExpressProductEntity outep : out.getExpressProductEntities()) {
                int productId = outep.getProductEntity().getProductId();
                if (productNumber.containsKey(productId)) {
                    Double number = productNumber.get(productId);
                    productNumber.put(outep.getProductEntity().getProductId(), number - outep.getNumber());
                } else {
                    productNumber.put(productId, -outep.getNumber());
                }
            }
        }

        return productNumber;
    }


}
