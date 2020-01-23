package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.BatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface BatchDao extends JpaRepository<BatchEntity, Serializable> {

}
