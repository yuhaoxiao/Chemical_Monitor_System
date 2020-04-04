package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.StoreProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface StoreProductDao extends JpaRepository<StoreProductEntity, Serializable> {
}
