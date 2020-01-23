package cn.nju.edu.chemical_monitor_system.vo;

import cn.nju.edu.chemical_monitor_system.entity.EnterpriseEntity;
import lombok.Data;

@Data
public class EnterpriseVO {

    private int enterpriseId;
    private String name;
    private int code;

    public EnterpriseVO(EnterpriseEntity e) {
        if (e == null) {
            this.code = 0;
            return;
        }

        this.enterpriseId = e.getEnterpriseId();
        this.name = e.getName();
        this.code = 1;
    }

    public EnterpriseVO() {
        this.code = 0;
    }

}
