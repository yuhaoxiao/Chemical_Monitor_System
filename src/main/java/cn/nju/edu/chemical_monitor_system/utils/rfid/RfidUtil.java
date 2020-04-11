package cn.nju.edu.chemical_monitor_system.utils.rfid;

import cn.nju.edu.chemical_monitor_system.constant.ExpressProductStatusEnum;
import cn.nju.edu.chemical_monitor_system.dao.ExpressDao;
import cn.nju.edu.chemical_monitor_system.entity.ExpressEntity;
import cn.nju.edu.chemical_monitor_system.entity.ExpressProductEntity;
import cn.nju.edu.chemical_monitor_system.entity.RfidInfoEntity;
import cn.nju.edu.chemical_monitor_system.service.RfidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class RfidUtil {

    @Autowired
    RfidService rfidService;

    @Autowired
    ExpressDao expressDao;

    class ReadRfidThread implements Callable<String> {
        String port;

        ReadRfidThread(String port) {
            this.port = port;
        }

        @Override
        public String call() throws Exception {
            String rfid = "-1";
            //返回结果为-1代表读取失败，继续读取
            while (true) {
                rfid = rfidService.readRfid(port);
                if (!rfid.equals("-1")) {
                    return rfid;
                }
                Thread.sleep(1000);
            }
        }
    }

    class WriteRfidThread implements Callable<String> {
        String rfid;
        String port;

        WriteRfidThread(String rfid, String port) {
            this.rfid = rfid;
            this.port = port;
        }

        @Override
        public String call() throws Exception {
            String newRfid;
            //返回结果为-1代表写入失败，继续写入
            while (true) {
                newRfid = rfidService.writeRfid(rfid, port);
                if (!newRfid.equals("-1")) {
                    return newRfid;
                }
                Thread.sleep(1000);
            }
        }
    }

    private String limitTaskTime(Callable<String> thread) {
        FutureTask<String> futureTask = new FutureTask<>(thread);
        new Thread(futureTask).start();
        String result = "-1";
        try {
            //设置超时时间4秒
            result = futureTask.get(4000, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            futureTask.cancel(true);
            ex.printStackTrace();
            return result;
        }
        return result;
    }

    public String read(String port) {
        boolean in=false;
        if(port.endsWith("i")){
            in=true;
        }
        int expressId=Integer.parseInt(port.substring(0,port.length()-1));
        String rfid="-1";
        int randomInt=new Random().nextInt(10);
        if(randomInt>=9){
            return rfid;
        }else{
            //模拟不在物流单的情况
            if(randomInt<=1){
                List<ExpressEntity> expressEntities = expressDao.findAll().stream().filter(e -> e.getExpressId() != expressId).collect(Collectors.toList());
                if(expressEntities.size()!=0){
                    ExpressEntity e = expressEntities.get(0);
                    RfidInfoEntity rfidInfoEntity = new RfidInfoEntity(e.getExpressProductEntities().get(0));
                    return rfidInfoEntity.toString();
                }
            }
            ExpressEntity express = expressDao.findFirstByExpressId(expressId);
            List<ExpressProductEntity> expressProductEntities;
            if(!in) {
                expressProductEntities = express.getExpressProductEntities().stream().filter(e -> e.getStatus() == ExpressProductStatusEnum.NOT_START.getCode()).collect(Collectors.toList());
            }else{
                expressProductEntities = express.getExpressProductEntities().stream().filter(e -> e.getStatus() == ExpressProductStatusEnum.OUT_INVENTORY.getCode()).collect(Collectors.toList());
            }
            ExpressProductEntity expressProductEntity = expressProductEntities.get(0);
            return new RfidInfoEntity(expressProductEntity).toString();
        }
        //return limitTaskTime(new ReadRfidThread(port));
    }

    public String write(String rfid, String port) {
        return limitTaskTime(new WriteRfidThread(rfid, port));
    }
}
