package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.vo.InOutBatchVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InoutController {

    @GetMapping(value = "inout/get_inout")
    public InOutBatchVO getInout(int inoutId){
        return null;
    }

    @PostMapping(value = "inout/create_inout")
    public InOutBatchVO createInout(int batchId, int storeId, int productId, double number) {
        return null;
    }

    @PostMapping(value = "inout/update_inout")
    public InOutBatchVO updateInout(int inoutId, String status) {
        return null;
    }
}
