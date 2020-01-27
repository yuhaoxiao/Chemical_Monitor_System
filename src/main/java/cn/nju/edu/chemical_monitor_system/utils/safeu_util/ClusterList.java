package cn.nju.edu.chemical_monitor_system.utils.safeu_util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClusterList implements Iterable<Cluster>{
    private List<Cluster> clusters=new ArrayList<>();

    //寻找距离某个节点最近的簇
    public Cluster findNearestCluster(DistanceMetric distanceMetric, Product product){
        Cluster nearest=null;
        double nearestDistance=Double.MIN_VALUE;
        for(Cluster cluster:clusters){
            double distance=distanceMetric.calculateDistance(product,cluster);
            if(nearestDistance>distance){
                nearestDistance=distance;
                nearest=cluster;
            }
        }
        return nearest;
    }

    //返回距离所有簇中最远的一个节点
    public Product findFurthestProduct(DistanceMetric distanceMetric,ProductList products){
        Product furthest=null;
        double furthestDistance=Double.MAX_VALUE;
        for(Product product:products){
            double distance=distanceMetric.calculateDistance(product,this);
            if(furthestDistance<distance){
                furthestDistance=distance;
                furthest=product;
            }
        }
        return furthest;
    }

    public void add(Cluster cluster){
        clusters.add(cluster);
    }
    public int size(){
        return clusters.size();
    }
    public void updateCenter(){
        for(Cluster cluster:clusters){
            cluster.updateCenter();
        }
    }
    public void clear() {
        for (Cluster cluster : clusters) {
            cluster.clear();
        }
    }
    public Cluster get(int i){
        return clusters.get(i);
    }
    @Override
    public Iterator<Cluster> iterator() {
        return clusters.iterator();
    }
}
