package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.vo.ExpressProductVO;
import cn.nju.edu.chemical_monitor_system.vo.ExpressVO;
import cn.nju.edu.chemical_monitor_system.vo.ProductVO;

import java.util.List;
import java.util.Map;

public interface ExpressService {

    ExpressVO createExpress(int inputStoreId, int outputStoreId, Map<Integer, Double> productNumberMap);

    ExpressVO outputExpress(int expressId, int userId);

    ExpressVO getExpress(int expressId);

    List<ExpressProductVO> getProductExpress(int productId);

    ProductVO outputProduct(int expressId, int userId);

    ProductVO inputProduct(int expressId, int userId);

    ExpressVO outputProductRewrite(int productId, int expressId, int userId);


}
