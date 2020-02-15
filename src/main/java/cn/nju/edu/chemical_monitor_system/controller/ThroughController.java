package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.request.GetThroughputRequest;
import cn.nju.edu.chemical_monitor_system.service.CasService;
import cn.nju.edu.chemical_monitor_system.service.ThroughputService;
import cn.nju.edu.chemical_monitor_system.vo.CASThroughputVO;
import cn.nju.edu.chemical_monitor_system.vo.CasVO;
import cn.nju.edu.chemical_monitor_system.vo.ThroughputVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ThroughController {

    @Autowired
    private ThroughputService throughputService;

    @Autowired
    private CasService casService;

    @GetMapping(value = "/throughput/get_through")
    public ThroughputVO getThroughput(@RequestBody GetThroughputRequest request) {
        ThroughputVO throughputVO = throughputService.getEntityThroughput(request.getEntityType(), request.getEntityId(),
                request.getTimeType(), request.getStart(), request.getEnd());

        if (request.getCasId() == 0) {
            return throughputVO;
        }

        CasVO casVO = casService.getCas(request.getCasId());
        if (casVO.getCode() == 0) {
            return new ThroughputVO("CAS id不存在");
        }

        List<CASThroughputVO> consume = throughputVO.getConsume();
        if (consume.size() != 0) {
            for (CASThroughputVO ct : consume) {
                if (ct.getCasId() == request.getCasId()) {
                    List<CASThroughputVO> temp = new ArrayList<>();
                    temp.add(ct);
                    throughputVO.setConsume(temp);
                    break;
                }
            }
        }

        List<CASThroughputVO> produce = throughputVO.getProduce();
        if (produce.size() != 0) {
            for (CASThroughputVO ct : produce) {
                if (ct.getCasId() == request.getCasId()) {
                    List<CASThroughputVO> temp = new ArrayList<>();
                    temp.add(ct);
                    throughputVO.setProduce(temp);
                    break;
                }
            }
        }

        List<CASThroughputVO> in = throughputVO.getIn();
        if (in.size() != 0) {
            for (CASThroughputVO ct : consume) {
                if (ct.getCasId() == request.getCasId()) {
                    List<CASThroughputVO> temp = new ArrayList<>();
                    temp.add(ct);
                    throughputVO.setIn(temp);
                    break;
                }
            }
        }

        List<CASThroughputVO> out = throughputVO.getOut();
        if (out.size() != 0) {
            for (CASThroughputVO ct : consume) {
                if (ct.getCasId() == request.getCasId()) {
                    List<CASThroughputVO> temp = new ArrayList<>();
                    temp.add(ct);
                    throughputVO.setOut(temp);
                    break;
                }
            }
        }

        return throughputVO;
    }

}
