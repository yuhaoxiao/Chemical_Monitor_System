package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.request.BatchOutRequest;
import cn.nju.edu.chemical_monitor_system.request.CreateBatchRequest;
import cn.nju.edu.chemical_monitor_system.vo.BatchVO;
import cn.nju.edu.chemical_monitor_system.vo.InOutBatchVO;
import cn.nju.edu.chemical_monitor_system.vo.ProductVO;

import java.util.List;

public interface BatchService {

    BatchVO createBatch(int productlineId, int type, List<CreateBatchRequest.RawRequest> raws, int userId);

    BatchVO batchOut(int batchId, List<BatchOutRequest.ProductRequest> products);

    BatchVO getBatch(int batchId);

    List<ProductVO> getBatchProduct(int batchId);

    List<InOutBatchVO> getBatchInout(int batchId, boolean isIn);
}
