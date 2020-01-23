package cn.nju.edu.chemical_monitor_system.vo;

import cn.nju.edu.chemical_monitor_system.entity.ExpressProductEntity;
import lombok.Data;

@Data
public class ExpressProductVO {

    private int expressProductId;
    private int expressId;
    private int productId;
    private Double number;
    private int code;

    public ExpressProductVO(ExpressProductEntity ep) {
        if (ep == null) {
            this.code = 0;
            return;
        }

        this.expressProductId = ep.getExpressProductId();
        this.expressId = ep.getExpressId();
        this.productId = ep.getProductId();
        this.number = ep.getNumber();
        this.code = 1;
    }

    public ExpressProductVO() {
        this.code = 0;
    }
}
