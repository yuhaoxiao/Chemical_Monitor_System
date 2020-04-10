package cn.nju.edu.chemical_monitor_system.utils.safe;

import cn.nju.edu.chemical_monitor_system.constant.ConstantVariables;
import cn.nju.edu.chemical_monitor_system.entity.CasEntity;
import cn.nju.edu.chemical_monitor_system.entity.ProductEntity;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Data
@Component
public class Product {
    private double[] nums;//数值类型属性
    private boolean isAllocated;//判断是否已经被分配到质心
    private int productId;
    private int featureNums = ConstantVariables.featureNums;
    private String name;

    Product() {
    }

    Product(int productId){
        this.productId = productId;
        this.isAllocated = false;
        this.nums = new double[featureNums];
        this.name="center";
    }
    Product(ProductEntity productEntity) {
        this.nums = new double[featureNums];
        CasEntity cas = productEntity.getCasEntity();
        nums[0] = cas.getFusionPoint() / (double) ConstantVariables.maxFusionPoint;
        nums[1] = cas.getBoilingPoint() / (double) ConstantVariables.maxBoilingPoint;
        nums[2] = cas.getExistType() / (double) ConstantVariables.maxExistType;
        nums[3] = cas.getIsOrganic() / (double) ConstantVariables.maxIsOrganic;
        nums[4] = cas.getOxidation() / (double) ConstantVariables.maxOxidation;
        nums[5] = cas.getReducibility() / (double) ConstantVariables.maxReducibility;
        nums[6] = cas.getInflammability() / (double) ConstantVariables.maxInflammability;
        nums[7] = cas.getExplosion() / (double) ConstantVariables.maxExplosion;
        this.productId = productEntity.getProductId();
        this.isAllocated = false;
        this.name = cas.getName();
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

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
