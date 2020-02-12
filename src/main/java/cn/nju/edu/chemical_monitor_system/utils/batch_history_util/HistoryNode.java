package cn.nju.edu.chemical_monitor_system.utils.batch_history_util;

import cn.nju.edu.chemical_monitor_system.entity.InOutBatchEntity;
import lombok.Data;

import java.util.List;

@Data
public class HistoryNode {
    List<HistoryNode> historyNodes;
    int storeId;
    int batchId;
    int productId;
    double number;
    int type;
    HistoryNode(){

    }
    HistoryNode(InOutBatchEntity inOutBatchEntity){
        this.storeId=inOutBatchEntity.getStoreId();
        this.number=inOutBatchEntity.getNumber();
        this.batchId=inOutBatchEntity.getBatchId();
        this.productId=inOutBatchEntity.getProductId();
    }
}
