package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.ProductionLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface ProductLineDao extends JpaRepository<ProductionLineEntity, Serializable> {
    
}
