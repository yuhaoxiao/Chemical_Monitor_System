package cn.nju.edu.chemical_monitor_system.vo;

import cn.nju.edu.chemical_monitor_system.entity.ProductEntity;
import lombok.Data;

@Data
public class ProductVO {

    private int productId;
    private int casId;
    private int batchId;
    private Double number;
    private int code;

    public ProductVO(ProductEntity p) {
        if (p == null) {
            this.code = 0;
            return;
        }
        this.productId = p.getProductId();
        this.casId = p.getCasId();
        this.batchId = p.getBatchId();
        this.number = p.getNumber();
        this.code = 1;
    }

    public ProductVO() {
        this.code = 0;
    }
}
