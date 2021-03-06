package cn.nju.edu.chemical_monitor_system.vo;

import cn.nju.edu.chemical_monitor_system.entity.StoreEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StoreVO {

    private int storeId;
    private int enterpriseId;
    private String name;
    private int enable;
    private int code;
    private String message;

    public StoreVO(StoreEntity s) {
        if (s == null) {
            this.code = 0;
            return;
        }

        this.storeId = s.getStoreId();
        this.enterpriseId = s.getEnterpriseId();
        this.name = s.getName();
        this.enable = s.getEnable();
        this.code = 1;
    }

    public StoreVO(String message) {
        this.code = 0;
        this.message = message;
    }
}
