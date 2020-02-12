package cn.nju.edu.chemical_monitor_system.utils.safe_util;

import cn.nju.edu.chemical_monitor_system.constant.ConstantVariables;
import cn.nju.edu.chemical_monitor_system.dao.ProductDao;
import cn.nju.edu.chemical_monitor_system.entity.CasEntity;
import cn.nju.edu.chemical_monitor_system.entity.ProductEntity;
import cn.nju.edu.chemical_monitor_system.service.ProductService;
import cn.nju.edu.chemical_monitor_system.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SafeUtil {

    @Autowired
    StoreService storeService;

    @Autowired
    ProductService productService;

    @Autowired
    ProductDao productDao;

    public static void main(String[] args) {
        List<ProductEntity> productEntities = new ArrayList<>();
        CasEntity c1 = new CasEntity();
        c1.setName("product0");
        ProductEntity p1 = new ProductEntity();
        p1.setCasEntity(c1);
        p1.setProductId(0);
        for (int i = 1; i < 10; i++) {
            ProductEntity p = new ProductEntity();
            p.setProductId(i);
            CasEntity c = new CasEntity();
            c.setName("product" + i);
            p.setCasEntity(c);
            productEntities.add(p);
        }
        new SafeUtil().getSafeProducts(p1, productEntities);
    }

    public List<Product> getSafeProducts(ProductEntity inProduct, List<ProductEntity> productEntities) {
        int productId = inProduct.getProductId();
        productEntities.add(inProduct);
        ProductList products = new ProductList(productEntities);

        Distance distance = new Distance();
        int iterationNum = ConstantVariables.iterationNum;//设置迭代次数
        KMeans k = new KMeans(distance, iterationNum);
        ClusterList clusters = k.runKMeans(products);
        output(clusters);
        return clusters.getClusterByProductId(productId);
    }

    //控制台输出结果
    static void output(ClusterList clusterList) {
        int i = 0;
        for (Cluster cluster : clusterList) {
            System.out.print("Cluster" + i + ":" + cluster.getCenter() + "\n");
            for (Product doc : cluster.getProducts()) {
                System.out.print("\t" + doc.getProductId() + "\t" + doc + "\n");
            }
            i++;
        }
    }

    //入库是否安全
    public boolean isSafe(int productId, int storeId) {
        List<Integer> storeIds = storeService.getAllStoreId();
        Product inProduct = new Product(productDao.findById(productId).get());
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        double targetStoreDistance = 0;
        for (int i = 0; i < storeIds.size(); i++) {
            List<Integer> productIds = new ArrayList<>(storeService.getStoreProduct(storeIds.get(i)).keySet());
            List<Product> products = productIds.stream()
                    .map(id -> new Product(productDao.findById(id).get())).collect(Collectors.toList());
            Cluster cluster = new Cluster();
            cluster.addAll(products);
            cluster.updateCenter();
            double centerDistance = cluster.getCenter().awayFrom(inProduct);
            if (max < centerDistance) {
                max = centerDistance;
            }
            if (min > centerDistance) {
                min = centerDistance;
            }
            if (storeId == storeIds.get(i)) {
                targetStoreDistance = centerDistance;
            }
        }
        return targetStoreDistance <= (max + min) / 2;
    }
}
