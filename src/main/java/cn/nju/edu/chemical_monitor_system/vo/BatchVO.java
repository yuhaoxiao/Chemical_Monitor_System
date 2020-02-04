package cn.nju.edu.chemical_monitor_system.vo;

import cn.nju.edu.chemical_monitor_system.entity.BatchEntity;
import cn.nju.edu.chemical_monitor_system.entity.UserEntity;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class BatchVO {

    private int batchId;
    private int productionLineId;
    private int userId;
    private Timestamp time;
    private int code;
    private UserEntity userEntity;
    private String type;

    public BatchVO(BatchEntity b) {
        if (b == null) {
            this.code = 0;
            return;
        }

        this.batchId = b.getBatchId();
        this.productionLineId = b.getProductionLineId();
        this.userId = b.getUserEntity().getUserId();
        this.time = b.getTime();
        this.code = 1;
        this.userEntity = b.getUserEntity();
        this.type = b.getType();
    }

    public BatchVO() {
        this.code = 0;
    }

}
