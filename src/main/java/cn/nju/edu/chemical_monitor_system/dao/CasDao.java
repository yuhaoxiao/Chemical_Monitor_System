package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.CasEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface CasDao extends JpaRepository<CasEntity, Serializable> {

    List<CasEntity> findAllByOrderByCasId();
}
