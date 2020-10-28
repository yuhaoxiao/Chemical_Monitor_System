package cn.nju.edu.chemical_monitor_system.request;

import lombok.Data;

@Data
public class RawRequest { // 新建inbatch
    private int productId;
    private int storeId;
    private double number;
}
