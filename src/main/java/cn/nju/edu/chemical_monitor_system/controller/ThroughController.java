package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.request.GetThroughputRequest;
import cn.nju.edu.chemical_monitor_system.service.CasService;
import cn.nju.edu.chemical_monitor_system.service.ThroughputService;
import cn.nju.edu.chemical_monitor_system.vo.ThroughputVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThroughController {

    @Autowired
    private ThroughputService throughputService;

    @GetMapping(value = "/throughput/get_through")
    public ThroughputVO getThroughput(@RequestBody GetThroughputRequest request) {
        if (request.getCasId() == 0) {
            return throughputService.getEntityThroughput(request.getEntityType(), request.getEntityId(),
                    request.getTimeType(), request.getStart(), request.getEnd());
        } else {
            return throughputService.getCasThroughput(request.getEntityType(), request.getEntityId(),
                    request.getTimeType(), request.getStart(), request.getEnd(), request.getCasId());
        }
    }

}
