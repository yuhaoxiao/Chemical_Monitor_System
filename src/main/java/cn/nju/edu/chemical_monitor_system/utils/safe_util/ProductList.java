package cn.nju.edu.chemical_monitor_system.utils.safe_util;

import cn.nju.edu.chemical_monitor_system.entity.ProductEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProductList implements Iterable<Product> {
    private List<Product> productList = new ArrayList<>();

    ProductList() {
    }

    ProductList(List<ProductEntity> productEntities) {
        productList = productEntities.stream().map(Product::new).collect(Collectors.toList());
    }

    void add(Product p) {
        productList.add(p);
    }

    int size() {
        return productList.size();
    }

    Product get(int index) {
        return productList.get(index);
    }

    //把所有节点重新标记为未分配
    void clearAllocated() {
        for (Product product : productList) {
            product.setAllocated(false);
        }
    }

    void addAll(List<Product> products) {
        productList.addAll(products);
    }

    void clear() {
        productList.clear();
    }

    boolean contains(Product product) {
        return productList.contains(product);
    }

    @Override
    public Iterator<Product> iterator() {
        return productList.iterator();
    }
}
