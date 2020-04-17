package cn.nju.edu.chemical_monitor_system.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CASThroughputVO {

    private int casId;
    private String name;
    private List<Double> throughput; // 对应 ThroughputVO 中 times 的每一时间单位的吞吐量
    private double total;

    public CASThroughputVO(int casId, String name) {
        this.casId = casId;
        this.name = name;
        this.throughput = new ArrayList<>();
        this.total = 0.0;
    }

    public void calculate() {
        this.total = this.throughput.stream().reduce(Double::sum).orElse(0.0);
    }

    public CASThroughputVO(int casId, String name, List<Double> throughput, double total) {
        this.casId = casId;
        this.name = name;
        this.throughput = throughput;
        this.total = total;
    }
}
