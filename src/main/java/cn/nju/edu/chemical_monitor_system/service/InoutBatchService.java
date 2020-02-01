package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.vo.InOutBatchVO;

public interface InoutBatchService {

    InOutBatchVO getInout(int ioId);

    InOutBatchVO createInout(int batchId, int storeId, int productId, double number, boolean isIn);

    InOutBatchVO updateInout(int ioId, String status);
}
