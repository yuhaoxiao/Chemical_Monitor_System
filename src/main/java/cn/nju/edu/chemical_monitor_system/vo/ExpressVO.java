package cn.nju.edu.chemical_monitor_system.vo;

import cn.nju.edu.chemical_monitor_system.entity.ExpressEntity;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ExpressVO {

    private int expressId;
    private Timestamp outputTime;
    private Timestamp inputTime;
    private int status;
    private int inputUserId;
    private int outputUserId;
    private int inputStoreId;
    private int outputStoreId;
    private int code;

    public ExpressVO(ExpressEntity e) {
        if (e == null) {
            this.code = 0;
            return;
        }

        this.expressId = e.getExpressId();
        this.outputTime = e.getOutputTime();
        this.inputTime = e.getInputTime();
        this.status = e.getStatus();
        this.inputStoreId = e.getInputStoreId();
        this.outputStoreId = e.getOutputStoreId();
        this.inputUserId = e.getInputUserId();
        this.outputUserId = e.getOutputUserId();
        this.code = 1;
    }

    public ExpressVO() {
        this.code = 0;
    }

}
