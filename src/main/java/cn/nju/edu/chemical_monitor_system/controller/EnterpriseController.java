package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.service.EnterpriseService;
import cn.nju.edu.chemical_monitor_system.vo.EnterpriseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    @PostMapping(value = "enterprise/add_enterprise")
    public EnterpriseVO addEnterprise(String name){
        return enterpriseService.addEnterprise(name);
    }

    @PostMapping(value = "enterprise/delete_enterprise")
    public EnterpriseVO deleteEnterprise(int eid){
        return enterpriseService.deleteEnterprise(eid);
    }

    @PostMapping(value = "enterprise/update_enterprise")
    public EnterpriseVO updateEnterprise(EnterpriseVO enterpriseVO){
        return enterpriseService.updateEnterprise(enterpriseVO);
    }

    @GetMapping(value = "enterprise/search_enterprise")
    public List<EnterpriseVO> searchEnterprise(String s){
        return enterpriseService.searchEnterprise(s);
    }

}
