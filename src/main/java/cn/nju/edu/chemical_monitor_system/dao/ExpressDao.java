package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.ExpressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface ExpressDao extends JpaRepository<ExpressEntity, Serializable> {
}
