package cn.nju.edu.chemical_monitor_system.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@ToString
public class GetThroughputRequest {

    private int entityType; // 0代表整个园区，1代表企业，2代表生产线，3代表仓库

    private int entityId; // 企业、生产线或仓库 id

    private int timeType; // 0代表年，1代表季度，2代表月，3代表日

    private String start;

    private String end;

    private int casId; // 如果不为0，代表只查这一个化学品的吞吐量

}
