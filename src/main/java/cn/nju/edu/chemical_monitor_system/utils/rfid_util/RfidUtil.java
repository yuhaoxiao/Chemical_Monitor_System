package cn.nju.edu.chemical_monitor_system.utils.rfid_util;

import cn.nju.edu.chemical_monitor_system.service.RfidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@Component
public class RfidUtil {
    @Autowired
    RfidService rfidService;

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
        return limitTaskTime(new ReadRfidThread(port));
    }

    public String write(String rfid, String port) {
        return limitTaskTime(new WriteRfidThread(rfid, port));
    }
}
