package cn.nju.edu.chemical_monitor_system.utils.rfid;

import cn.nju.edu.chemical_monitor_system.service.impl.RXTXListenerImpl;
import com.module.interaction.RXTXListener;
import com.module.interaction.ReaderHelper;
import com.rfid.RFIDReaderHelper;
import com.rfid.ReaderConnector;
import com.rfid.rxobserver.RXObserver;
import com.rfid.rxobserver.bean.RXInventoryTag;
import com.util.StringTool;

import java.util.Arrays;
import java.util.Observer;

public class RfidDeviceUtil {
    private static ReaderConnector mConnector;
    private static ReaderHelper mReaderHelper;
    private static byte insertLocation = (byte)0x02;

    static Observer mObserver = new RXObserver() {
        @Override
        protected void onInventoryTag(RXInventoryTag tag) {
            System.out.println("EPC data:" + tag.strEPC);
        }

        @Override
        protected void onInventoryTagEnd(RXInventoryTag.RXInventoryTagEnd endTag) {
            System.out.println("inventory end:" + endTag.mTotalRead);
            ((RFIDReaderHelper) mReaderHelper).realTimeInventory((byte) 0xff,(byte)0x01);
        }

        @Override
        protected void onExeCMDStatus(byte cmd,byte status) {
            System.out.format("CDM:%s  Execute status:%S",
                    String.format("%02X",cmd),String.format("%02x", status));
        }

    };

    static Observer mObserver1 = new RXObserver() {
        @Override
        protected void onInventoryTag(RXInventoryTag tag) {
            System.out.println("EPC data:" + tag.strEPC);
        }

        @Override
        protected void onInventoryTagEnd(RXInventoryTag.RXInventoryTagEnd endTag) {
            System.out.println("inventory end:" + endTag.mTotalRead);
            ((RFIDReaderHelper) mReaderHelper).realTimeInventory((byte) 0xff,(byte)0x01);
        }

        @Override
        protected void onExeCMDStatus(byte cmd,byte status) {
            System.out.format("CDM:%s  Execute status:%S",
                    String.format("%02X",cmd),String.format("%0sendData2x", status));
        }
    };

    static RXTXListener mListener = new RXTXListener() {
        @Override
        public void reciveData(byte[] btAryReceiveData) {
            // TODO Auto-generated method stub
            System.out.println("reciveData" + StringTool.byteArrayToString(btAryReceiveData, 0, btAryReceiveData.length));
        }

        @Override
        public void sendData(byte[] btArySendData) {
            // TODO Auto-generated method stub
            System.out.println("sendData" + StringTool.byteArrayToString(btArySendData, 0, btArySendData.length));
        }

        @Override
        public void onLostConnect() {
            // TODO Auto-generated method stub
        }

    };

    public static void setConnector(String port, int buad){
        if (mConnector == null){
            mConnector = new ReaderConnector();
            mReaderHelper = mConnector.connectCom(port, buad);
            System.out.println("RFID Device connect by port " + port);
        }
    }

