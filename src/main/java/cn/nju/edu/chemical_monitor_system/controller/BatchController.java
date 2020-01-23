package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.vo.BatchVO;
import cn.nju.edu.chemical_monitor_system.vo.InOutBatchVO;
import cn.nju.edu.chemical_monitor_system.vo.ProductVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class BatchController {

    @PostMapping(value = "batch/create_batch")
    public BatchVO createBatch(int productlineId, Date date) {
        return null;
    }

    @PostMapping(value = "batch/update_batch")
    public BatchVO updateBatch(int batchId, String status) {
        return null;
    }

    @GetMapping(value = "batch/get_batch")
    public BatchVO getBatch(int batchId){
        return null;
    }

    @GetMapping(value = "batch/get_batch_product")
    public List<ProductVO> getBatchProduct(int batchId){
        return null;
    }

    @GetMapping(value = "batch/get_batch_inout")
    public List<InOutBatchVO> getBatchInout(int batchId, boolean isIn){
        return null;
    }


}
