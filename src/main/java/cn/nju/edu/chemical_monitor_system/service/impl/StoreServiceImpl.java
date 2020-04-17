package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.constant.ExpressStatusEnum;
import cn.nju.edu.chemical_monitor_system.constant.InOutBatchStatusEnum;
import cn.nju.edu.chemical_monitor_system.dao.*;
import cn.nju.edu.chemical_monitor_system.entity.*;
import cn.nju.edu.chemical_monitor_system.service.StoreService;
import cn.nju.edu.chemical_monitor_system.vo.ProductVO;
import cn.nju.edu.chemical_monitor_system.vo.StoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreDao storeDao;

    @Autowired
    private InoutBatchDao inoutBatchDao;

    @Autowired
    private ExpressDao expressDao;

    @Autowired
    private StoreProductDao storeProductDao;

    @Autowired
    private CasDao casDao;

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

        StoreEntity storeEntity = storeOpt.get();
        storeEntity.setName(storeVO.getName());
        storeEntity.setEnterpriseId(storeVO.getEnterpriseId());
        //storeEntity.setEnable(storeVO.getEnable());
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
                storeEntities.clear();
                storeEntities.add(storeOpt.get());
            }
        } catch (Exception e) {

        }

        return storeEntities.stream().map(StoreVO::new).collect(Collectors.toList());
    }

    @Override
    public List<StoreVO> getAll() {
        return storeDao.findAll().stream().map(StoreVO::new).collect(Collectors.toList());
    }

    @Override
    public List<StoreVO> searchByEnterprise(int eid) {
        return storeDao.findByEnterpriseId(eid).stream().map(StoreVO::new).collect(Collectors.toList());
    }

    @Override
    public List<ProductVO> getAllStoreProducts(int storeId) {
        List<StoreProductEntity> storeProductEntities = storeDao.findFirstByStoreId(storeId).getStoreProductEntities();
        List<ExpressEntity> expressEntities = expressDao.findByOutputStoreId(storeId).stream()
                .filter(e -> e.getStatus() == ExpressStatusEnum.NOT_START.getCode() || e.getStatus() == ExpressStatusEnum.OUT_INVENTORY_ING.getCode()).collect(Collectors.toList());
        for (ExpressEntity expressEntity : expressEntities) {
            List<ExpressProductEntity> expressProductEntities = expressEntity.getExpressProductEntities();
            for (ExpressProductEntity e1 : expressProductEntities) {
                double number = e1.getNumber() - e1.getOutputNumber();
                for (StoreProductEntity storeProductEntity : storeProductEntities) {
                    if (storeProductEntity.getProductEntity().getProductId() == e1.getProductId()) {
                        storeProductEntity.setNumber(storeProductEntity.getNumber() - number);
                    }
                }
            }
        }

        List<InOutBatchEntity> inOutBatchEntities = inoutBatchDao.findByStoreId(storeId).stream()
                .filter(e -> e.getInout() == 0 && (e.getStatus() == InOutBatchStatusEnum.NOT_START.getCode() ||
                        e.getStatus() == InOutBatchStatusEnum.ING.getCode())).collect(Collectors.toList());
        for (InOutBatchEntity inOutBatchEntity : inOutBatchEntities) {
            double notFinished = inOutBatchEntity.getNumber() - inOutBatchEntity.getFinishedNumber();
            for (StoreProductEntity storeProductEntity : storeProductEntities) {
                if (storeProductEntity.getProductEntity().getProductId() == inOutBatchEntity.getProductId()) {
                    storeProductEntity.setNumber(storeProductEntity.getNumber() - notFinished);
                }
            }
        }
        return storeProductEntities.stream().map(ProductVO::new).collect(Collectors.toList());
    }

    @Override
    public List<StoreVO> searchStoresByCAS(int casId) {
        Optional<CasEntity> casOpt = casDao.findById(casId);
        if (!casOpt.isPresent())
            return new ArrayList<>();
        Set<StoreEntity> storeEntitySet = new HashSet<>();
        for (ProductEntity productEntity : casOpt.get().getProductEntities()) {
            for (StoreProductEntity storeProductEntity : productEntity.getStoreProductEntities()) {
                if (storeProductEntity.getNumber() > 0)
                    storeEntitySet.add(storeProductEntity.getStoreEntity());
            }
        }
        return storeEntitySet.stream().map(StoreVO::new).collect(Collectors.toList());
    }

    @Override
    public List<ProductVO> searchProductsByStoreAndCAS(int storeId, int casId) {
        Optional<StoreEntity> storeOpt = storeDao.findById(storeId);
        List<ProductVO> list = new ArrayList<>();
        if (!storeOpt.isPresent())
            return list;
        for (StoreProductEntity storeProductEntity : storeProductDao.findByStoreEntityAndNumberGreaterThan(storeOpt.get(), 0)) {
            if (storeProductEntity.getProductEntity().getCasEntity().getCasId() == casId)
                list.add(new ProductVO(storeProductEntity));
        }
        return list;
    }


}
