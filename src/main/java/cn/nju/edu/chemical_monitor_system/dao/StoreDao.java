package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.StoreEntity;
import cn.nju.edu.chemical_monitor_system.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface StoreDao extends JpaRepository<StoreEntity, Serializable> {

    StoreEntity findByStoreId(int storeId);
}
