package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface RoleDao extends JpaRepository<RoleEntity, Serializable> {
    RoleEntity findByRoleId(int id);
}
