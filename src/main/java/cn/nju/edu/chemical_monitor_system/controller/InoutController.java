package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.response.BaseResponse;
import cn.nju.edu.chemical_monitor_system.service.InOutBatchService;
import cn.nju.edu.chemical_monitor_system.vo.InOutBatchVO;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inout")
public class InoutController {

    @Autowired
    private InOutBatchService inoutService;

    //需要添加实现
    @PostMapping(value = "/input_production_batch/{batchId}/{storeId}")
    @RequiresRoles(value={"operator"})
    public BaseResponse inputProductionBatch(@PathVariable int batchId, @PathVariable int storeId) { // 原料出库
        return new BaseResponse(200,"出库成功",new InOutBatchVO());
    }

    @PostMapping(value = "/input_batch/{batchId}")
    @RequiresRoles(value={"operator"})
    public BaseResponse inputBatch(@PathVariable int batchId) {
        return new BaseResponse(200,"出库成功",inoutService.inputBatch(batchId));
    }

    //这里好像有个参数没用到？
    @PostMapping(value = "/output_batch/{batchId}/{storeId}/{productId}/{number}")
    @RequiresRoles(value={"operator"})
    public BaseResponse outputBatch(@PathVariable int batchId, @PathVariable int storeId,
                                    @PathVariable int productId, @PathVariable double number) {
        return new BaseResponse(200,"出库成功",inoutService.outputBatch(batchId, productId, number));
    }




    //以下接口暂时不需要
    @GetMapping(value = "/get_inout")
    public InOutBatchVO getInout(int inoutId) {
        return inoutService.getInout(inoutId);
    }
}
