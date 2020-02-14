package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.request.CreateExpressRequest;
import cn.nju.edu.chemical_monitor_system.service.ExpressService;
import cn.nju.edu.chemical_monitor_system.utils.kafka_util.KafkaProducer;
import cn.nju.edu.chemical_monitor_system.vo.ExpressProductVO;
import cn.nju.edu.chemical_monitor_system.vo.ExpressVO;
import cn.nju.edu.chemical_monitor_system.vo.ProductVO;
import cn.nju.edu.chemical_monitor_system.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class ExpressController {

    private final ExpressService expressService;


    @Autowired
    public ExpressController(ExpressService expressService) {
        this.expressService = expressService;
    }

    @PostMapping(value = "/express/create_express")
    public ExpressVO createExpress(@RequestBody CreateExpressRequest createExpressRequest) {
        return expressService.createExpress(
                createExpressRequest.getInputStoreId(),
                createExpressRequest.getOutputStoreId(),
                createExpressRequest.getProductNumberMap()
        );
    }

    @PostMapping(value = "/express/output_product")
    public ProductVO outputProduct(int expressId, HttpServletRequest httpServletRequest) {
        UserVO userVO = (UserVO) httpServletRequest.getSession().getAttribute("User");
        return expressService.outputProduct(expressId, userVO.getUserId());
    }

    @PostMapping(value = "/express/input_product")
    public ProductVO inputProduct(int expressId, HttpServletRequest httpServletRequest) {
        UserVO userVO = (UserVO) httpServletRequest.getSession().getAttribute("User");
        return expressService.inputProduct(expressId, userVO.getUserId());
    }

    @GetMapping(value = "/express/get_product_express")
    public List<ExpressProductVO> getProductExpress(int productId) {
        return expressService.getProductExpress(productId);
    }

    @GetMapping(value = "/express/get_express")
    public ExpressVO getExpress(int expressId) {
        return expressService.getExpress(expressId);
    }

}
