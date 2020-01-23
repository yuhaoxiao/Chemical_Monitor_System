package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.vo.ProductVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @PostMapping(value = "product/add_product")
    public ProductVO addProduct(int batchId, int casId, double number) {
        return null;
    }

    @GetMapping(value = "product/get_product")
    public ProductVO getProduct(int productId){
        return null;
    }



}
