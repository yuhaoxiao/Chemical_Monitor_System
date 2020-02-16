package cn.nju.edu.chemical_monitor_system.utils.safe;

import cn.nju.edu.chemical_monitor_system.constant.ConstantVariables;
import lombok.Data;

import java.util.List;

@Data
class Cluster {
    private ProductList products = new ProductList();
    private static int featureNums = ConstantVariables.featureNums;
    private Product center;

    Cluster() {
    }

    Cluster(Product product) {
        this.center = product;
        products.add(product);
    }

    void add(Product p) {
        p.setAllocated(true);
        products.add(p);
    }

    void addAll(List<Product> ps) {
        products.addAll(ps);
    }

    //更新质心节点
    void updateCenter() {
        Product newCenter = new Product(center.getProductId());
        for (int i = 0; i < products.size(); i++) {
            newCenter.add(products.get(i));
        }
        newCenter.divide(products.size());
        center = newCenter;
    }

    void clear() {
        products.clearAllocated();
        products.clear();
    }

}
