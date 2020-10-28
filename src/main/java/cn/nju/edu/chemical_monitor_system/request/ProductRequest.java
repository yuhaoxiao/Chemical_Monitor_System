package cn.nju.edu.chemical_monitor_system.request;

import lombok.Data;

@Data
public class ProductRequest { // 新建product和outbatch
    private int casId;
    private int storeId;
    private double number;
}
