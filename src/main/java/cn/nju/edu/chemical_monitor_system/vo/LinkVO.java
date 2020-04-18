package cn.nju.edu.chemical_monitor_system.vo;

import lombok.Data;

@Data
public class LinkVO {
    private int from;
    private int to;
    private double number;

    public LinkVO(int from, int to, double number) {
        this.from = from;
        this.to = to;
        this.number = number;
    }
}