package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.service.ExpressService;
import cn.nju.edu.chemical_monitor_system.vo.ExpressProductVO;
import cn.nju.edu.chemical_monitor_system.vo.ExpressVO;
import cn.nju.edu.chemical_monitor_system.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class ExpressController {

    private final ExpressService expressService;

    @Autowired
    public ExpressController(ExpressService expressService) {
        this.expressService = expressService;
    }

    @PostMapping(value = "express/create_express")
    public ExpressVO createExpress(int inputStoreId, int outputStoreId, Map<Integer,Double> productNumberMap) {
        return expressService.createExpress(inputStoreId, outputStoreId, productNumberMap);
    }

    @PostMapping(value = "express/input_express")
    public ExpressVO inputExpress(int expressId, HttpServletRequest httpServletRequest) {
        UserVO userVO = (UserVO) httpServletRequest.getSession().getAttribute("User");
        return expressService.inputExpress(expressId, userVO.getUserId());
    }

    @PostMapping(value = "express/output_express")
    public ExpressVO outputExpress(int expressId, HttpServletRequest httpServletRequest) {
        UserVO userVO = (UserVO) httpServletRequest.getSession().getAttribute("User");
        return expressService.outputExpress(expressId, userVO.getUserId());
    }

    @GetMapping(value = "express/get_product_express")
    public List<ExpressProductVO> getProductExpress(int productId) {
        return expressService.getProductExpress(productId);
    }

    @GetMapping(value = "express/get_express")
    public ExpressVO getExpress(int expressId) {
        return expressService.getExpress(expressId);
    }

}
