package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.service.CasService;
import cn.nju.edu.chemical_monitor_system.vo.CasVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CASController {

    @Autowired
    private CasService casService;

    @GetMapping(value = "cas/get_cas")
    public CasVO getCAS(int casId){
        return casService.getCas(casId);
    }
}
