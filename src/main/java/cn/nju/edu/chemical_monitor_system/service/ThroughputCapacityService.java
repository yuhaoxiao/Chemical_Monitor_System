package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.request.GetThroughputRequest;
import cn.nju.edu.chemical_monitor_system.vo.ThroughputVO;

public interface ThroughputCapacityService {
    ThroughputVO getThroughput(GetThroughputRequest getThroughputRequest);
}
