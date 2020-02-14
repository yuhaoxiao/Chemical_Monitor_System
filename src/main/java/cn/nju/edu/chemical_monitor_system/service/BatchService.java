package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.vo.BatchVO;
import cn.nju.edu.chemical_monitor_system.vo.InOutBatchVO;
import cn.nju.edu.chemical_monitor_system.vo.ProductVO;

import java.sql.Timestamp;
import java.util.List;

public interface BatchService {

    BatchVO createBatch(int productlineId, Timestamp time, String type, List<InOutBatchVO> inOutBatchVOS, int userId);

    BatchVO getBatch(int batchId);

    List<ProductVO> getBatchProduct(int batchId);

    List<InOutBatchVO> getBatchInout(int batchId, boolean isIn);
}
