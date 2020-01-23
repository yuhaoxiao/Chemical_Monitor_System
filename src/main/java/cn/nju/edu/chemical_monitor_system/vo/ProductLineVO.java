package cn.nju.edu.chemical_monitor_system.vo;

import cn.nju.edu.chemical_monitor_system.entity.ProductionLineEntity;
import lombok.Data;

@Data
public class ProductLineVO {

    private int productionLineId;
    private int enterpriseId;
    private int code;

    public ProductLineVO(ProductionLineEntity pl) {
        if (pl == null) {
            this.code = 0;
            return;
        }

        this.productionLineId = pl.getProductionLineId();
        this.enterpriseId = pl.getEnterpriseId();
        this.code = 1;
    }

    public ProductLineVO() {
        this.code = 0;
    }

}
