package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.request.CreateExpressRequest;
import cn.nju.edu.chemical_monitor_system.response.BaseResponse;
import cn.nju.edu.chemical_monitor_system.service.ExpressService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/express")
public class ExpressController {

    private final ExpressService expressService;
    @Autowired
    public ExpressController(ExpressService expressService) {
        this.expressService = expressService;
    }

    @PostMapping(value = "/create_express")
    @RequiresRoles(value={"operator"})
    public BaseResponse createExpress(@RequestBody CreateExpressRequest createExpressRequest) {
        return new BaseResponse(200, "创建物流单成功",expressService.createExpress(
                createExpressRequest.getInputStoreId(),
                createExpressRequest.getOutputStoreId(),
                createExpressRequest.getProductNumberMap()
        ));
    }

    @PostMapping(value = "/output_product/{expressId}")
    @RequiresRoles(value={"operator"})
    public BaseResponse outputProduct(@PathVariable int expressId) {
        return new BaseResponse(200,"成功", expressService.outputProduct(expressId));
    }

    @PostMapping(value = "/input_product/{expressId}")
    @RequiresRoles(value={"operator"})
    public BaseResponse inputProduct(@PathVariable int expressId) {
        return new BaseResponse(200,"成功", expressService.inputProduct(expressId));
    }

    @GetMapping(value = "/get_product_express")
    @RequiresRoles(logical = Logical.OR, value={"operator","administrator"})
    public BaseResponse getProductExpress(int productId) {  //  暂时没用到
        return new BaseResponse(200,"成功",expressService.getProductExpress(productId));
    }

    @GetMapping(value = "/get_express/{expressId}")
    @RequiresRoles(logical = Logical.OR, value={"operator","administrator"})
    public BaseResponse getExpress(@PathVariable int expressId) {
        return new BaseResponse(200,"成功",expressService.getExpress(expressId));
    }

    @PostMapping(value = "/reverse_express/{expressId}")
    @RequiresRoles(logical = Logical.OR, value={"operator"})
    public BaseResponse reverseExpress(@PathVariable int expressId) {
        return new BaseResponse(200,"成功",expressService.reverseExpress(expressId));
    }

}
