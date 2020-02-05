package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.CasEntity;
import cn.nju.edu.chemical_monitor_system.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface ProductDao extends JpaRepository<ProductEntity, Serializable> {
    List<ProductEntity> findByBatchIdAndCasEntity(int batchId, CasEntity cas);

    List<ProductEntity> findByBatchId(int batchId);

    ProductEntity findByProductId(int productId);
}
