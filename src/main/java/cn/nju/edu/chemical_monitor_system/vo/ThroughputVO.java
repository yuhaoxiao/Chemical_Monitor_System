package cn.nju.edu.chemical_monitor_system.vo;

import lombok.Data;

import java.util.List;

@Data
public class ThroughputVO {
    // 排好序的时间字符串 如:
    // ["2018", "2019", "2020"]
    // ["2019-Q3", "2019-Q4", "2020-Q1"]
    // ["2020-01", "2020-02", "2020-03"]
    // ["2020-01-02", "2020-01-03"]
    private List<String> times;
    private List<CASThroughputVO> consume; // 如果是企业和生产线，返回原料和销毁的数量；如果是园区，返回原料和销毁的数量；如果是仓库，返回一个长度为0的List
    private List<CASThroughputVO> produce; // 如果是企业和生产线，返回产品的数量；如果是园区，返回产品的数量；如果是仓库，返回一个长度为0的List
    private List<CASThroughputVO> in; // 如果是园区，返回入园的数量；如果是仓库，返回入库的数量；如果是企业和生产线；返回一个长度为0的List
    private List<CASThroughputVO> out; // 如果是园区，返回出园的数量；如果是仓库，返回出库的数量；如果是企业和生产线；返回一个长度为0的List
}
