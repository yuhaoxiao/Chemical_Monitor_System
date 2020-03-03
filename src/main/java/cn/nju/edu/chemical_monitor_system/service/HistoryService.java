package cn.nju.edu.chemical_monitor_system.service;

import java.util.Map;

public interface HistoryService {
    Map<String, Map> getHistory(int batchId);
}
