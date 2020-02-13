package cn.nju.edu.chemical_monitor_system.vo;

import cn.nju.edu.chemical_monitor_system.entity.ProductionLineEntity;
import lombok.Data;

@Data
public class ProductionLineVO {

    private int productionLineId;
    private int enterpriseId;
    private int code;
    private int enable;
    private String message;

    public ProductionLineVO(ProductionLineEntity pl) {
        if (pl == null) {
            this.code = 0;
            return;
        }

        this.productionLineId = pl.getProductionLineId();
        this.enterpriseId = pl.getEnterpriseEntity().getEnterpriseId();
        this.enable = pl.getEnable();
        this.code = 1;
    }

    public ProductionLineVO(String message) {
        this.code = 0;
        this.message = message;
    }

}
