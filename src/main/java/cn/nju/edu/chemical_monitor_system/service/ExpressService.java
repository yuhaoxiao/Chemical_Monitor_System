package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.vo.ExpressProductVO;
import cn.nju.edu.chemical_monitor_system.vo.ExpressVO;

import java.util.List;
import java.util.Map;

public interface ExpressService {

    ExpressVO createExpress(int inputStoreId, int outputStoreId, Map<Integer, Double> productNumberMap);

    ExpressVO inputExpress(int expressId, int userId);

    ExpressVO outputExpress(int expressId, int userId);

    ExpressVO getExpress(int expressId);

    List<ExpressProductVO> getProductExpress(int productId);

}
