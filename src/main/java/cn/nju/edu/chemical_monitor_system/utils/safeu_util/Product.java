package cn.nju.edu.chemical_monitor_system.utils.safeu_util;

import cn.nju.edu.chemical_monitor_system.constant.ConstantVariables;
import cn.nju.edu.chemical_monitor_system.entity.ProductEntity;
import lombok.Data;

import java.util.Arrays;

@Data
public class Product {
    private double[] nums;//数值类型属性
    private Boolean[] booleans;//bool类型属性
    private boolean isAllocated;//判断是否已经被分配到质心
    private int productId;
    private int featureNums = ConstantVariables.featureNums;
    private String name;

    Product() {
    }

    Product(ProductEntity productEntity) {
        this.nums = new double[featureNums];
        nums[0] = Math.random() * 100;
        nums[1] = Math.random() * 100;
        nums[2] = Math.random() * 100;
        this.productId = productEntity.getProductId();
        this.isAllocated = false;
        this.name = productEntity.getCasEntity().getName();
    }

    Product(int productId, String name) {
        this.nums = new double[this.featureNums];
        this.productId = productId;
        this.isAllocated = false;
        this.name = name;
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
