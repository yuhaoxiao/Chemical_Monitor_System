package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.vo.InOutBatchVO;

public interface InOutBatchService {

    InOutBatchVO getInout(int ioId);

    InOutBatchVO createInout(int batchId, int storeId, int productId, double number, boolean isIn);

    InOutBatchVO InputBatch(int batchId);
}
