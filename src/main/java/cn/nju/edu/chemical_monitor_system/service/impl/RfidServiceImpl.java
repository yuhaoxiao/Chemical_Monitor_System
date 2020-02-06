package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.service.RfidService;
import org.springframework.stereotype.Service;

@Service
public class RfidServiceImpl implements RfidService {
    @Override
    public String readRfid(String port) {
        //模拟概率成功
        double a = Math.random();
        return a > 0.8 ? "131" : "-1";
    }

    @Override
    public String writeRfid(String rfid, String port) {
        double a = Math.random();
        return a > 0.99 ? "131" : "-1";
    }
}
