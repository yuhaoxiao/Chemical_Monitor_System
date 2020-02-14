package cn.nju.edu.chemical_monitor_system.vo;

import java.util.List;

public class CASThroughputVO {

    private int casId;
    private String name;
    private List<Double> throughput; // 对应 ThroughputVO 中 times 的每一时间单位的吞吐量
    private double total;

}
