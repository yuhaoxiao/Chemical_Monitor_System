package cn.nju.edu.chemical_monitor_system.service.impl;

import com.module.interaction.RXTXListener;
import com.util.StringTool;

public class RXTXListenerImpl implements RXTXListener {
    public static String str = "";

    @Override
    public void reciveData(byte[] btAryReceiveData) {
        System.out.println("reciveData: " + StringTool.byteArrayToString(btAryReceiveData, 0, btAryReceiveData.length));
        str = StringTool.byteArrayToString(btAryReceiveData, 0, btAryReceiveData.length);
    }

    @Override
    public void sendData(byte[] btArySendData) {
        System.out.println("sendData: " + StringTool.byteArrayToString(btArySendData, 0, btArySendData.length));
    }

    @Override
    public void onLostConnect() {

    }

}
