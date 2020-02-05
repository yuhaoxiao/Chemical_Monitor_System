package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.EncryptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.Optional;

public interface EncryptionDao extends JpaRepository<EncryptionEntity, Serializable> {
    Optional<EncryptionEntity> findByInputStoreIdAndOutputStoreId(int inputStoreId, int outputStoreId);
}
