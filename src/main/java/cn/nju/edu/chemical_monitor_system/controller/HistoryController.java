package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.response.BaseResponse;
import cn.nju.edu.chemical_monitor_system.utils.history.BatchHistoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private BatchHistoryUtil batchHistoryUtil;
    @GetMapping("/batch_{batchId}")
    public BaseResponse getCompositionInfo(@PathVariable("batchId") int batchId) {
        Map<String, Map> result=batchHistoryUtil.getHistory(batchId);
        return new BaseResponse(200, "success", result);
    }
}
