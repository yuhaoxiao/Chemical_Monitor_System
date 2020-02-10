package cn.nju.edu.chemical_monitor_system.vo;

import cn.nju.edu.chemical_monitor_system.entity.ExpressEntity;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<ExpressProductVO> expressProductVOS;
    private String message;

    public ExpressVO() {

    }

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
        if (e.getInputUserId() != null) {
            this.inputUserId = e.getInputUserId();
        }
        if (e.getOutputUserId() != null) {
            this.outputUserId = e.getOutputUserId();
        }
        this.code = 1;
        this.expressProductVOS = e.getExpressProductEntities().stream()
                .map(ExpressProductVO::new)
                .collect(Collectors.toList());
    }

    public ExpressVO(String message) {
        this.code = 0;
        this.message = message;
    }

}
