package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.constant.ExpressProductStatusEnum;
import cn.nju.edu.chemical_monitor_system.constant.ExpressStatusEnum;
import cn.nju.edu.chemical_monitor_system.dao.*;
import cn.nju.edu.chemical_monitor_system.entity.ExpressEntity;
import cn.nju.edu.chemical_monitor_system.entity.ExpressProductEntity;
import cn.nju.edu.chemical_monitor_system.entity.ProductEntity;
import cn.nju.edu.chemical_monitor_system.entity.StoreEntity;
import cn.nju.edu.chemical_monitor_system.service.ExpressService;
import cn.nju.edu.chemical_monitor_system.service.RfidService;
import cn.nju.edu.chemical_monitor_system.utils.encryption_util.EncryptionUtil;
import cn.nju.edu.chemical_monitor_system.utils.safe_util.Product;
import cn.nju.edu.chemical_monitor_system.utils.safe_util.SafeUtil;
import cn.nju.edu.chemical_monitor_system.vo.ExpressProductVO;
import cn.nju.edu.chemical_monitor_system.vo.ExpressVO;
import cn.nju.edu.chemical_monitor_system.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
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
    private RfidService rfidService;

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

        expressEntity.setInputStore(inputStoreOpt.get());
        expressEntity.setOutputStore(outputStoreOpt.get());
        expressEntity.setStatus(ExpressStatusEnum.NOT_START.getCode());

        List<ExpressProductEntity> expressProductEntities = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : productNumberMap.entrySet()) {
            ExpressProductEntity ep = new ExpressProductEntity();
            Optional<ProductEntity> productOpt = productDao.findById(entry.getKey());

            if (!productOpt.isPresent()) {
                continue;
            }

            ep.setProductEntity(productOpt.get());
            ep.setExpressEntity(expressEntity);
            ep.setStatus(ExpressProductStatusEnum.NOT_START.getCode());
            ep.setNumber(entry.getValue());
            expressProductEntities.add(ep);
        }

        expressEntity.setExpressProductEntities(expressProductEntities);
        expressDao.save(expressEntity);
        return new ExpressVO(expressEntity);
    }

    public ExpressVO inputExpress(int expressId, int userId) {
        Optional<ExpressEntity> expressOpt = expressDao.findById(expressId);

        if (!expressOpt.isPresent()) {
            return new ExpressVO("物流id不存在");
        }

        ExpressEntity expressEntity = expressOpt.get();
        expressEntity.setInputUser(userDao.findById(userId).get());
        expressEntity.setInputTime(new Timestamp(System.currentTimeMillis()));
        expressEntity.setStatus(ExpressStatusEnum.IN_INVENTORY.getCode());
        expressDao.save(expressEntity);
        return new ExpressVO(expressEntity);
    }

    public ExpressVO outputExpress(int expressId, int userId) {
        Optional<ExpressEntity> expressOpt = expressDao.findById(expressId);

        if (!expressOpt.isPresent()) {
            return new ExpressVO("物流id不存在");
        }

        ExpressEntity expressEntity = expressOpt.get();
        expressEntity.setOutputUser(userDao.findById(userId).get());
        expressEntity.setOutputTime(new Timestamp(System.currentTimeMillis()));
        expressEntity.setStatus(ExpressStatusEnum.OUT_INVENTORY.getCode());
        expressDao.save(expressEntity);
        return new ExpressVO(expressEntity);
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

    class ReadRfidThread implements Callable<String> {
        String port;

        ReadRfidThread(String port) {
            this.port = port;
        }

        @Override
        public String call() throws Exception {
            String rfid;
            //返回结果为-1代表读取失败，继续读取
            while ((rfid = rfidService.readRfid(port)).equals("-1")) {
                Thread.sleep(1000);
            }
            return rfid;
        }
    }

    class WriteRfidThread implements Callable<String> {
        String port;
        String rfid;

        WriteRfidThread(String rfid, String port) {
            this.port = port;
            this.rfid = rfid;
        }

        @Override
        public String call() throws Exception {
            String newRfid;
            //返回结果为-1代表写入失败，继续写入
            while ((newRfid = rfidService.writeRfid(rfid, port)).equals("-1")) {
                Thread.sleep(1000);
            }
            return newRfid;
        }
    }

    private String limitTimeTask(Callable<String> thread) {
        FutureTask<String> futureTask = new FutureTask<>(thread);
        new Thread(futureTask).start();
        String result = "";
        try {
            //设置超时时间4秒
            result = futureTask.get(4000, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public ProductVO outputProduct(int expressId, int userId) {
        //查询物流单信息
        ExpressEntity expressEntity = expressDao.findFirstByExpressId(expressId);
        //从物流单里面的store获得port
        String port = expressEntity.getOutputStore().getPort();
        Callable<String> readThread = new ReadRfidThread(port);
        String rfid = limitTimeTask(readThread);
        //如果没有读到结果
        if (rfid.equals("")) {
            ProductVO p = new ProductVO();
            p.setCode(0);
            p.setMessage("扫描超时请重试");
            return p;
        }
        String newRfid = null;
        try {
            //进行解密
            newRfid = encryptionUtil.decrypt(rfid, expressEntity.getInputStore().getStoreId(), expressEntity.getOutputStore().getStoreId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //拆解信息
        int productId = Integer.parseInt(newRfid.substring(0, 1));
        int expressIdOfProduct = Integer.parseInt(newRfid.substring(1, 2));

        //如果扫描的货物不是当前物流单内的，返回报错
        if (expressId != expressIdOfProduct) {
            ProductVO p = new ProductVO();
            p.setCode(0);
            p.setMessage("该商品不在该物流批次内，请重新扫描");
            return p;
        }
        ProductVO productVO = new ProductVO(productDao.findByProductId(productId));
        productVO.setCode(1);
        return productVO;
    }

    @Override
    public ProductVO inputProduct(int expressId, int userId) {
        ExpressEntity expressEntity = expressDao.findFirstByExpressId(expressId);
        String port = expressEntity.getInputStore().getPort();
        Callable<String> readThread = new ReadRfidThread(port);
        String rfid = limitTimeTask(readThread);
        //如果没有读到结果
        if (rfid.equals("")) {
            ProductVO p = new ProductVO();
            p.setCode(0);
            p.setMessage("扫描超时请重试");
            return p;
        }
        String newRfid = null;
        try {
            //进行解密
            newRfid = encryptionUtil.decrypt(rfid, expressEntity.getInputStore().getStoreId(), expressEntity.getOutputStore().getStoreId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //拆解信息
        int productId = Integer.parseInt(newRfid.substring(0, 1));
        ProductVO productVO = new ProductVO(productDao.findByProductId(productId));
        //更新expressProduct变为已入库并判断是否所有的expressProduct的状态都变成已入库
        int temp = 0;//对已入库product计数
        List<ExpressProductEntity> expressProductEntities = expressEntity.getExpressProductEntities();
        for (ExpressProductEntity expressProductEntity : expressProductEntities) {
            if (expressProductEntity.getProductEntity().getProductId() == productId) {
                expressProductEntity.setStatus(ExpressProductStatusEnum.IN_INVENTORY.getCode());//更新状态为已入库
                expressProductDao.saveAndFlush(expressProductEntity);
            }
            if (expressProductEntity.getStatus() == ExpressProductStatusEnum.IN_INVENTORY.getCode()) {
                temp++;
            }
            //全部入库
            if (temp == expressProductEntities.size()) {
                inputExpress(expressId, userId);
                productVO.setCode(2);//这里设置为2的话代表该物流全部完成，为1的话代表该product扫描成功
                return productVO;
            }
        }
        return productVO;
    }

    @Override
    public ExpressVO outputProductRewrite(int productId, int expressId, int userId) {
        ExpressEntity expressEntity = expressDao.findFirstByExpressId(expressId);
        String port = expressEntity.getOutputStore().getPort();
        //更新expressProduct变为已出库并判断是否所有的expressProduct的状态都变成已出库
        int temp = 0;//对已出库product计数
        List<ExpressProductEntity> expressProductEntities = expressEntity.getExpressProductEntities();
        for (ExpressProductEntity expressProductEntity : expressProductEntities) {
            if (expressProductEntity.getProductEntity().getProductId() == productId) {
                double number = expressProductEntity.getNumber();
                String rfid = productId + "" + expressId + "" + number;//更新新的expressId，不过之后需要修改，可能不是简单的字符串连接
                String newRfid = null;
                try {
                    //写之前加密
                    newRfid = encryptionUtil.encrypt(rfid, expressEntity.getInputStore().getStoreId(), expressEntity.getOutputStore().getStoreId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Callable<String> writeThread = new WriteRfidThread(port, newRfid);
                String writeRfid = limitTimeTask(writeThread);
                if (writeRfid.equals("")) {
                    ExpressVO expressVO = new ExpressVO();
                    expressVO.setCode(0);
                    expressVO.setMessage("写入失败");
                    return expressVO;
                }
                //写入成功之后才进行保存
                expressProductEntity.setStatus(ExpressProductStatusEnum.OUT_INVENTORY.getCode());//更新状态为已出库
                expressProductDao.saveAndFlush(expressProductEntity);

            }
            if (expressProductEntity.getStatus() == ExpressProductStatusEnum.OUT_INVENTORY.getCode()) {
                temp++;
            }
            //全部出库
            if (temp == expressProductEntities.size()) {
                return outputExpress(expressId, userId);
            }
        }
        return new ExpressVO(expressEntity);
    }
}
