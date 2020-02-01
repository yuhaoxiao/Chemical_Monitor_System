package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.vo.StoreVO;

import java.util.List;
import java.util.Map;

public interface StoreService {

    List<Integer> getAllStoreId();

    StoreVO getStore(int sid);

    Map<Integer,Double> getStoreProduct(int sid);

}
