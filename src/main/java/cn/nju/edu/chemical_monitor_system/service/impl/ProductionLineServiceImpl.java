package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.constant.BatchTypeEnum;
import cn.nju.edu.chemical_monitor_system.dao.BatchDao;
import cn.nju.edu.chemical_monitor_system.dao.EnterpriseDao;
import cn.nju.edu.chemical_monitor_system.dao.ProductionLineDao;
import cn.nju.edu.chemical_monitor_system.entity.BatchEntity;
import cn.nju.edu.chemical_monitor_system.entity.EnterpriseEntity;
import cn.nju.edu.chemical_monitor_system.entity.ProductionLineEntity;
import cn.nju.edu.chemical_monitor_system.service.ProductionLineService;
import cn.nju.edu.chemical_monitor_system.vo.BatchVO;
import cn.nju.edu.chemical_monitor_system.vo.ProductionLineVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductionLineServiceImpl implements ProductionLineService {

    @Autowired
    private ProductionLineDao productionLineDao;

    @Autowired
    private EnterpriseDao enterpriseDao;

    @Autowired
    private BatchDao batchDao;

    @Override
    public ProductionLineVO addProductionLine(int eid) {
        Optional<EnterpriseEntity> enterpriseOpt = enterpriseDao.findById(eid);

        if (!enterpriseOpt.isPresent()) {
            return new ProductionLineVO("企业id不存在");
        }

        ProductionLineEntity productionLineEntity = new ProductionLineEntity();
        productionLineEntity.setEnable(1);
        productionLineEntity.setEnterpriseEntity(enterpriseOpt.get());
        productionLineEntity.setType(BatchTypeEnum.PRODUCE.getCode());
        productionLineDao.saveAndFlush(productionLineEntity);
        return new ProductionLineVO(productionLineEntity);
    }

    @Override
    public ProductionLineVO deleteProductionLine(int plId) {
        Optional<ProductionLineEntity> productionLineOpt = productionLineDao.findById(plId);

        if (!productionLineOpt.isPresent()) {
            return new ProductionLineVO("生产线id不存在");
        }

        ProductionLineEntity productionLineEntity = productionLineOpt.get();
        productionLineEntity.setEnable(0);
        productionLineDao.saveAndFlush(productionLineEntity);
        return new ProductionLineVO(productionLineEntity);
    }

    @Override
    public ProductionLineVO updateProductionLine(ProductionLineVO productionLineVO) {
        Optional<ProductionLineEntity> productionLineOpt = productionLineDao.findById(productionLineVO.getProductionLineId());
        Optional<EnterpriseEntity> enterpriseOpt = enterpriseDao.findById(productionLineVO.getEnterpriseId());

        if (!productionLineOpt.isPresent()) {
            return new ProductionLineVO("生产线id不存在");
        }

        if (!enterpriseOpt.isPresent()) {
            return new ProductionLineVO("企业id不存在");
        }

        ProductionLineEntity productionLineEntity = productionLineOpt.get();
        productionLineEntity.setEnterpriseEntity(enterpriseOpt.get());
        //productionLineEntity.setProductionLineId(productionLineVO.getProductionLineId());
        //productionLineEntity.setEnable(productionLineVO.getEnable());
        productionLineDao.saveAndFlush(productionLineEntity);
        return new ProductionLineVO(productionLineEntity);
    }

    @Override
    public List<BatchVO> getProductionBatch(int plId) {
        Optional<ProductionLineEntity> productionLineOpt = productionLineDao.findById(plId);

        if (!productionLineOpt.isPresent()) {
            return null;
        }

        List<BatchEntity> batchEntities = batchDao.findByProductionLineId(plId);
        return batchEntities.stream().map(BatchVO::new).collect(Collectors.toList());
    }

    @Override
    public List<ProductionLineVO> getAll() {
        return productionLineDao.findAll().stream().map(ProductionLineVO::new).collect(Collectors.toList());
    }

    @Override
    public List<ProductionLineVO> searchByEnterprise(int eid) {
        Optional<EnterpriseEntity> enterpriseOpt = enterpriseDao.findById(eid);

        return enterpriseOpt.map(enterpriseEntity -> productionLineDao.findByEnterpriseEntity(enterpriseEntity).stream().map(ProductionLineVO::new).collect(Collectors.toList())).orElseGet(ArrayList::new);
    }

    @Override
    public ProductionLineVO getProductionLine(int plId) {
        Optional<ProductionLineEntity> productionLineOpt = productionLineDao.findById(plId);

        ProductionLineVO nonExist = new ProductionLineVO("生产线不存在");
        if (!productionLineOpt.isPresent()) {
            return nonExist;
        }
        ProductionLineEntity productionLineEntity = productionLineOpt.get();
        if (productionLineEntity.getEnable() == 0 || productionLineEntity.getType() != BatchTypeEnum.PRODUCE.getCode()) {
            return nonExist;
        }
        return new ProductionLineVO(productionLineEntity);
    }

    @Override
    public ProductionLineVO getByEnterpriseAndType(int eid, int type) {
        Optional<EnterpriseEntity> enterpriseOpt = enterpriseDao.findById(eid);

        if (!enterpriseOpt.isPresent()) {
            return new ProductionLineVO("企业id不存在");
        }

        ProductionLineEntity productionLineEntity = productionLineDao.findFirstByEnterpriseEntityAndType(enterpriseOpt.get(), type);
        if (productionLineEntity == null) {
            return new ProductionLineVO("该企业没有这种生产线");
        }
        return new ProductionLineVO(productionLineEntity, false);
    }
}
