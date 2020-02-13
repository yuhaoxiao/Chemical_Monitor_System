package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.request.CreateBatchRequest;
import cn.nju.edu.chemical_monitor_system.service.BatchService;
import cn.nju.edu.chemical_monitor_system.vo.BatchVO;
import cn.nju.edu.chemical_monitor_system.vo.InOutBatchVO;
import cn.nju.edu.chemical_monitor_system.vo.ProductVO;
import cn.nju.edu.chemical_monitor_system.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class BatchController {

    @Autowired
    private BatchService batchService;

    /**
     * 创建批次
     * @param createBatchRequest 其中的inoutBatch是原材料
     * @return
     */
    @PostMapping(value = "batch/create_batch")
    public BatchVO createBatch(@RequestBody CreateBatchRequest createBatchRequest, HttpServletRequest httpServletRequest) {
        UserVO userVO = (UserVO) httpServletRequest.getSession().getAttribute("User");
        return batchService.createBatch(createBatchRequest.getProductionlineId(), createBatchRequest.getTime(),
                createBatchRequest.getType(), createBatchRequest.getInOutBatchVOS(), userVO.getUserId());
    }

    @GetMapping(value = "batch/get_batch")
    public BatchVO getBatch(int batchId) {
        return batchService.getBatch(batchId);
    }

    @GetMapping(value = "batch/get_batch_product")
    public List<ProductVO> getBatchProduct(int batchId) {
        return batchService.getBatchProduct(batchId);
    }

    @GetMapping(value = "batch/get_batch_inout")
    public List<InOutBatchVO> getBatchInout(int batchId, boolean isIn) {
        return batchService.getBatchInout(batchId, isIn);
    }
}
