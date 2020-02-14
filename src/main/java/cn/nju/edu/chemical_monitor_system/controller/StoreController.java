package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.service.StoreService;
import cn.nju.edu.chemical_monitor_system.vo.StoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class StoreController {

    @Autowired
    private StoreService storeService;

    @GetMapping("/store/get_all_storeId")
    public List<Integer> getAllStoreId() {
        return storeService.getAllStoreId();
    }

    @GetMapping("/store/get_store_byId")
    public StoreVO getStoreById(int sid) {
        return storeService.getStoreById(sid);
    }

    @GetMapping("/store/get_store_products")
    public Map<Integer, Double> getStoreProducts(int sid) {
        if (storeService.getStoreById(sid).getCode() == 0) {
            return null;
        }

        return storeService.getStoreProduct(sid);
    }

    @PostMapping("/store/add_store")
    public StoreVO addStore(int eid, String name) {
        return storeService.addStore(eid, name);
    }

    @PostMapping("/store/delete_store")
    public StoreVO deleteStore(int sid) {
        return storeService.deleteStore(sid);
    }

    @PostMapping("/store/update_store")
    public StoreVO updateStore(StoreVO storeVO) {
        return storeService.updateStore(storeVO);
    }

    @GetMapping("/store/search_store")
    public List<StoreVO> searchStore(String s) {
        return storeService.searchStore(s);
    }

}
