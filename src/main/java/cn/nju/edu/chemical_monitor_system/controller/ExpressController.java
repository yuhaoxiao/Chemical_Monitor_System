package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.vo.ExpressProductVO;
import cn.nju.edu.chemical_monitor_system.vo.ExpressVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class ExpressController {

    @PostMapping(value = "express/create_express")
    public ExpressVO createExpress(int inputStoreId, int outputStoreId, Date outputDate) {
        return null;
    }

    @PostMapping(value = "express/complete_express")
    public ExpressVO completeExpress(int expressId, Date inputDate) {
        return null;
    }

    @PostMapping(value = "express/add_product")
    public ExpressProductVO addProduct(int expressId, int productId, int number) {
        return null;
    }

    @GetMapping(value = "express/get_product_express")
    public List<ExpressProductVO> getProductExpress(int productId){
        return null;
    }

    @GetMapping(value = "express/get_express")
    public ExpressVO getExpress(int expressId){
        return null;
    }


}
