package cn.nju.edu.chemical_monitor_system.vo;

import cn.nju.edu.chemical_monitor_system.entity.ProductionLineEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
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

    private static Map<Integer, ProductionLineVO[]> SPECIFIC_MAP;

    static {
        SPECIFIC_MAP = new HashMap<>();
        ProductionLineVO p0 = new ProductionLineVO("");
        ProductionLineVO p1 = new ProductionLineVO(); p1.setProductionLineId(1); p1.setEnterpriseId(1); p1.setEnable(1); p1.setCode(1);
        ProductionLineVO p2 = new ProductionLineVO(); p2.setProductionLineId(2); p2.setEnterpriseId(3); p2.setEnable(1); p2.setCode(1);
        ProductionLineVO p3 = new ProductionLineVO(); p3.setProductionLineId(3); p3.setEnterpriseId(1); p3.setEnable(1); p3.setCode(1);
        ProductionLineVO p4 = new ProductionLineVO(); p4.setProductionLineId(4); p4.setEnterpriseId(2); p4.setEnable(1); p4.setCode(1);
        ProductionLineVO p5 = new ProductionLineVO(); p5.setProductionLineId(5); p5.setEnterpriseId(1); p5.setEnable(1); p5.setCode(1);
        ProductionLineVO p6 = new ProductionLineVO(); p6.setProductionLineId(6); p6.setEnterpriseId(2); p6.setEnable(1); p6.setCode(1);
        SPECIFIC_MAP.put(1, new ProductionLineVO[]{p1, p3, p5});
        SPECIFIC_MAP.put(2, new ProductionLineVO[]{p4, p0, p6});
        SPECIFIC_MAP.put(3, new ProductionLineVO[]{p0, p2, p0});
    }

    public static ProductionLineVO getSpecific(int eid, int type) {
        return SPECIFIC_MAP.get(eid)[type];
    }

}
