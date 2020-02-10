package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.constant.ExpressProductStatusEnum;
import cn.nju.edu.chemical_monitor_system.constant.ExpressStatusEnum;
import cn.nju.edu.chemical_monitor_system.dao.*;
import cn.nju.edu.chemical_monitor_system.entity.*;
import cn.nju.edu.chemical_monitor_system.service.ExpressService;
import cn.nju.edu.chemical_monitor_system.utils.encryption_util.EncryptionUtil;
import cn.nju.edu.chemical_monitor_system.utils.rfid_util.RfidUtil;
import cn.nju.edu.chemical_monitor_system.utils.safe_util.SafeUtil;
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
    private SafeUtil safeUtil;

    @Autowired
    EncryptionUtil encryptionUtil;

    @Autowired
    private RfidUtil rfidUtil;

    @Override
    public ExpressVO createExpress(int inputStoreId, int outputStoreId, Map<Integer, Double> productNumberMap) {
        ExpressEntity expressEntity = new ExpressEntity();
        Optional<StoreEntity> inputStoreOpt = storeDao.findById(inputStoreId);
        Optional<StoreEntity> outputStoreOpt = storeDao.findById(outputStoreId);

        if (!inputStoreOpt.isPresent()) {
            return new ExpressVO("入库仓库id不存在");
        }

        if (!outputStoreOpt.isPresent()) {
            return new ExpressVO("出库仓库id不存在");
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
            s.append("的商品不能入库");
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
            ProductEntity productEntity=productOpt.get();
            ep.setProductEntity(productEntity);
            ep.setExpressEntity(expressEntity);
            ep.setStatus(ExpressProductStatusEnum.NOT_START.getCode());
            ep.setNumber(entry.getValue());
            expressProductEntities.add(ep);
        }

        expressEntity.setExpressProductEntities(expressProductEntities);
        expressDao.save(expressEntity);
        return new ExpressVO(expressEntity);
    }

    private void inputExpress(ExpressEntity expressEntity, int userId) {
        expressEntity.setInputUserId(userId);
        expressEntity.setInputTime(new Timestamp(System.currentTimeMillis()));
        expressEntity.setStatus(ExpressStatusEnum.IN_INVENTORY.getCode());
        expressDao.save(expressEntity);
    }

    private void outputExpress(ExpressEntity expressEntity, int userId) {
        expressEntity.setOutputUserId(userId);
        expressEntity.setOutputTime(new Timestamp(System.currentTimeMillis()));
        expressEntity.setStatus(ExpressStatusEnum.OUT_INVENTORY.getCode());
        expressDao.save(expressEntity);
    }


    @Override
    public ExpressVO getExpress(int expressId) {
        Optional<ExpressEntity> expressOpt = expressDao.findById(expressId);

        return expressOpt.map(ExpressVO::new).orElseGet(() -> new ExpressVO("物流id不存在"));

    }

    @Override
    public List<ExpressProductVO> getProductExpress(int productId) {
        Optional<ProductEntity> productOpt = productDao.findById(productId);

        return productOpt.map(productEntity -> productEntity.getExpressProductEntities().stream()
                .map(ExpressProductVO::new)
                .collect(Collectors.toList())).orElseGet(ArrayList::new);
    }

    @Override
    public ProductVO outputProduct(int expressId, int userId) {
        //查询物流单信息
        Optional<ExpressEntity> expressOpt = expressDao.findById(expressId);
        if (!expressOpt.isPresent()) {
            return new ProductVO("物流id不存在");
        }

        ExpressEntity expressEntity = expressOpt.get();
        //从物流单里面的store获得port
        int outputStoreId=expressEntity.getOutputStoreId();
        int inputStoreId=expressEntity.getInputStoreId();
        StoreEntity storeEntity=storeDao.findByStoreId(outputStoreId);
        String port = storeEntity.getPort();
        String rfid = rfidUtil.read(port);
        //如果没有读到结果
        if (rfid.equals("-1")) {
            return new ProductVO("扫描超时请重试");
        }
        String newRfid = null;
        try {
            //进行解密,由于读出来的数据没有加密过，先模拟已经加密过
            newRfid = encryptionUtil.decrypt(rfid, inputStoreId, outputStoreId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RfidInfoEntity rfidInfoEntity = new RfidInfoEntity(newRfid);
        //拆解信息
        int productId = rfidInfoEntity.getProductId();
        int expressIdOfProduct = rfidInfoEntity.getExpressId();
        double outputNumber = rfidInfoEntity.getNumber();

        //如果扫描的货物不是当前物流单内的，返回报错
        if (expressId != expressIdOfProduct) {
            return new ProductVO("该商品不在该物流批次内，请重新扫描");
        }
        rfidInfoEntity.setExpressId(expressId);//只需要更新expressId

        int temp = 0;//对已出库product计数
        List<ExpressProductEntity> expressProductEntities = expressEntity.getExpressProductEntities();
        for (ExpressProductEntity expressProductEntity : expressProductEntities) {
            if (expressProductEntity.getProductEntity().getProductId() == productId) {
                double number = expressProductEntity.getNumber();//该批次总共需要出库的量
                try {
                    //写之前加密
                    newRfid = encryptionUtil.encrypt(rfidInfoEntity.toString(), inputStoreId, outputStoreId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String writeRfid = rfidUtil.write(newRfid, port);
                if (writeRfid.equals("-1")) {
                    return new ProductVO("写入失败");
                }
                //写入成功之后才进行保存
                expressProductEntity.setOutputNumber(expressProductEntity.getOutputNumber() + outputNumber);
                if (expressProductEntity.getOutputNumber() == number) {
                    expressProductEntity.setStatus(ExpressProductStatusEnum.OUT_INVENTORY.getCode());//更新状态为已出库
                }
                expressProductDao.saveAndFlush(expressProductEntity);
            }
            if (expressProductEntity.getStatus() == ExpressProductStatusEnum.OUT_INVENTORY.getCode()) {
                temp++;
            }
            //全部出库
            if (temp == expressProductEntities.size()) {
                outputExpress(expressEntity, userId);
            }
        }
        return new ProductVO(productDao.findByProductId(productId));
    }

    @Override
    public ProductVO inputProduct(int expressId, int userId) {
        Optional<ExpressEntity> expressOpt = expressDao.findById(expressId);

        if (!expressOpt.isPresent()) {
            return new ProductVO("物流id不存在");
        }
        ExpressEntity expressEntity = expressOpt.get();
        int outputStoreId=expressEntity.getOutputStoreId();
        int inputStoreId=expressEntity.getInputStoreId();
        StoreEntity storeEntity=storeDao.findByStoreId(outputStoreId);
        String port = storeEntity.getPort();
        String rfid = rfidUtil.read(port);
        //如果没有读到结果
        if (rfid.equals("-1")) {
            return new ProductVO("扫描超时请重试");
        }
        String newRfid = null;
        try {
            //进行解密
            rfid = encryptionUtil.encrypt(rfid, inputStoreId, outputStoreId);
            newRfid = encryptionUtil.decrypt(rfid, inputStoreId, outputStoreId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //拆解信息
        RfidInfoEntity rfidInfoEntity = new RfidInfoEntity(newRfid);
        int productId = rfidInfoEntity.getProductId();
        double inputNumber = rfidInfoEntity.getNumber();

        ProductVO productVO = new ProductVO(productDao.findByProductId(productId));
        //更新expressProduct变为已入库并判断是否所有的expressProduct的状态都变成已入库
        int temp = 0;//对已入库product计数
        List<ExpressProductEntity> expressProductEntities = expressEntity.getExpressProductEntities();
        for (ExpressProductEntity expressProductEntity : expressProductEntities) {
            if (expressProductEntity.getProductEntity().getProductId() == productId) {
                double number = expressProductEntity.getNumber();
                String writeRfid = rfidUtil.write(newRfid, port);
                if (writeRfid.equals("-1")) {
                    return new ProductVO("写入失败");
                }
                expressProductEntity.setInputNumber(expressProductEntity.getInputNumber() + inputNumber);
                if (expressProductEntity.getInputNumber() == number) {
                    expressProductEntity.setStatus(ExpressProductStatusEnum.IN_INVENTORY.getCode());//更新状态为已入库
                }
                expressProductDao.saveAndFlush(expressProductEntity);
            }
            if (expressProductEntity.getStatus() == ExpressProductStatusEnum.IN_INVENTORY.getCode()) {
                temp++;
            }
            //全部入库
            if (temp == expressProductEntities.size()) {
                inputExpress(expressEntity, userId);
                productVO.setCode(2);//这里设置为2的话代表该物流全部完成，为1的话代表该product扫描成功
                return productVO;
            }
        }
        return productVO;
    }

}
