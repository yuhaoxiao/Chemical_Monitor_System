package cn.nju.edu.chemical_monitor_system.vo;

import cn.nju.edu.chemical_monitor_system.entity.InOutBatchEntity;
import lombok.Data;

@Data
public class InOutBatchVO {

    private int inOutId;
    private int productId;
    private int storeId;
    private int batchId;
    private Integer inOrOut;
    private Double number;
    private String status;
    private Double finishedNumber;

    private int code;
    private String message;

    public InOutBatchVO(InOutBatchEntity io) {
        if (io == null) {
            this.code = 0;
            return;
        }

        this.inOrOut = io.getInout();
        this.productId = io.getProductId();
        this.storeId = io.getStoreId();
        this.batchId = io.getBatchId();
        this.inOrOut = io.getInout();
        this.number = io.getNumber();
        this.status = io.getStatus();
        this.finishedNumber = io.getFinishedNumber();
        this.code = 1;
    }

    public InOutBatchVO(String message) {
        this.code = 0;
        this.message = message;
    }
}
