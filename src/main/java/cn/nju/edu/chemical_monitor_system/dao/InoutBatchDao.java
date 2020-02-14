package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.InOutBatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface InoutBatchDao extends JpaRepository<InOutBatchEntity, Serializable> {

    List<InOutBatchEntity> findByStoreId(int storeId);

    List<InOutBatchEntity> findByBatchId(int batchId);

    List<InOutBatchEntity> findByBatchIdAndInout(int batchId, int isIn);

    List<InOutBatchEntity> findByBatchIdAndInoutAndProductIdAndStoreId(int batchId,int ProductId,int StoreId,int isIn);
}