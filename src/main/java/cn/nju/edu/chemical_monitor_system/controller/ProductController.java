package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.service.ProductService;
import cn.nju.edu.chemical_monitor_system.service.RfidService;
import cn.nju.edu.chemical_monitor_system.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private RfidService rfidService;

    @PostMapping(value = "product/add_product")
    public ProductVO addProduct(int batchId, int casId, double number) {
        ProductVO productVO = productService.addProduct(batchId, casId, number);
        rfidService.writeRfid(productVO.getProductId());
        return productVO;
    }

    @GetMapping(value = "product/get_product")
    public ProductVO getProduct(int productId) {
        return productService.getProduct(productId);
    }

}
