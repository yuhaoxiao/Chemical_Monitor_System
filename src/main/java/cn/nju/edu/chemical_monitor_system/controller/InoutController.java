package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.request.AddProductRequest;
import cn.nju.edu.chemical_monitor_system.service.InOutBatchService;
import cn.nju.edu.chemical_monitor_system.vo.InOutBatchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InoutController {

    @Autowired
    private InOutBatchService inoutService;

    @GetMapping(value = "inout/get_inout")
    public InOutBatchVO getInout(int inoutId) {
        return inoutService.getInout(inoutId);
    }

    @PostMapping(value = "inout/add_product")
    public List<InOutBatchVO> addProduct(@RequestBody AddProductRequest addProductRequest) {
        return inoutService.addProduct(addProductRequest.getBatchId(),
                addProductRequest.getCasNumberMap(), addProductRequest.getStoreId())
    }

    @PostMapping(value = "inout/input_batch")
    public InOutBatchVO InputBatch(int batchId) {
        return inoutService.inputBatch(batchId);
    }

    @PostMapping(value = "inout/output_batch")
    public InOutBatchVO OutputBatch(int batchId, int productId, double number) {
        return inoutService.outputBatch(batchId, productId, number);
    }
}
