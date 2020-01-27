package cn.nju.edu.chemical_monitor_system.utils.safeu_util;

import lombok.Data;

@Data
public class Product {
    private Double[] nums;//存储药品各个维度的数据
    private boolean isAllocated;//判断是否已经被分配到质心
    private int productId;
    private int featureNums;

    public Product(int featureNums,int productId){
        this.featureNums=featureNums;
        this.nums=new Double[featureNums];
        this.productId=productId;
        this.isAllocated=false;
    }
    public void add(Product product){
        for(int i=0;i<featureNums;i++){
            Double[] temp=product.getNums();
            nums[i]+=temp[i];
        }
    }
    public void divide(int divisor) {
        for (int i = 0; i < featureNums; i++) {
            nums[i] /= divisor;
        }
    }

    @Override
    public boolean equals(Object o) {
        Product p=(Product)o;
        if(this.getProductId()==p.getProductId()){
            return true;
        }
        return false;
    }
    public double awayFrom(Product p){
        double distance=0;
        for(int i=0;i<featureNums;i++){
            distance+=Math.pow(nums[i]-p.getNums()[i],2);
        }
        return Math.sqrt(distance);
    }

}
