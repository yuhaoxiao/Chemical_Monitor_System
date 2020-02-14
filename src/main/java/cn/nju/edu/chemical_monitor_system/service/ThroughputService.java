package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.vo.ThroughputVO;

public interface ThroughputService {

    ThroughputVO getCasThroughput(int casId, int entityType, int entityId, int timeType, String start, String end);

    ThroughputVO getEntityThroughput(int entityType, int entityId, int timeType, String start, String end);

}
