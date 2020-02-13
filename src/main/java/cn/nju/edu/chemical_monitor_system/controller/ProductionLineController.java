package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.service.ProductionLineService;
import cn.nju.edu.chemical_monitor_system.vo.BatchVO;
import cn.nju.edu.chemical_monitor_system.vo.ProductionLineVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductionLineController {

    @Autowired
    private ProductionLineService productionLineService;

    @PostMapping(value = "productionline/add_productionline")
    public ProductionLineVO addProductionLine(int eid) {
        return productionLineService.addProductionLine(eid);
    }

    @PostMapping(value = "productionline/delete_productionline")
    public ProductionLineVO deleteProductionLine(int plId){
        return productionLineService.deleteProductionLine(plId);
    }

    @PostMapping(value = "productionline/update_productionline")
    public ProductionLineVO updateProductionLine(ProductionLineVO productionLineVO){
        return productionLineService.updateProductionLine(productionLineVO);
    }

    @GetMapping(value = "productionline/get_produtionline_batch")
    public List<BatchVO> getProductionLineBatch(int plId){
        return productionLineService.getProductionBatch(plId);
    }
}
