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

    public String ToString() {
        return StringUtils.leftPad(productId + "", 3, "0")
                + StringUtils.leftPad(batchId + "", 6, "0")
                + StringUtils.leftPad(casId + "", 7, "0")
                + StringUtils.leftPad(expressId + "", 5, "0")
                + StringUtils.leftPad(number + "", 7, "0");

    }

    public RfidInfoEntity(ExpressProductEntity expressProductEntity) {
        productId = expressProductEntity.getProductId();
        expressId = expressProductEntity.getExpressEntity().getExpressId();
        batchId = 0;
        number = expressProductEntity.getNumber() / 2;
        casId = 0;
    }

}


