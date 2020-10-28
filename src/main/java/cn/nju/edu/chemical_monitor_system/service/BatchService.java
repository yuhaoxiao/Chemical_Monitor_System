package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.request.ProductRequest;
import cn.nju.edu.chemical_monitor_system.request.RawRequest;
import cn.nju.edu.chemical_monitor_system.vo.BatchVO;
import cn.nju.edu.chemical_monitor_system.vo.InOutBatchVO;
import cn.nju.edu.chemical_monitor_system.vo.ProductVO;
import cn.nju.edu.chemical_monitor_system.vo.StoreVO;

import java.util.List;

public interface BatchService {

    BatchVO createBatch(int productlineId, int type, List<RawRequest> raws, int userId);

    BatchVO batchOut(int batchId, List<ProductRequest> products);

    BatchVO getBatch(int batchId);

    List<StoreVO> getBatchStores(int batchId, int isIn);

    List<ProductVO> getBatchProduct(int batchId);

    List<InOutBatchVO> getBatchInout(int batchId, boolean isIn);
}
