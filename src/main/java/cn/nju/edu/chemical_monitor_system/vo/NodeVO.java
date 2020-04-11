package cn.nju.edu.chemical_monitor_system.vo;

import lombok.Data;

import java.util.Objects;

@Data
public class NodeVO {
    private int batchId;
    private String productName;
    private String storeName;
    private int type; // 1是产品，2是批次
    private int batchType;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NodeVO nodeVO = (NodeVO) o;
        if(nodeVO.getType()==1){
            return false;
        }
        return batchId == nodeVO.batchId &&
                type == nodeVO.type &&
                Objects.equals(productName, nodeVO.productName) &&
                Objects.equals(storeName, nodeVO.storeName) &&
                Objects.equals(batchType, nodeVO.batchType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(batchId, productName, storeName, type, batchType);
    }
}