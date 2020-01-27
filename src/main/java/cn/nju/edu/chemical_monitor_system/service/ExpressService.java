package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.vo.ExpressProductVO;
import cn.nju.edu.chemical_monitor_system.vo.ExpressVO;
import cn.nju.edu.chemical_monitor_system.vo.ProductVO;

import java.util.List;

public interface ExpressService {
    ExpressVO createExpress(int inputStoreId, int outputStoreId, List<ProductVO> productVOS);
    ExpressVO inputExpress(int expressId,int userId);
    ExpressVO outputExpress(int expressId,int userId);
    ExpressVO findExpress(int expressId);
    List<ExpressProductVO> getProductExpress(int productId);

}