    public static String readEPC(){
        String data = "";
        if(mReaderHelper != null) {
            System.out.println("Read Connect success!");
            try {
                RXTXListenerImpl rxtxListener = new RXTXListenerImpl();
                mReaderHelper.setRXTXListener(rxtxListener);

                // read tag
                //public int ReadTag(byte btReadId, byte btMemBank, byte btWordAdd, byte btWordCnt)
                //btReadId - 读写器的地址(0xFF是公用地址)
                //btMemBank - 标签存储区域(0x00:RESERVED, 0x01:EPC, 0x02:TID, 0x03:USER)
                //btWordAdd - 读取数据首地址,取值范围参考标签规格。
                //btWordCnt - 字长，WORD(16 bits)长度。取值范围请参考标签规格书
                //Succeeded :0, Failed:-1
                System.out.println("isReadSuccess:"+((RFIDReaderHelper) mReaderHelper).readTag((byte)0xff,(byte)0x01,(byte)0x02,(byte)0x00,null));
                Thread.currentThread().sleep(2000);
                data = RXTXListenerImpl.str;
                while (data.length() <50 ){
                    Thread.currentThread().sleep(2000);
                    data = RXTXListenerImpl.str;
                }


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            System.out.println("Read Connect faild!");
            mConnector.disConnect();
        }

        System.out.println(data.substring(24, 59));
        String result = hex2String(data.substring(24, 59));
        return result;
    }

    public static String readUSER(){
        String data = "";
        if(mReaderHelper != null) {
            System.out.println("Read Connect success!");
            try {
                RXTXListenerImpl rxtxListener = new RXTXListenerImpl();
                mReaderHelper.setRXTXListener(rxtxListener);

                // read tag
                //public int ReadTag(byte btReadId, byte btMemBank, byte btWordAdd, byte btWordCnt)
                //btReadId - 读写器的地址(0xFF是公用地址)
                //btMemBank - 标签存储区域(0x00:RESERVED, 0x01:EPC, 0x02:TID, 0x03:USER)
                //btWordAdd - 读取数据首地址,取值范围参考标签规格。
                //btWordCnt - 字长，WORD(16 bits)长度。取值范围请参考标签规格书
                //Succeeded :0, Failed:-1
                System.out.println("isReadSuccess:"+((RFIDReaderHelper) mReaderHelper).readTag((byte)0xff,(byte)0x03,(byte)0x00,(byte)0x00,null));
                Thread.currentThread().sleep(2000);
                data = RXTXListenerImpl.str;
                while (data.length() <50 ){
                    Thread.currentThread().sleep(2000);
                    data = RXTXListenerImpl.str;
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            System.out.println("Read Connect faild!");
            mConnector.disConnect();
        }

        System.out.println(data.substring(66,113));
        String result = hex2String(data.substring(66,113));
        return result;
    }

    public static boolean  writeEPC(String rfid_str, int size){

        if(mReaderHelper != null) {
            System.out.println("Write Connect success!");
            try {
                byte[] psw = {(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
                byte[] data = new byte[size];
                data = rfid_str.getBytes();

                System.out.println(Arrays.toString(data));
                //public int WriteTag(byte btReadId, byte[] btAryPassWord, byte btMemBank, byte btWordAdd, byte btWordCnt, byte[] btAryData)
                //btReadId - 读写器的地址(0xFF是公用地址)
                //btAryPassWord - 密码, 4 bytes.
                //btMemBank - 标签存储区域(0x00:RESERVED, 0x01:EPC, 0x02:TID, 0x03:USER)
                //btWordAdd - 数据首地址,WORD(16 bits)地址。写入EPC存储区域一般从02开始，该区域前四个字节存放PC+CRC。
                //btWordCnt - 写入的字长度WORD(16 bits), WORD(16 bits)长度，数值请参考标签规格。
                //btAryData - Write data, btWordCnt*2 bytes.
                //Succeeded :0, Failed:-1
                boolean success=((RFIDReaderHelper) mReaderHelper).writeTag((byte)0xff,psw,(byte)0x01,(byte)0x02,int2ByteArray(size),data)==0;
                System.out.println("isWriteSuccess:"+success);
                return success;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            System.out.println("Write Connect faild!");
            mConnector.disConnect();
            return false;
        }
    }


    public static boolean writeUSER(String rfid_str, int size){

        if(mReaderHelper != null) {
            System.out.println("Write Connect success!");
            try {
                byte[] psw = {(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
                byte[] data = new byte[size];
                data = rfid_str.getBytes();

                System.out.println(Arrays.toString(data));
                //public int WriteTag(byte btReadId, byte[] btAryPassWord, byte btMemBank, byte btWordAdd, byte btWordCnt, byte[] btAryData)
                //btReadId - 读写器的地址(0xFF是公用地址)
                //btAryPassWord - 密码, 4 bytes.
                //btMemBank - 标签存储区域(0x00:RESERVED, 0x01:EPC, 0x02:TID, 0x03:USER)
                //btWordAdd - 数据首地址,WORD(16 bits)地址。写入EPC存储区域一般从02开始，该区域前四个字节存放PC+CRC。
                //btWordCnt - 写入的字长度WORD(16 bits), WORD(16 bits)长度，数值请参考标签规格。
                //btAryData - Write data, btWordCnt*2 bytes.
                //Succeeded :0, Failed:-1
                boolean result= ((RFIDReaderHelper) mReaderHelper).writeTag((byte) 0xff, psw, (byte) 0x03, (byte) 0x00, int2ByteArray(size), data) == 0;
                System.out.println("isWriteSuccess:"+result);
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            System.out.println("Write Connect faild!");
            mConnector.disConnect();
            return false;
        }
    }

    public static int reset(){
        int result = 0;
        if(mReaderHelper != null) {
            System.out.println("Reset Connect success!");
            try {
                byte[] psw = {(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
                byte[] data = {(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
                result =  ((RFIDReaderHelper) mReaderHelper).writeTag((byte)0xff,psw,(byte)0x01,(byte)0x02,(byte)0x06,data);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            System.out.println("Reset Connect faild!");
            mConnector.disConnect();
        }
        return result;
    }


    public static byte int2ByteArray(int i){
        byte[] result=new byte[4];
        result[0]=(byte)((i >> 24)& 0xFF);
        result[1]=(byte)((i >> 16)& 0xFF);
        result[2]=(byte)((i >> 8)& 0xFF);
        result[3]=(byte)(i & 0xFF);
        return result[3];
    }

    public static String hex2String(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "gbk");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public static String stringToHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

}
