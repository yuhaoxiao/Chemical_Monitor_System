package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.EnterpriseEntity;
import cn.nju.edu.chemical_monitor_system.entity.ProductionLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface ProductionLineDao extends JpaRepository<ProductionLineEntity, Serializable> {
    List<ProductionLineEntity> findByEnterpriseEntity(EnterpriseEntity enterpriseEntity);

    ProductionLineEntity findFirstByEnterpriseEntityAndType(EnterpriseEntity enterpriseEntity, int type);
}
