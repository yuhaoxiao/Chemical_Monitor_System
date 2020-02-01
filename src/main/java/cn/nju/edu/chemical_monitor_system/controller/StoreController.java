package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.service.StoreService;
import cn.nju.edu.chemical_monitor_system.vo.StoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class StoreController {

    @Autowired
    private StoreService storeService;

    @GetMapping("store/get_all_storeId")
    public List<Integer> getAllStoreId() {
        return storeService.getAllStoreId();
    }

    @GetMapping("store/get_store")
    public StoreVO getStore(int sid) {
        return storeService.getStore(sid);
    }

    @GetMapping("store/get_store_products")
    public Map<Integer, Double> getStoreProducts(int sid) {
        if (storeService.getStore(sid).getCode() == 0) {
            return null;
        }

        return storeService.getStoreProduct(sid);
    }


}
