package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.service.ProductService;
import cn.nju.edu.chemical_monitor_system.service.RfidService;
import cn.nju.edu.chemical_monitor_system.vo.InOutBatchVO;
import cn.nju.edu.chemical_monitor_system.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "product/get_product")
    public ProductVO getProduct(int productId) {
        return productService.getProduct(productId);
    }

}
