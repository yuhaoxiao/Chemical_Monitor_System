package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.vo.ThroughputVO;

public interface ThroughputService {

    ThroughputVO getEntityThroughput(int entityType, int entityId, int timeType, String start, String end);

}
