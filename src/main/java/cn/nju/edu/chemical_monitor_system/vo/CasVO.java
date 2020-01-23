package cn.nju.edu.chemical_monitor_system.vo;

import cn.nju.edu.chemical_monitor_system.entity.CasEntity;
import lombok.Data;

@Data
public class CasVO {

    private int casId;
    private String name;
    private int code;

    public CasVO(CasEntity c) {
        if (c == null) {
            this.code = 0;
            return;
        }
        
        this.casId = c.getCasId();
        this.name = c.getName();
        this.code = 1;
    }

    public CasVO() {
        this.code = 0;
    }
}
