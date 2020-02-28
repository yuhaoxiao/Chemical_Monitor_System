package cn.nju.edu.chemical_monitor_system.vo;

import lombok.Data;

@Data
public class NodeVO {
    private int batchId;
    private String productName;
    private String storeName;
    private int type; // 1是产品，2是批次
    private String batchType;

}