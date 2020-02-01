package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.dao.BatchDao;
import cn.nju.edu.chemical_monitor_system.dao.CasDao;
import cn.nju.edu.chemical_monitor_system.dao.ProductDao;
import cn.nju.edu.chemical_monitor_system.entity.CasEntity;
import cn.nju.edu.chemical_monitor_system.entity.ProductEntity;
import cn.nju.edu.chemical_monitor_system.service.ProductService;
import cn.nju.edu.chemical_monitor_system.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private BatchDao batchDao;

    @Autowired
    private CasDao casDao;

    @Autowired
    private ProductDao productDao;

    @Override
    public ProductVO addProduct(int batchId, int casId, double number) {
        if (!batchDao.findById(batchId).isPresent()) {
            return new ProductVO("批次id不存在");
        }

        Optional<CasEntity> casOpt = casDao.findById(casId);
        if (!casOpt.isPresent()) {
            return new ProductVO("CAS id不存在");
        }

        List<ProductEntity> existed = productDao.findByBatchIdAndCasEntity(batchId, casOpt.get());
        if (existed != null && existed.size() != 0) {
            return new ProductVO("该批次已有本产品");
        }

        ProductEntity product = new ProductEntity();
        product.setBatchId(batchId);
        product.setCasEntity(casOpt.get());
        product.setNumber(number);
        productDao.saveAndFlush(product);
        return new ProductVO(product);
    }

    @Override
    public ProductVO getProduct(int pid) {
        Optional<ProductEntity> productOpt = productDao.findById(pid);

        if (!productOpt.isPresent()) {
            return new ProductVO("产品id不存在");
        }

        return new ProductVO(productOpt.get());
    }

}
