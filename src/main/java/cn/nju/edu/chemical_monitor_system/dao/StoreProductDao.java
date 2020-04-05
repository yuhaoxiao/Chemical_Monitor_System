package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.StoreEntity;
import cn.nju.edu.chemical_monitor_system.entity.StoreProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface StoreProductDao extends JpaRepository<StoreProductEntity, Serializable> {
    List<StoreProductEntity> findByStoreEntityAndNumberGreaterThan(StoreEntity storeEntity, double x);
}
