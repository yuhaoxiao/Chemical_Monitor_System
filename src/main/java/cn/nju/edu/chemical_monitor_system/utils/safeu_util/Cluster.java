package cn.nju.edu.chemical_monitor_system.utils.safeu_util;

import lombok.Data;

@Data
public class Cluster {
    private ProductList products;
    private int featureNums;
    private Product center;
    public Cluster(Product product){
        this.center=product;
        products.add(product);
        this.featureNums=product.getFeatureNums();
    }
    public void add(Product p){
        p.setAllocated(true);
        products.add(p);
    }
    public void updateCenter(){
        Product newCenter=new Product(featureNums,center.getProductId());
        for(int i=0;i<products.size();i++){
            newCenter.add(products.get(i));
        }
        newCenter.divide(products.size());
        center=newCenter;
    }
    public void clear() {
        products.clearAllocated();
        products.clear();
    }

}
