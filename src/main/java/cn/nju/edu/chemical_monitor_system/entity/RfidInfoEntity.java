package cn.nju.edu.chemical_monitor_system.entity;

import lombok.Data;

@Data
public class RfidInfoEntity {

    private int productId;

    private int batchId;

    private int casId;

    private int expressId;

    private double number;

    public RfidInfoEntity(String s) {

    }

    public String ToString() {
        return null;
    }

}
