package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface ProductDao extends JpaRepository<ProductEntity, Serializable> {
    
}
