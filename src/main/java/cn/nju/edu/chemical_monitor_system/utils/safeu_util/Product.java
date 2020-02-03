package cn.nju.edu.chemical_monitor_system.utils.safeu_util;

import cn.nju.edu.chemical_monitor_system.constant.ConstantVariables;
import cn.nju.edu.chemical_monitor_system.dao.ProductDao;
import cn.nju.edu.chemical_monitor_system.entity.CasEntity;
import cn.nju.edu.chemical_monitor_system.entity.ProductEntity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

@Data
public class Product {
    private double[] nums;//数值类型属性
    private Boolean[] booleans;//bool类型属性
    private boolean isAllocated;//判断是否已经被分配到质心
    private int productId;
    private int featureNums = ConstantVariables.featureNums;
    private String name;

    @Autowired
    private ProductDao productDao;

    Product() {
    }

    Product(int productId) {
        ProductEntity productEntity = productDao.findById(productId).get();
        this.nums = new double[featureNums];
        CasEntity cas = productEntity.getCasEntity();
        nums[0] = cas.getFusionPoint();
        nums[1] = cas.getBoilingPoint();
        nums[2] = cas.getExistType();
        nums[3] = cas.getIsOrganic();
        nums[4] = cas.getOxidation();
        nums[5] = cas.getReducibility();
        nums[6] = cas.getInflammability();
        nums[7] = cas.getExplosion();
        this.productId = productEntity.getProductId();
        this.isAllocated = false;
        this.name = productEntity.getCasEntity().getName();
    }

    void add(Product product) {
        for (int i = 0; i < featureNums; i++) {
            double[] temp = product.getNums();
            nums[i] += temp[i];
        }
    }

    void divide(int divisor) {
        for (int i = 0; i < featureNums; i++) {
            nums[i] /= divisor;
        }
    }

    //计算两个节点之间的距离公式，目前先简单采用欧几里得公式计算
    double awayFrom(Product p) {
        double distance = 0;
        for (int i = 0; i < featureNums; i++) {
            double[] nums2 = p.getNums();
            distance += Math.pow(nums[i] - nums2[i], 2);
        }
        return Math.sqrt(distance);
    }

    @Override
    public boolean equals(Object o) {
        Product p = (Product) o;
        return this.getProductId() == p.getProductId();
    }

    @Override
    public String toString() {
        return Arrays.toString(nums);
    }

}
