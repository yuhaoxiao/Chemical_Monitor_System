package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.service.RfidService;
import cn.nju.edu.chemical_monitor_system.utils.rfid.RfidDeviceUtil;
import org.springframework.stereotype.Service;

@Service
public class RfidServiceImpl implements RfidService {
    @Override
    public String readRfid(String port) {
        RfidDeviceUtil.setConnector("COM3", 115200);
        String dataUSER = RfidDeviceUtil.readUSER();
        String dataEPC = RfidDeviceUtil.readEPC();
        return dataUSER + dataEPC;
    }

    @Override
    public String writeRfid(String rfid, String port) {
        RfidDeviceUtil.setConnector("COM3", 115200);

        String testUSER = rfid.substring(0, 16);

        String testEPC = rfid.substring(16, 28);

        boolean success1 = RfidDeviceUtil.writeUSER(testUSER, 16);

        boolean success2 = RfidDeviceUtil.writeEPC(testEPC, 12);

        if (success1 && success2) {
            return rfid;
        }
        return "-1";
    }
}
