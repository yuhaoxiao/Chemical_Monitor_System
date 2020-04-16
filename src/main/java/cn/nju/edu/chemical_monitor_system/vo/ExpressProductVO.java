package cn.nju.edu.chemical_monitor_system.vo;

import cn.nju.edu.chemical_monitor_system.entity.ExpressProductEntity;
import lombok.Data;

@Data
public class ExpressProductVO {

    private int expressProductId;
    private int expressId;
    private Double number;
    private int code;
    private String message;

    private Double finishedNumber;
    private Double thisNumber;
    private ProductVO productVO;

    public ExpressProductVO(ExpressProductEntity ep) {
        if (ep == null) {
            this.code = 0;
            return;
        }
        init(ep, null);
    }

    public ExpressProductVO(ExpressProductEntity ep, ProductVO productVO) {
        if (ep == null) {
            this.code = 0;
            return;
        }
        init(ep, productVO);
    }

    public ExpressProductVO(ExpressProductEntity ep, double finishedNumber, double thisNumber, ProductVO productVO) {
        if (ep == null) {
            this.code = 0;
            return;
        }
        init(ep, productVO);
        this.finishedNumber = finishedNumber;
        this.thisNumber = thisNumber;
    }

    private void init(ExpressProductEntity ep, ProductVO productVO) {
        this.expressProductId = ep.getExpressProductId();
        this.expressId = ep.getExpressEntity().getExpressId();
        this.number = ep.getNumber();
        this.code = 1;
        this.productVO = productVO;
    }

}
