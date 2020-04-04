package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.request.GetThroughputRequest;
import cn.nju.edu.chemical_monitor_system.response.BaseResponse;
import cn.nju.edu.chemical_monitor_system.service.ThroughputService;
import cn.nju.edu.chemical_monitor_system.vo.ThroughputVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/throughput")
public class ThroughController {

    @Autowired
    private ThroughputService throughputService;

    @PostMapping
    public BaseResponse getThroughput(@RequestBody GetThroughputRequest request) {
        ThroughputVO data;
        if (request.getCasId() == 0) {
            data = throughputService.getEntityThroughput(request.getEntityType(), request.getEntityId(),
                    request.getTimeType(), request.getStart(), request.getEnd());
        } else {
            data = throughputService.getCasThroughput(request.getEntityType(), request.getEntityId(),
                    request.getTimeType(), request.getStart(), request.getEnd(), request.getCasId());
        }
        return new BaseResponse(200, "success", data);
    }

}
