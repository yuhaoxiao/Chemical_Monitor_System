package cn.nju.edu.chemical_monitor_system.utils.safeu_util;

import cn.nju.edu.chemical_monitor_system.entity.ProductEntity;
import lombok.Data;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProductList implements Iterable<Product> {
    private  List<Product> productList;
    public ProductList(List<ProductEntity> productEntities,int featureNums){
        productList=productEntities.stream().map(productEntity -> {
            Product product=new Product();
            Double[] nums=new Double[featureNums];

            nums[0]=productEntity.getNumber();
            nums[1]=productEntity.getNumber();//这里把各种属性赋值

            product.setNums(nums);
            product.setAllocated(false);
            product.setProductId(productEntity.getProductId());
            return product;
        }).collect(Collectors.toList());
    }
    public void add(Product p){
        productList.add(p);
    }
    public int size(){
        return productList.size();
    }
    public Product get(int index){
        return productList.get(index);
    }

    //把所有节点重新标记为未分配
    public void clearAllocated(){
        for(Product product:productList){
            product.setAllocated(false);
        }
    }
    public void clear(){
        productList.clear();
    }
    public boolean contains(Product product){
        return productList.contains(product);
    }
    @Override
    public Iterator<Product> iterator() {
        return productList.iterator();
    }
}
