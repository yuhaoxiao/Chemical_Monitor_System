package cn.nju.edu.chemical_monitor_system.vo;

import cn.nju.edu.chemical_monitor_system.entity.EnterpriseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EnterpriseVO {

    private int enterpriseId;
    private String name;
    private int enable;
    private int code;
    private String message;

    public EnterpriseVO(EnterpriseEntity e) {
        if (e == null) {
            this.code = 0;
            return;
        }

        this.enterpriseId = e.getEnterpriseId();
        this.name = e.getName();
        this.enable = e.getEnable();
        this.code = 1;
    }

    public EnterpriseVO(String message) {
        this.code = 0;
        this.message = message;
    }

}
