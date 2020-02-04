package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.constant.ExpressStatusEnum;
import cn.nju.edu.chemical_monitor_system.dao.ExpressDao;
import cn.nju.edu.chemical_monitor_system.dao.ProductDao;
import cn.nju.edu.chemical_monitor_system.dao.StoreDao;
import cn.nju.edu.chemical_monitor_system.dao.UserDao;
import cn.nju.edu.chemical_monitor_system.entity.ExpressEntity;
import cn.nju.edu.chemical_monitor_system.entity.ExpressProductEntity;
import cn.nju.edu.chemical_monitor_system.entity.ProductEntity;
import cn.nju.edu.chemical_monitor_system.entity.StoreEntity;
import cn.nju.edu.chemical_monitor_system.service.ExpressService;
import cn.nju.edu.chemical_monitor_system.utils.safe_util.SafeUtil;
import cn.nju.edu.chemical_monitor_system.vo.ExpressProductVO;
import cn.nju.edu.chemical_monitor_system.vo.ExpressVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpressServiceImpl implements ExpressService {

    @Autowired
    private ExpressDao expressDao;

    @Autowired
    private StoreDao storeDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SafeUtil safeUtil;

    @Override
    public ExpressVO createExpress(int inputStoreId, int outputStoreId, Map<Integer, Double> productNumberMap) {
        ExpressEntity expressEntity = new ExpressEntity();
        Optional<StoreEntity> inputStoreOpt = storeDao.findById(inputStoreId);
        Optional<StoreEntity> outputStoreOpt = storeDao.findById(outputStoreId);

        if (!inputStoreOpt.isPresent()) {
            return new ExpressVO("入库仓库id不存在");
        }

        if (!outputStoreOpt.isPresent()) {
            return new ExpressVO("出库仓库id不存在");
        }

        expressEntity.setInputStore(inputStoreOpt.get());
        expressEntity.setOutputStore(outputStoreOpt.get());
        expressEntity.setStatus(ExpressStatusEnum.NOT_START.getCode());

        List<ExpressProductEntity> expressProductEntities = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : productNumberMap.entrySet()) {
            ExpressProductEntity ep = new ExpressProductEntity();
            Optional<ProductEntity> productOpt = productDao.findById(entry.getKey());

            if (!productOpt.isPresent()) {
                continue;
            }

            ep.setProductEntity(productOpt.get());
            ep.setExpressEntity(expressEntity);
            ep.setNumber(entry.getValue());
            expressProductEntities.add(ep);
        }

        expressEntity.setExpressProductEntities(expressProductEntities);
        expressDao.save(expressEntity);
        return new ExpressVO(expressEntity);
    }

    @Override
    public ExpressVO inputExpress(int expressId, int userId) {
        Optional<ExpressEntity> expressOpt = expressDao.findById(expressId);

        if (!expressOpt.isPresent()) {
            return new ExpressVO("物流id不存在");
        }

        ExpressEntity expressEntity = expressOpt.get();
        expressEntity.setInputUser(userDao.findById(userId).get());
        expressEntity.setInputTime(new Timestamp(System.currentTimeMillis()));
        expressEntity.setStatus(ExpressStatusEnum.IN_INVENTORY.getCode());
        expressDao.save(expressEntity);
        return new ExpressVO(expressEntity);
    }

    @Override
    public ExpressVO outputExpress(int expressId, int userId) {
        Optional<ExpressEntity> expressOpt = expressDao.findById(expressId);

        if (!expressOpt.isPresent()) {
            return new ExpressVO("物流id不存在");
        }

        ExpressEntity expressEntity = expressOpt.get();
        expressEntity.setOutputUser(userDao.findById(userId).get());
        expressEntity.setOutputTime(new Timestamp(System.currentTimeMillis()));
        expressEntity.setStatus(ExpressStatusEnum.OUT_INVENTORY.getCode());
        expressDao.save(expressEntity);
        return new ExpressVO(expressEntity);
    }

    @Override
    public ExpressVO getExpress(int expressId) {
        Optional<ExpressEntity> expressOpt = expressDao.findById(expressId);

        if (!expressOpt.isPresent()) {
            return new ExpressVO("物流id不存在");
        }

        return new ExpressVO(expressOpt.get());
    }

    @Override
    public List<ExpressProductVO> getProductExpress(int productId) {
        Optional<ProductEntity> productOpt = productDao.findById(productId);

        if (!productOpt.isPresent()) {
            return new ArrayList<>();
        }

        return productOpt.get().getExpressProductEntities().stream()
                .map(ExpressProductVO::new)
                .collect(Collectors.toList());
    }

}
