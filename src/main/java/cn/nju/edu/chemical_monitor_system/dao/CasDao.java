package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.CasEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface CasDao extends JpaRepository<CasEntity, Serializable> {

    CasEntity findFirstByCasId(int cid);
    
}
