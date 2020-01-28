package cn.nju.edu.chemical_monitor_system.utils.safeu_util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClusterList implements Iterable<Cluster> {
    private List<Cluster> clusters = new ArrayList<>();

    //寻找距离某个节点最近的簇
    Cluster findNearestCluster(Distance distanceMetric, Product product) {
        Cluster nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        for (Cluster cluster : clusters) {
            double distance = distanceMetric.calculateDistance(product, cluster);
            if (nearestDistance > distance) {
                nearestDistance = distance;
                nearest = cluster;
            }
        }
        return nearest;
    }

    //返回距离所有簇中最远的一个节点
    Product findFurthestProduct(Distance distanceMetric, ProductList products) {
        Product furthest = null;
        double furthestDistance = Double.MIN_VALUE;
        for (Product product : products) {
            double distance = distanceMetric.calculateDistance(product, this);
            if (furthestDistance < distance) {
                furthestDistance = distance;
                furthest = product;
            }
        }
        return furthest;
    }

    void add(Cluster cluster) {
        clusters.add(cluster);
    }

    int size() {
        return clusters.size();
    }

    void updateCenter() {
        for (Cluster cluster : clusters) {
            cluster.updateCenter();
        }
    }

    void clear() {
        for (Cluster cluster : clusters) {
            cluster.clear();
        }
    }

    Cluster get(int i) {
        return clusters.get(i);
    }

    List<Product> getClusterByProductId(int productId) {
        Product p = new Product();
        p.setProductId(productId);
        for (Cluster cluster : clusters) {
            ProductList pList = cluster.getProducts();
            if (pList.contains(p)) {
                return pList.getProductList();
            }
        }
        return new ArrayList<>();
    }

    @Override
    public Iterator<Cluster> iterator() {
        return clusters.iterator();
    }
}
