package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.service.ThroughputService;
import cn.nju.edu.chemical_monitor_system.vo.ThroughputVO;
import org.springframework.stereotype.Service;

@Service
public class ThroughputServiceImpl implements ThroughputService {

    @Override
    public ThroughputVO getCasThroughput(int casId, int entityType, int entityId, int timeType, String start, String end) {

        return null;
    }

    @Override
    public ThroughputVO getEntityThroughput(int entityType, int entityId, int timeType, String start, String end) {
        return null;
    }

}
