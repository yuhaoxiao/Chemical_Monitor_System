package cn.nju.edu.chemical_monitor_system.utils.batch_history_util;

import lombok.Data;

import java.util.List;

@Data
public class HistoryNode {
    List<HistoryNode> historyNodes;
    List<Double> nums;
    int storeId;
    int batchId;
    int productId;
    double number;
    int type;
    int visit;//0未访问，1访问
}
