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

    @PostMapping(value = "/input_batch/{batchId}")
    @RequiresRoles(value={"operator"})
    public BaseResponse inputBatch(@PathVariable int batchId, @PathVariable int storeId) {
        return new BaseResponse(200,"出库成功",inoutService.inputBatch(batchId, storeId));
    }

    @PostMapping(value = "/output_batch/{batchId}/{storeId}/{productId}/{number}")
    @RequiresRoles(value={"operator"})
    public BaseResponse outputBatch(@PathVariable int batchId, @PathVariable int storeId,
                                    @PathVariable int productId, @PathVariable double number) {
        return new BaseResponse(200,"出库成功",inoutService.outputBatch(batchId, storeId, productId, number));
    }

}
