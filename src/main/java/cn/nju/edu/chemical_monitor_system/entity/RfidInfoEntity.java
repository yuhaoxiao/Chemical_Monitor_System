package cn.nju.edu.chemical_monitor_system.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class RfidInfoEntity {

    private int productId;

    private int batchId;

    private int casId;

    private int expressId;

    private double number;

    public RfidInfoEntity() {

    }

    public RfidInfoEntity(String s) {
        productId = Integer.parseInt(s.substring(0, 3));
        batchId = Integer.parseInt(s.substring(3, 9));
        casId = Integer.parseInt(s.substring(9, 16));
        expressId = Integer.parseInt(s.substring(16, 21));
        number = Double.parseDouble(s.substring(21, 28));
    }

    @Override
    public String toString() {
        return StringUtils.leftPad(productId + "", 3, "0")
                + StringUtils.leftPad(batchId + "", 6, "0")
                + StringUtils.leftPad(casId + "", 7, "0")
                + StringUtils.leftPad(expressId + "", 5, "0")
                + StringUtils.leftPad(number + "", 7, "0");
    }

    public RfidInfoEntity(ExpressProductEntity expressProductEntity) {
        this.productId = expressProductEntity.getProductId();
        this.expressId = expressProductEntity.getExpressEntity().getExpressId();
        this.batchId = 0;
        this.number = expressProductEntity.getNumber() / 2;
        this.casId = 0;
    }

    public RfidInfoEntity(InOutBatchEntity inOutBatchEntity) {
        this.productId = inOutBatchEntity.getProductId();
        this.expressId = 1;
        this.batchId = inOutBatchEntity.getBatchId();
        this.number = inOutBatchEntity.getNumber();
        this.casId = 1;
    }
}


