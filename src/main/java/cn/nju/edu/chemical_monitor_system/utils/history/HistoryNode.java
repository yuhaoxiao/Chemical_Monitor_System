package cn.nju.edu.chemical_monitor_system.utils.history;

import lombok.Data;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryNode that = (HistoryNode) o;
        return storeId == that.storeId &&
                batchId == that.batchId &&
                productId == that.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, batchId, productId);
    }
}
