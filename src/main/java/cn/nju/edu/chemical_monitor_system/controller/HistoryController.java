package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.service.CasService;
import cn.nju.edu.chemical_monitor_system.utils.batch_history_util.BatchHistoryUtil;
import cn.nju.edu.chemical_monitor_system.utils.batch_history_util.HistoryNode;
import cn.nju.edu.chemical_monitor_system.vo.CasVO;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HistoryController {

    @Autowired
    private BatchHistoryUtil batchHistoryUtil;

    @GetMapping(value = "history/get_history")
    public HistoryNode getHistory() {
        HistoryNode historyNode=batchHistoryUtil.getBeforeHistory(2);
        return historyNode;
    }
}
