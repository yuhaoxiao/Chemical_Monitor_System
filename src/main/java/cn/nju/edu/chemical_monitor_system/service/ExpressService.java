package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.vo.ExpressProductVO;
import cn.nju.edu.chemical_monitor_system.vo.ExpressVO;
import cn.nju.edu.chemical_monitor_system.vo.ProductVO;

import java.util.List;
import java.util.Map;

public interface ExpressService {

    ExpressVO createExpress(int inputStoreId, int outputStoreId, Map<Integer, Double> productNumberMap);

    ExpressVO getExpress(int expressId);

    ExpressVO reverseExpress(int expressId);

    List<ExpressProductVO> getProductExpress(int productId);

    ExpressProductVO outputProduct(int expressId);

    ExpressProductVO inputProduct(int expressId);


}
