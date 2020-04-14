package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.BatchEntity;
import cn.nju.edu.chemical_monitor_system.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface BatchDao extends JpaRepository<BatchEntity, Serializable>  {

    List<BatchEntity> findByProductionLineId(int plId);

    List<BatchEntity> findByUserEntity(UserEntity user);

    BatchEntity findFirstByBatchId(int BatchId);

    List<BatchEntity> findByTimeBetween(Date start, Date end);

    List<BatchEntity> findByTimeBetweenAndProductionLineId(Date start, Date end,int productionLineId);

    List<BatchEntity> findByTimeBetweenAndProductionLineIdIn(Date start, Date end,List<Integer> ids);
}
