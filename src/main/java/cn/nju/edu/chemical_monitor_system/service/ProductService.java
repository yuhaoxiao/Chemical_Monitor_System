package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.vo.ProductVO;

public interface ProductService {

    ProductVO addProduct(int batchId, int casId, double number);

    ProductVO getProduct(int pid);

}
