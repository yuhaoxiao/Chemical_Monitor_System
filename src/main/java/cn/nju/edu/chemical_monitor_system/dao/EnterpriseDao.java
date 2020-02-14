package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.EnterpriseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface EnterpriseDao extends JpaRepository<EnterpriseEntity, Serializable> {

    List<EnterpriseEntity> findByName(String name);

    List<EnterpriseEntity> findByNameLike(String name);

}
