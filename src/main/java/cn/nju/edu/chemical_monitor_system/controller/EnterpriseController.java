package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.response.BaseResponse;
import cn.nju.edu.chemical_monitor_system.service.EnterpriseService;
import cn.nju.edu.chemical_monitor_system.vo.EnterpriseVO;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enterprise")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    @RequiresRoles(value = {"administrator"})
    @PostMapping(value = "/add_enterprise")
    public BaseResponse addEnterprise(@RequestBody EnterpriseVO enterpriseVO) {
        String name = enterpriseVO.getName();
        return new BaseResponse(200, "success", enterpriseService.addEnterprise(name));
    }

    @RequiresRoles(value = {"administrator"})
    @PostMapping(value = "/delete_enterprise/{eid}")
    public BaseResponse deleteEnterprise(@PathVariable int eid) {
        return new BaseResponse(200, "success", enterpriseService.deleteEnterprise(eid));
    }

    @RequiresRoles(value = {"administrator"})
    @PostMapping(value = "/update_enterprise")
    public BaseResponse updateEnterprise(@RequestBody EnterpriseVO enterpriseVO) {
        return new BaseResponse(200, "success", enterpriseService.updateEnterprise(enterpriseVO));
    }

    @RequiresRoles(value = {"administrator"})
    @GetMapping(value = "/search_enterprise/{s}")
    public BaseResponse searchEnterprise(@PathVariable String s) {
        return new BaseResponse(200, "success", enterpriseService.searchEnterprise(s));
    }

    @RequiresRoles(value = {"administrator"})
    @GetMapping
    public BaseResponse getAll() {
        return new BaseResponse(200, "success", enterpriseService.getAll());
    }

}
