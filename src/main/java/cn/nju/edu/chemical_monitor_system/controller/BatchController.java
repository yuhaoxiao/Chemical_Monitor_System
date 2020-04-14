package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.request.BatchOutRequest;
import cn.nju.edu.chemical_monitor_system.request.CreateBatchRequest;
import cn.nju.edu.chemical_monitor_system.response.BaseResponse;
import cn.nju.edu.chemical_monitor_system.service.BatchService;
import cn.nju.edu.chemical_monitor_system.utils.common.UserUtil;
import cn.nju.edu.chemical_monitor_system.vo.InOutBatchVO;
import cn.nju.edu.chemical_monitor_system.vo.ProductVO;
import cn.nju.edu.chemical_monitor_system.vo.StoreVO;
import cn.nju.edu.chemical_monitor_system.vo.UserVO;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController

@RequestMapping("/batch")
public class BatchController {

    @Autowired
    private BatchService batchService;

    @Autowired
    private UserUtil userUtil;
    @RequiresRoles(value = {"operator"})
    @PostMapping(value = "/create_batch")
    public BaseResponse createBatch(@RequestBody CreateBatchRequest createBatchRequest) {
        UserVO userVO = userUtil.getUser();
        return new BaseResponse(200, "success",
                batchService.createBatch(createBatchRequest.getProductionLineId(),
                createBatchRequest.getType(), createBatchRequest.getRaws(), userVO.getUserId()));
    }

    @RequiresRoles(value = {"operator"})
    @PostMapping(value = "/batch_out")
    public BaseResponse batchOut(@RequestBody BatchOutRequest batchOutRequest) {
        return new BaseResponse(200, "success",
                batchService.batchOut(batchOutRequest.getBatchId(), batchOutRequest.getProducts()));
    }

    @RequiresRoles(logical = Logical.OR, value = {"operator", "administrator"})
    @GetMapping(value = "/get_batch/{batchId}")
    public BaseResponse getBatch(@PathVariable int batchId) {
        return new BaseResponse(200, "success", batchService.getBatch(batchId));
    }

    @RequiresRoles(logical = Logical.OR, value = {"operator", "administrator"})
    @GetMapping("/get_batch_in_stores/{batchId}") // 查询该批次还需要出库原料的仓库
    public BaseResponse getBatchInStores(@PathVariable int batchId) {
        List<StoreVO> batchVOS = batchService.getBatchStores(batchId, 1);
        if (batchVOS == null) {
            return new BaseResponse(500, "fail", new ArrayList<StoreVO>());
        }
        return new BaseResponse(200, "success", batchVOS);
    }

    @RequiresRoles(logical = Logical.OR, value = {"operator", "administrator"})
    @GetMapping("/get_batch_out_stores/{batchId}") // 查询该批次还需要入库产品的仓库
    public BaseResponse getBatchOutStores(@PathVariable int batchId) {
        List<StoreVO> batchVOS = batchService.getBatchStores(batchId, 0);
        if (batchVOS == null) {
            return new BaseResponse(500, "fail", new ArrayList<StoreVO>());
        }
        return new BaseResponse(200, "success", batchVOS);
    }

    @RequiresRoles(logical = Logical.OR, value = {"operator", "administrator"})
    @GetMapping(value = "/get_batch_product")  //  暂时没用到
    public List<ProductVO> getBatchProduct(int batchId) {
        return batchService.getBatchProduct(batchId);
    }

    @RequiresRoles(logical = Logical.OR, value = {"operator", "administrator"})
    @GetMapping(value = "/get_batch_inout")  //  暂时没用到
    public List<InOutBatchVO> getBatchInout(int batchId, boolean isIn) {
        return batchService.getBatchInout(batchId, isIn);
    }
}
