package cn.nju.edu.chemical_monitor_system.utils.safeu_util;

import cn.nju.edu.chemical_monitor_system.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;

public class SafeUtil {
    public List<Product> getSafeProducts(int productId, List<ProductEntity> productEntities,int featureNums){
        ProductList products=new ProductList(productEntities,featureNums);
        DistanceMetric distanceMetric=new DistanceMetric();
        int iterationNum=100;
        KMeans k=new KMeans(distanceMetric,iterationNum);
        ClusterList clusters=k.runKMeans(products,featureNums);
        Product p=new Product();
        p.setProductId(productId);
        for(int i=0;i<clusters.size();i++){
            ProductList pList=clusters.get(i).getProducts();
            if(pList.contains(p)){
                return pList.getProductList();
            }
        }
        return new ArrayList<>();
    }
}
