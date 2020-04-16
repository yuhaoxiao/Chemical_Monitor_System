package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.constant.BatchTypeEnum;
import cn.nju.edu.chemical_monitor_system.dao.EnterpriseDao;
import cn.nju.edu.chemical_monitor_system.dao.ProductionLineDao;
import cn.nju.edu.chemical_monitor_system.entity.EnterpriseEntity;
import cn.nju.edu.chemical_monitor_system.entity.ProductionLineEntity;
import cn.nju.edu.chemical_monitor_system.service.EnterpriseService;
import cn.nju.edu.chemical_monitor_system.vo.EnterpriseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnterpriseServiceImpl implements EnterpriseService {

    @Autowired
    private EnterpriseDao enterpriseDao;

    @Autowired
    private ProductionLineDao productionLineDao;

    @Override
    public EnterpriseVO addEnterprise(String name) {
        List<EnterpriseEntity> existed = enterpriseDao.findByName(name);

        if (existed != null && existed.size() != 0) {
            return new EnterpriseVO("已有同名企业");
        }

        EnterpriseEntity enterpriseEntity = new EnterpriseEntity();
        enterpriseEntity.setName(name);
        enterpriseEntity.setEnable(1);
        enterpriseDao.saveAndFlush(enterpriseEntity);

        ProductionLineEntity plInPark = new ProductionLineEntity();
        plInPark.setEnable(1);
        plInPark.setType(BatchTypeEnum.IN_PARK.getCode());
        plInPark.setEnterpriseEntity(enterpriseEntity);
        productionLineDao.saveAndFlush(plInPark);

        ProductionLineEntity plOutPark = new ProductionLineEntity();
        plOutPark.setEnable(1);
        plOutPark.setType(BatchTypeEnum.OUT_PARK.getCode());
        plOutPark.setEnterpriseEntity(enterpriseEntity);
        productionLineDao.saveAndFlush(plOutPark);

        ProductionLineEntity plDestory = new ProductionLineEntity();
        plDestory.setEnable(1);
        plDestory.setType(BatchTypeEnum.DESTROY.getCode());
        plDestory.setEnterpriseEntity(enterpriseEntity);
        productionLineDao.saveAndFlush(plDestory);

        return new EnterpriseVO(enterpriseEntity);
    }

    @Override
    public EnterpriseVO deleteEnterprise(int eid) {
        Optional<EnterpriseEntity> enterpriseOpt = enterpriseDao.findById(eid);

        if (!enterpriseOpt.isPresent()) {
            return new EnterpriseVO("企业id不存在");
        }

        EnterpriseEntity enterpriseEntity = enterpriseOpt.get();
        enterpriseEntity.setEnable(0);
        enterpriseDao.saveAndFlush(enterpriseEntity);
        return new EnterpriseVO(enterpriseEntity);
    }

    @Override
    public EnterpriseVO updateEnterprise(EnterpriseVO enterpriseVO) {
        Optional<EnterpriseEntity> enterpriseOpt = enterpriseDao.findById(enterpriseVO.getEnterpriseId());

        if (!enterpriseOpt.isPresent()) {
            return new EnterpriseVO("企业id不存在");
        }

        EnterpriseEntity enterpriseEntity = enterpriseOpt.get();
        enterpriseEntity.setName(enterpriseVO.getName());
        //enterpriseEntity.setEnterpriseId(enterpriseVO.getEnterpriseId());
        //enterpriseEntity.setEnable(enterpriseVO.getEnterpriseId());
        enterpriseDao.saveAndFlush(enterpriseEntity);
        return new EnterpriseVO(enterpriseEntity);
    }

    @Override
    public List<EnterpriseVO> searchEnterprise(String s) {
        List<EnterpriseEntity> enterpriseEntities = enterpriseDao.findByNameLike("%" + s + "%");

        try {
            int eid = Integer.parseInt(s);
            Optional<EnterpriseEntity> enterpriseOpt = enterpriseDao.findById(eid);

            if (enterpriseOpt.isPresent()) {
                enterpriseEntities.clear();
                enterpriseEntities.add(enterpriseOpt.get());
            }
        } catch (Exception e) {
        }

        return enterpriseEntities.stream().map(EnterpriseVO::new).collect(Collectors.toList());
    }

    @Override
    public List<EnterpriseVO> getAll() {
        return enterpriseDao.findAll().stream().map(EnterpriseVO::new).collect(Collectors.toList());
    }
}
