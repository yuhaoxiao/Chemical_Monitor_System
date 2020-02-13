package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.ExpressProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface ExpressProductDao extends JpaRepository<ExpressProductEntity, Serializable> {
    List<ExpressProductEntity> findByProductId(int productId);
}
