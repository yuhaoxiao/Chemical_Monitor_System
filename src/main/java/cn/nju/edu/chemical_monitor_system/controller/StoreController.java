package cn.nju.edu.chemical_monitor_system.controller;

import cn.nju.edu.chemical_monitor_system.response.BaseResponse;
import cn.nju.edu.chemical_monitor_system.service.StoreService;
import cn.nju.edu.chemical_monitor_system.vo.StoreVO;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{storeId}/products")
    @RequiresRoles(logical = Logical.OR, value={"operator", "administrator"})
    public BaseResponse getAllProducts(@PathVariable int storeId) {  // 获取目前在这个仓库的所有产品；产品数量为仓库中的数量
        return new BaseResponse(200, "success", storeService.getAllStoreProducts(storeId));
    }

    @GetMapping("/search_by_cas/{casId}")
    @RequiresRoles(logical = Logical.OR, value={"operator", "administrator"})
    public BaseResponse searchStoresByCAS(@PathVariable int casId) {  // 查询目前有这个化学品的仓库
        return new BaseResponse(200, "success", storeService.searchStoresByCAS(casId));
    }

    @GetMapping("/{storeId}/search_by_cas/{casId}")
    @RequiresRoles(logical = Logical.OR, value={"operator", "administrator"})
    public BaseResponse searchProductsByStoreAndCAS(@PathVariable int storeId, @PathVariable int casId) {  //  获取目前在这个仓库该化学品的所有批次产品；产品数量为仓库中的数量
        return new BaseResponse(200, "success", storeService.searchProductsByStoreAndCAS(storeId, casId));
    }

    @GetMapping("/get_all_storeId")
    public List<Integer> getAllStoreId() {  //  暂时没用到
        return storeService.getAllStoreId();
    }

    @GetMapping("/get_store_byId")
    public StoreVO getStoreById(int sid) {  //  暂时没用到
        return storeService.getStoreById(sid);
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
