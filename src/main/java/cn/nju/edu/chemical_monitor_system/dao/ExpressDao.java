package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.ExpressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface ExpressDao extends JpaRepository<ExpressEntity, Serializable> {
    ExpressEntity findFirstByExpressId(int expressId);

    List<ExpressEntity> findByInputStoreId(int inputStoreId);

    List<ExpressEntity> findByOutputStoreId(int outputStoreId);

    List<ExpressEntity> findByInputUserId(int uid);

    List<ExpressEntity> findByOutputUserId(int uid);

    List<ExpressEntity> findByInputTimeBetween(Date start, Date end);

    List<ExpressEntity> findByOutputTimeBetweenAndAndOutputStoreId(Date start, Date end,int outputStoreId);

    List<ExpressEntity> findByInputTimeBetweenAndAndInputStoreId(Date start, Date end,int inputStoreId);

}
