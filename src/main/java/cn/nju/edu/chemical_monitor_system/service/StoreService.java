package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.vo.ProductVO;
import cn.nju.edu.chemical_monitor_system.vo.StoreVO;

import java.util.List;
import java.util.Map;

public interface StoreService {

    List<Integer> getAllStoreId();

    StoreVO getStoreById(int sid);

    StoreVO addStore(int eid, String name);

    StoreVO deleteStore(int sid);

    StoreVO updateStore(StoreVO storeVO);

    List<StoreVO> searchStore(String s);

    List<StoreVO> getAll();

    List<StoreVO> searchByEnterprise(int eid);

    List<ProductVO> getAllStoreProducts(int storeId);

    List<StoreVO> searchStoresByCAS(int casId);

    List<ProductVO> searchProductsByStoreAndCAS(int storeId, int casId);
}
