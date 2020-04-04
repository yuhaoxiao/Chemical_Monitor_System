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

    @GetMapping
    @RequiresRoles(logical = Logical.OR,value={"operator","administrator"})
    public BaseResponse getAll() {
        return new BaseResponse(200,"成功", storeService.getAll());
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

    @RequiresRoles(value={"administrator"})
    @PostMapping("/add_store")
    public BaseResponse addStore(@RequestBody StoreVO storeVO) {
        int eid = storeVO.getEnterpriseId();
        String name = storeVO.getName();
        return new BaseResponse(200, "success", storeService.addStore(eid, name));
    }

    @RequiresRoles(value={"administrator"})
    @PostMapping("/delete_store/{sid}")
    public BaseResponse deleteStore(@PathVariable int sid) {
        return new BaseResponse(200, "success", storeService.deleteStore(sid));
    }

    @RequiresRoles(value={"administrator"})
    @PostMapping("/update_store")
    public BaseResponse updateStore(@RequestBody StoreVO storeVO) {
        return new BaseResponse(200, "success", storeService.updateStore(storeVO));
    }

    @RequiresRoles(value={"administrator"})
    @GetMapping("/search_store/{s}")
    public BaseResponse searchStore(@PathVariable String s) {
        return new BaseResponse(200, "success", storeService.searchStore(s));
    }

    @GetMapping(value = "/search_store_by_enterprise/{eid}")
    public BaseResponse searchStoreByEnterprise(@PathVariable int eid){
        return new BaseResponse(200, "success", storeService.searchByEnterprise(eid));
    }

}
