package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.vo.InOutBatchVO;

public interface InOutBatchService {

    InOutBatchVO inputBatch(int batchId, int storeId);

    InOutBatchVO outputBatch(int batchId, int storeId, int productId, double number);

}
