package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.EnterpriseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface EnterpriseDao extends JpaRepository<EnterpriseEntity, Serializable> {
    
}
