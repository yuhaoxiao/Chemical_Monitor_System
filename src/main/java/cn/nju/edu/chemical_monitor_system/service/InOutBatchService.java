package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.vo.InOutBatchVO;

public interface InOutBatchService {

    InOutBatchVO getInout(int ioId);

    InOutBatchVO inputBatch(int batchId);

    InOutBatchVO outputBatch(int batchId, int productId, double number);

}
