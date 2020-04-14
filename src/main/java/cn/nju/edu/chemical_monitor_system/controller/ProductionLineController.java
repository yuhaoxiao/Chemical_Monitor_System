package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.response.BaseResponse;
import cn.nju.edu.chemical_monitor_system.service.ProductionLineService;
import cn.nju.edu.chemical_monitor_system.vo.ProductionLineVO;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/production_line")
public class ProductionLineController {

    @Autowired
    private ProductionLineService productionLineService;

    @GetMapping("/get_production_line/{plId}")
    @RequiresRoles(logical = Logical.OR, value={"operator", "administrator"})
    public BaseResponse getProductionLine(@PathVariable int plId) {
        return new BaseResponse(200,"success", productionLineService.getProductionLine(plId));
    }

    @GetMapping("/get_in_park_by_eid/{eid}")
    @RequiresRoles(logical = Logical.OR, value={"operator", "administrator"})
    public BaseResponse getInParkProductionLineByEnterpriseId(@PathVariable int eid) {  // TODO: 根据企业id获取该企业的入园生产线
        return new BaseResponse(200,"success", ProductionLineVO.getSpecific(eid, 0));
    }

    @GetMapping("/get_out_park_by_eid/{eid}")
    @RequiresRoles(logical = Logical.OR, value={"operator", "administrator"})
    public BaseResponse getOutParkProductionLineByEnterpriseId(@PathVariable int eid) {  // TODO: 根据企业id获取该企业的出园生产线
        return new BaseResponse(200,"success",ProductionLineVO.getSpecific(eid, 1));
    }

    @GetMapping("/get_destroy_by_eid/{eid}")
    @RequiresRoles(logical = Logical.OR, value={"operator", "administrator"})
    public BaseResponse getDestroyProductionLineByEnterpriseId(@PathVariable int eid) {  // TODO: 根据企业id获取该企业的销毁生产线
        return new BaseResponse(200,"success",ProductionLineVO.getSpecific(eid, 2));
    }


    @RequiresRoles(value={"administrator"})
    @PostMapping(value = "/add_production_line")
    public BaseResponse addProductionLine(@RequestBody ProductionLineVO productionLineVO) {
        int eid = productionLineVO.getEnterpriseId();
        return new BaseResponse(200, "success", productionLineService.addProductionLine(eid));
    }

    @RequiresRoles(value={"administrator"})
    @PostMapping(value = "/delete_production_line/{plId}")
    public BaseResponse deleteProductionLine(@PathVariable int plId){
        return new BaseResponse(200, "success", productionLineService.deleteProductionLine(plId));
    }

    @RequiresRoles(value={"administrator"})
    @PostMapping(value = "/update_production_line")
    public BaseResponse updateProductionLine(@RequestBody ProductionLineVO productionLineVO){
        return new BaseResponse(200, "success", productionLineService.updateProductionLine(productionLineVO));
    }

    @RequiresRoles(value={"administrator"})
    @GetMapping(value = "/get_production_line_batch")
    public BaseResponse getProductionLineBatch(int plId){  //  暂时没用到
        return new BaseResponse(200, "success", productionLineService.getProductionBatch(plId));
    }

    @RequiresRoles(value={"administrator"})
    @GetMapping(value = "/search_production_line/{eid}")
    public BaseResponse searchProductionLine(@PathVariable int eid){
        return new BaseResponse(200, "success", productionLineService.searchByEnterprise(eid));
    }

    @RequiresRoles(value={"administrator"})
    @GetMapping
    public BaseResponse getAll(){
        return new BaseResponse(200, "success", productionLineService.getAll());
    }
}
