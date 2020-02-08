package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.entity.RfidInfoEntity;
import cn.nju.edu.chemical_monitor_system.service.RfidService;
import org.springframework.stereotype.Service;

@Service
public class RfidServiceImpl implements RfidService {
    @Override
    public RfidInfoEntity readRfid(String port) {
        return null;
    }

    @Override
    public String writeRfid(RfidInfoEntity rfidInfo, String port) {
        return null;
    }
}
