package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface UserDao extends JpaRepository<UserEntity, Serializable> {
    UserEntity findFirstByUserId(int id);
}
