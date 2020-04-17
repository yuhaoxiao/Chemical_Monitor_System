package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.response.BaseResponse;
import cn.nju.edu.chemical_monitor_system.service.CasService;
import cn.nju.edu.chemical_monitor_system.vo.CasVO;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cas")
public class CASController {

    @Autowired
    private CasService casService;

    @GetMapping(value = "/get_cas")  //  暂时没用到
    public CasVO getCAS(int casId) {
        return casService.getCas(casId);
    }

    @GetMapping(value = "/search_cas/{key}")
    @RequiresRoles(logical = Logical.OR, value = {"operator", "administrator", "monitor"})
    public BaseResponse searchCas(@PathVariable String key) {  // 根据关键词查询化学品（或直接是casId）
        return new BaseResponse(200, "success", casService.searchCas(key));
    }

    @PostMapping(value = "/init_cas")
    @RequiresRoles(value = {"administrator"})
    public BaseResponse initCas() {
        casService.init();
        return new BaseResponse(200, "success", null);
    }
}
