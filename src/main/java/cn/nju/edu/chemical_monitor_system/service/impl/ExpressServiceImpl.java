package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.constant.ExpressProductStatusEnum;
import cn.nju.edu.chemical_monitor_system.constant.ExpressStatusEnum;
import cn.nju.edu.chemical_monitor_system.dao.*;
import cn.nju.edu.chemical_monitor_system.entity.*;
import cn.nju.edu.chemical_monitor_system.exception.ExpressException;
import cn.nju.edu.chemical_monitor_system.service.ExpressService;
import cn.nju.edu.chemical_monitor_system.utils.common.UserUtil;
import cn.nju.edu.chemical_monitor_system.utils.encryption.EncryptionUtil;
import cn.nju.edu.chemical_monitor_system.utils.kafka.KafkaUtil;
import cn.nju.edu.chemical_monitor_system.utils.rfid.RfidUtil;
import cn.nju.edu.chemical_monitor_system.utils.safe.SafeUtil;
import cn.nju.edu.chemical_monitor_system.vo.ExpressProductVO;
import cn.nju.edu.chemical_monitor_system.vo.ExpressVO;
import cn.nju.edu.chemical_monitor_system.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpressServiceImpl implements ExpressService {

    @Autowired
    private ExpressDao expressDao;

    @Autowired
    private ExpressProductDao expressProductDao;

    @Autowired
    private StoreDao storeDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private StoreProductDao storeProductDao;


    @Autowired
    private SafeUtil safeUtil;

    @Autowired
    EncryptionUtil encryptionUtil;

    @Autowired
    private RfidUtil rfidUtil;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private KafkaUtil kafkaUtil;

    @Override
    public ExpressVO createExpress(int inputStoreId, int outputStoreId, Map<Integer, Double> productNumberMap) {
        ExpressEntity expressEntity = new ExpressEntity();
        Optional<StoreEntity> inputStoreOpt = storeDao.findById(inputStoreId);
        Optional<StoreEntity> outputStoreOpt = storeDao.findById(outputStoreId);

        if (!inputStoreOpt.isPresent()) {
            throw new ExpressException("入库仓库id不存在");
        }

        if (!outputStoreOpt.isPresent()) {
            throw new ExpressException("出库仓库id不存在");
        }

        List<Integer> notSafeProducts=new ArrayList<>();
        for(Integer productId:productNumberMap.keySet()){
            if(!safeUtil.isSafe(productId,inputStoreId)){
                notSafeProducts.add(productId);
            }
        }
        if(notSafeProducts.size()!=0){
            StringBuilder s=new StringBuilder();
            s.append("标号为");
            for(Integer productId:notSafeProducts){
                s.append(productId);
                s.append(" ");
            }
            s.append("的商品不能在").append(inputStoreOpt.get().getName()).append("仓库存放");
            ExpressVO result=new ExpressVO();
            result.setCode(0);
            result.setMessage(s.toString());
            return result;
        }


        expressEntity.setInputStoreId(inputStoreId);
        expressEntity.setOutputStoreId(outputStoreId);
        expressEntity.setStatus(ExpressStatusEnum.NOT_START.getCode());

        List<ExpressProductEntity> expressProductEntities = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : productNumberMap.entrySet()) {
            ExpressProductEntity ep = new ExpressProductEntity();
            Optional<ProductEntity> productOpt = productDao.findById(entry.getKey());

            if (!productOpt.isPresent()) {
                continue;
            }
            ep.setProductId(entry.getKey());
            ep.setExpressEntity(expressEntity);
            ep.setStatus(ExpressProductStatusEnum.NOT_START.getCode());
            ep.setNumber(entry.getValue());
            expressProductEntities.add(ep);
        }

        expressEntity.setExpressProductEntities(expressProductEntities);
        expressDao.save(expressEntity);
        ExpressVO expressVO = new ExpressVO(expressEntity);
        kafkaUtil.sendExpress(expressVO);
        return expressVO;
    }

    private void inputExpress(ExpressEntity expressEntity) {

        expressEntity.setInputUserId(userUtil.getUserId());
        expressEntity.setInputTime(new Timestamp(System.currentTimeMillis()));
        expressEntity.setStatus(ExpressStatusEnum.IN_INVENTORY.getCode());
        expressDao.save(expressEntity);
    }

    private void outputExpress(ExpressEntity expressEntity) {
        expressEntity.setOutputUserId(userUtil.getUserId());
        expressEntity.setOutputTime(new Timestamp(System.currentTimeMillis()));
        expressEntity.setStatus(ExpressStatusEnum.OUT_INVENTORY.getCode());
        expressDao.save(expressEntity);
    }


    @Override
    public ExpressVO getExpress(int expressId) {
        Optional<ExpressEntity> expressOpt = expressDao.findById(expressId);
        return expressOpt.map(e->{
            return new ExpressVO(e,storeDao.findFirstByStoreId(e.getInputStoreId()).getName(),
                    storeDao.findFirstByStoreId(e.getOutputStoreId()).getName());
        }).orElseGet(() -> new ExpressVO("物流id不存在"));

    }

    @Override
    public ExpressVO reverseExpress(int expressId) {
        ExpressEntity expressEntity = expressDao.findFirstByExpressId(expressId);
        if(expressEntity.getStatus()==ExpressStatusEnum.ERROR.getCode()){
            throw new ExpressException("该物流单已经取消");
        }
        if(expressEntity.getStatus()==ExpressStatusEnum.NOT_START.getCode()){
            expressEntity.setStatus(ExpressStatusEnum.ERROR.getCode());
            expressDao.saveAndFlush(expressEntity);
            return new ExpressVO(expressEntity);
        }
        ExpressEntity reverseExpress = new ExpressEntity();
        reverseExpress.setInputStoreId(expressEntity.getOutputStoreId());
        reverseExpress.setOutputStoreId(expressEntity.getInputStoreId());
        reverseExpress.setStatus(ExpressStatusEnum.NOT_START.getCode());
        reverseExpress.setExpressProductEntities(expressEntity.getExpressProductEntities().stream().map(e -> {
            ExpressProductEntity expressProductEntity = new ExpressProductEntity();
            expressProductEntity.setProductId(e.getProductId());
            expressProductEntity.setExpressEntity(reverseExpress);
            expressProductEntity.setNumber(e.getNumber());
            expressProductEntity.setStatus(ExpressProductStatusEnum.NOT_START.getCode());
            return expressProductEntity;
        }).collect(Collectors.toList()));
        expressDao.saveAndFlush(reverseExpress);
        ExpressVO expressVO = new ExpressVO(reverseExpress);
        kafkaUtil.sendExpress(expressVO);
        return expressVO;
    }

    @Override
    public List<ExpressProductVO> getProductExpress(int productId) {
        return expressProductDao.findByProductId(productId).stream().map(ex -> new ExpressProductVO(ex,new ProductVO(productDao.findByProductId(ex.getProductId())))).collect(Collectors.toList());
    }

    @Override
    public ExpressProductVO outputProduct(int expressId) {
        //查询物流单信息
        Optional<ExpressEntity> expressOpt = expressDao.findById(expressId);
        if (!expressOpt.isPresent()) {
            throw new ExpressException("物流id不存在");
        }

        ExpressEntity expressEntity = expressOpt.get();
        //从物流单里面的store获得port
        int outputStoreId = expressEntity.getOutputStoreId();
        int inputStoreId = expressEntity.getInputStoreId();
        StoreEntity storeEntity = storeDao.findById(outputStoreId).get();
        String port = storeEntity.getPort();
        //String rfid = rfidUtil.read(port);
        String rfid = rfidUtil.read(expressId+"o");

        //如果没有读到结果
        if (rfid.equals("-1")) {
            throw new ExpressException("扫描超时请重试");
        }
        String newRfid = rfid;
        /*
        try {
            //进行解密,由于读出来的数据没有加密过，先模拟已经加密过
            newRfid = encryptionUtil.decrypt(rfid, inputStoreId, outputStoreId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */


        RfidInfoEntity rfidInfoEntity = new RfidInfoEntity(newRfid);
        //拆解信息
        int productId = rfidInfoEntity.getProductId();
        double outputNumber = rfidInfoEntity.getNumber();




        rfidInfoEntity.setExpressId(expressId);//只需要更新expressId

        ExpressProductEntity result=null;
        int temp = 0;//对已出库product计数
        List<ExpressProductEntity> expressProductEntities = expressEntity.getExpressProductEntities();
        for (ExpressProductEntity expressProductEntity : expressProductEntities) {
            if (expressProductEntity.getProductId() == productId) {
                double number = expressProductEntity.getNumber();//该批次总共需要出库的量
                /*
                try {
                    //写之前加密
                    newRfid = encryptionUtil.encrypt(rfidInfoEntity.toString(), inputStoreId, outputStoreId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String writeRfid = rfidUtil.write(newRfid, port);
                String writeRfid = "1";
                if (writeRfid.equals("-1")) {
                   throw new ExpressException("写入失败");
                }
                */
                if(expressProductEntity.getStatus()==ExpressProductStatusEnum.OUT_INVENTORY.getCode()){
                    throw new ExpressException("该产品已经出库");
                }else if(expressProductEntity.getOutputNumber() + outputNumber>number){
                    throw new ExpressException("本次出库将超过准出量");
                }
                //写入成功之后才进行保存
                expressProductEntity.setOutputNumber(expressProductEntity.getOutputNumber() + outputNumber);
                if (expressProductEntity.getOutputNumber() == number) {
                    expressProductEntity.setStatus(ExpressProductStatusEnum.OUT_INVENTORY.getCode());//更新状态为已出库
                }
                result=expressProductEntity;
                expressProductDao.saveAndFlush(expressProductEntity);
                expressEntity.setStatus(ExpressStatusEnum.OUT_INVENTORY_ING.getCode());
                expressDao.saveAndFlush(expressEntity);
            }
            if (expressProductEntity.getStatus() == ExpressProductStatusEnum.OUT_INVENTORY.getCode()) {
                temp++;
            }
        }
        if(result==null){
            throw new ExpressException("该商品不在该物流批次内，请重新扫描");
        }
        ExpressProductVO ex=new ExpressProductVO(result,result.getOutputNumber(),outputNumber,new ProductVO(productDao.findByProductId(productId)));
        //全部出库
        if (temp == expressProductEntities.size()) {
            outputExpress(expressEntity);
            ex.setCode(2);
        }

        storeEntity.getStoreProductEntities().forEach(x->{
            if(x.getProductEntity().getProductId()==productId){
                double number=x.getNumber()-outputNumber;
                if(number==0){
                    storeProductDao.delete(x);
                }
                else {
                    x.setNumber(number);
                    storeProductDao.saveAndFlush(x);
                }
            }
        });
        return ex;
    }

    @Override
    public ExpressProductVO inputProduct(int expressId) {
        Optional<ExpressEntity> expressOpt = expressDao.findById(expressId);

        if (!expressOpt.isPresent()) {
            throw new ExpressException("物流id不存在");
        }
        ExpressEntity expressEntity = expressOpt.get();
        int outputStoreId = expressEntity.getOutputStoreId();
        int inputStoreId = expressEntity.getInputStoreId();
        StoreEntity storeEntity = storeDao.findById(inputStoreId).get();
        String port = storeEntity.getPort();
        //String rfid = rfidUtil.read(port);
        String rfid = rfidUtil.read(expressId+"i");
        //如果没有读到结果
        if (rfid.equals("-1")) {
            throw new ExpressException("扫描超时请重试");
        }
        String newRfid = rfid;
        /*
        String newRfid = null;
        try {
            //进行解密
            rfid = encryptionUtil.encrypt(rfid, inputStoreId, outputStoreId);
            newRfid = encryptionUtil.decrypt(rfid, inputStoreId, outputStoreId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

        //拆解信息
        RfidInfoEntity rfidInfoEntity = new RfidInfoEntity(newRfid);
        int productId = rfidInfoEntity.getProductId();
        double inputNumber = rfidInfoEntity.getNumber();
        ExpressProductVO result=null;
        //更新expressProduct变为已入库并判断是否所有的expressProduct的状态都变成已入库
        int temp = 0;//对已入库product计数
        List<ExpressProductEntity> expressProductEntities = expressEntity.getExpressProductEntities();
        for (ExpressProductEntity expressProductEntity : expressProductEntities) {
            if (expressProductEntity.getProductId() == productId) {
                double number = expressProductEntity.getNumber();
                /*
                String writeRfid = rfidUtil.write(newRfid, port);
                if (writeRfid.equals("-1")) {
                    throw new ExpressException("写入失败");
                }
                */
                if(expressProductEntity.getStatus()==ExpressProductStatusEnum.IN_INVENTORY.getCode()){
                    throw new ExpressException("该产品已经入库");
                }else if(expressProductEntity.getInputNumber() + inputNumber>number){
                    throw new ExpressException("本次入库将超过准入量");
                }
                expressProductEntity.setInputNumber(expressProductEntity.getInputNumber() + inputNumber);
                if (expressProductEntity.getInputNumber() == number) {
                    expressProductEntity.setStatus(ExpressProductStatusEnum.IN_INVENTORY.getCode());//更新状态为已入库
                }
                result=new ExpressProductVO(expressProductEntity,expressProductEntity.getInputNumber(),inputNumber,new ProductVO(productDao.findByProductId(productId)));
                expressProductDao.saveAndFlush(expressProductEntity);
                expressEntity.setStatus(ExpressStatusEnum.IN_INVENTORY_ING.getCode());
                expressDao.saveAndFlush(expressEntity);
            }
            if (expressProductEntity.getStatus() == ExpressProductStatusEnum.IN_INVENTORY.getCode()) {
                temp++;
            }
        }
        if(result==null){
            throw new ExpressException("该商品不在该物流批次内，请重新扫描");
        }
        if (temp == expressProductEntities.size()) {
            inputExpress(expressEntity);
            result.setCode(2);//这里设置为2的话代表该物流全部完成，为1的话代表该product扫描成功
        }
        boolean exist=false;
        for (StoreProductEntity storeProductEntity : storeEntity.getStoreProductEntities()) {
            if(storeProductEntity.getProductEntity().getProductId()==productId){
                exist=true;
                double number=storeProductEntity.getNumber()+inputNumber;
                storeProductEntity.setNumber(number);
                storeProductDao.saveAndFlush(storeProductEntity);
            }
        }
        if(!exist){
            StoreProductEntity storeProductEntity=new StoreProductEntity();
            storeProductEntity.setNumber(inputNumber);
            storeProductEntity.setProductEntity(productDao.findByProductId(productId));
            storeProductEntity.setStoreEntity(storeEntity);
            storeProductDao.saveAndFlush(storeProductEntity);
        }
        return result;
    }

}
