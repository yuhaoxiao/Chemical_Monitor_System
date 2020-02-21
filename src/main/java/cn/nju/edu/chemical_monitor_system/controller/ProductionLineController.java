package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.response.BaseResponse;
import cn.nju.edu.chemical_monitor_system.service.ProductionLineService;
import cn.nju.edu.chemical_monitor_system.vo.BatchVO;
import cn.nju.edu.chemical_monitor_system.vo.ProductionLineVO;
import cn.nju.edu.chemical_monitor_system.vo.StoreVO;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/production_line")
public class ProductionLineController {

    @Autowired
    private ProductionLineService productionLineService;


    //需要实现
    @GetMapping("/get_production_line/{productionLineId}")
    @RequiresRoles(logical = Logical.OR,value={"operator","administrator"})
    public BaseResponse getProductionLine(@PathVariable int productionLineId) {
        return new BaseResponse(200,"success",new ProductionLineVO());
    }

    @GetMapping("/get_in_park_by_eid/{enterpriseId}")
    @RequiresRoles(logical = Logical.OR,value={"operator","administrator"})
    public BaseResponse getInParkProductionLineByEnterpriseId(@PathVariable int enterpriseId) {
        return new BaseResponse(200,"success",new ProductionLineVO());
    }

    @GetMapping("/get_out_park_by_eid/{enterpriseId}")
    @RequiresRoles(logical = Logical.OR,value={"operator","administrator"})
    public BaseResponse getOutParkProductionLineByEnterpriseId(@PathVariable int enterpriseId) {
        return new BaseResponse(200,"success",new ProductionLineVO());
    }

    @GetMapping("/get_destroy_by_eid/{enterpriseId}")
    @RequiresRoles(logical = Logical.OR,value={"operator","administrator"})
    public BaseResponse getDestroyProductionLineByEnterpriseId(@PathVariable int enterpriseId) {
        return new BaseResponse(200,"success",new ProductionLineVO());
    }



    //以下接口暂时没有用到
    @PostMapping(value = "/productionline/add_productionline")
    public ProductionLineVO addProductionLine(int eid) {
        return productionLineService.addProductionLine(eid);
    }

    @PostMapping(value = "/productionline/delete_productionline")
    public ProductionLineVO deleteProductionLine(int plId){
        return productionLineService.deleteProductionLine(plId);
    }

    @PostMapping(value = "/productionline/update_productionline")
    public ProductionLineVO updateProductionLine(ProductionLineVO productionLineVO){
        return productionLineService.updateProductionLine(productionLineVO);
    }

    @GetMapping(value = "/productionline/get_produtionline_batch")
    public List<BatchVO> getProductionLineBatch(int plId){
        return productionLineService.getProductionBatch(plId);
    }
}
