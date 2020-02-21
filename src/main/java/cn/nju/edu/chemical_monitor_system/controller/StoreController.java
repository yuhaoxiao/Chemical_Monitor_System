package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.response.BaseResponse;
import cn.nju.edu.chemical_monitor_system.service.StoreService;
import cn.nju.edu.chemical_monitor_system.vo.ProductVO;
import cn.nju.edu.chemical_monitor_system.vo.StoreVO;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreService storeService;

    //以下接口需要实现
    @GetMapping
    @RequiresRoles(logical = Logical.OR,value={"operator","administrator"})
    public BaseResponse getAll() {
        List<StoreVO> list = new ArrayList<>();
        return new BaseResponse(200,"成功",list);
    }

    @GetMapping("/{storeId}/products") // 获取目前在这个仓库的所有产品
    @RequiresRoles(logical = Logical.OR,value={"operator","administrator"})
    public BaseResponse getAllProducts(@PathVariable int storeId) {
        List<ProductVO> list = new ArrayList<>();
        return new BaseResponse(200,"success",list);
    }

    @GetMapping("/search_by_cas/{casId}") // 查询目前有这个化学品的仓库
    @RequiresRoles(logical = Logical.OR,value={"operator","administrator"})
    public BaseResponse searchStoresByCAS(@PathVariable int casId) {  // 化学品casId
        List<StoreVO> list = new ArrayList<>();
        return new BaseResponse(200,"success",list);
    }

    @GetMapping("/{storeId}/search_by_cas/{casId}") // 获取目前在这个仓库该化学品的所有批次产品
    @RequiresRoles(logical = Logical.OR,value={"operator","administrator"})
    public BaseResponse searchProductsByStoreAndCAS(@PathVariable int storeId, @PathVariable int casId) {
        List<ProductVO> list = new ArrayList<>();
        return new BaseResponse(200,"success",list);
    }




    //以下接口暂时不需要
    @GetMapping("/get_all_storeId")
    public List<Integer> getAllStoreId() {
        return storeService.getAllStoreId();
    }

    @GetMapping("/get_store_byId")
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

    @PostMapping("/add_store")
    public StoreVO addStore(int eid, String name) {
        return storeService.addStore(eid, name);
    }

    @PostMapping("/delete_store")
    public StoreVO deleteStore(int sid) {
        return storeService.deleteStore(sid);
    }

    @PostMapping("/update_store")
    public StoreVO updateStore(StoreVO storeVO) {
        return storeService.updateStore(storeVO);
    }

    @GetMapping("/search_store")
    public List<StoreVO> searchStore(String s) {
        return storeService.searchStore(s);
    }

}
