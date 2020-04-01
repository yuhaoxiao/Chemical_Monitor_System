package cn.nju.edu.chemical_monitor_system.vo;

import cn.nju.edu.chemical_monitor_system.entity.UserEntity;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserVO {

    private int userId;
    private String password;
    private String type;  // 想办法改成int类型  0：'操作员', 1：'管理员', 2：'监控员'
    private String name;
    private int enable;
    private Timestamp lastOperationTime;

    private int code;
    private String message;

    public UserVO(UserEntity u) {
        if (u == null) {
            this.code = 0;
            return;
        }

        this.userId = u.getUserId();
        this.password = u.getPassword();
        this.type = u.getType();
        this.enable = u.getEnable();
        this.name = u.getName();
        this.code = 1;
    }

    public UserVO(String message) {
        this.code = 0;
        this.message = message;
    }
}
