package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.vo.BatchVO;
import cn.nju.edu.chemical_monitor_system.vo.ProductionLineVO;

import java.util.List;

public interface ProductionLineService {

    ProductionLineVO addProductionLine(int eid);

    ProductionLineVO deleteProductionLine(int plId);

    ProductionLineVO updateProductionLine(ProductionLineVO productionLineVO);

    List<BatchVO> getProductionBatch(int plId);

    List<ProductionLineVO> getAll();

    List<ProductionLineVO> searchByEnterprise(int eid);

    ProductionLineVO getProductionLine(int plId);
}
