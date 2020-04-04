package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.vo.StoreVO;

import java.util.List;
import java.util.Map;

public interface StoreService {

    List<Integer> getAllStoreId();

    StoreVO getStoreById(int sid);

    Map<Integer,Double> getStoreProduct(int sid);

    StoreVO addStore(int eid, String name);

    StoreVO deleteStore(int sid);

    StoreVO updateStore(StoreVO storeVO);

    List<StoreVO> searchStore(String s);

    List<StoreVO> getAll();

    List<StoreVO> searchByEnterprise(int eid);
}
