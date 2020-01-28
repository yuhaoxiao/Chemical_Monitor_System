package cn.nju.edu.chemical_monitor_system.utils.safeu_util;


class Distance {
    private double calculateDistance(Product p1, Product p2) {
        return p1.awayFrom(p2);
    }

    double calculateDistance(Product p, Cluster cluster) {
        return calculateDistance(p, cluster.getCenter());
    }

    double calculateDistance(Product p, ClusterList clusterList) {
        double min = Double.MAX_VALUE;
        for (Cluster cluster : clusterList) {
            min = Math.min(min, calculateDistance(p, cluster));
        }
        return min;
    }
}
