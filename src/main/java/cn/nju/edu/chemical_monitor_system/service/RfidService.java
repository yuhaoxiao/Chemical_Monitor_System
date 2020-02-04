package cn.nju.edu.chemical_monitor_system.service;

public interface RfidService {

    int readRfid();

    void writeRfid(int productId);
}
