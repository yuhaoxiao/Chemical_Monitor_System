package cn.nju.edu.chemical_monitor_system.service;

import cn.nju.edu.chemical_monitor_system.entity.RfidInfoEntity;

public interface RfidService {

    RfidInfoEntity readRfid(String port);//-1表示没有读成功

    String writeRfid(RfidInfoEntity rfidInfo, String port);//-1表示没有写成功
}
