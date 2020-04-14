package cn.nju.edu.chemical_monitor_system.vo;

import cn.nju.edu.chemical_monitor_system.entity.CasEntity;
import cn.nju.edu.chemical_monitor_system.entity.ProductEntity;
import cn.nju.edu.chemical_monitor_system.entity.StoreProductEntity;
import lombok.Data;

@Data
public class ProductVO {

    private int productId;
    private CasEntity cas;
    private int batchId;
    private Double number;
    private String message;

    private int code;

    public ProductVO(){

    }
    public ProductVO(ProductEntity p) {
        if (p == null) {
            this.code = 0;
            return;
        }
        this.productId = p.getProductId();
        this.batchId = p.getBatchId();
        this.number = p.getNumber();
        this.code = 1;
        this.cas = p.getCasEntity();
    }

    public ProductVO(StoreProductEntity sp) {
        if (sp == null) {
            this.code = 0;
            return;
        }
        ProductEntity p = sp.getProductEntity();
        this.productId = p.getProductId();
        this.batchId = p.getBatchId();
        this.number = sp.getNumber();
        this.code = 1;
        this.cas = p.getCasEntity();
    }

    public ProductVO(String message) {
        this.code = 0;
        this.message = message;
    }
}
