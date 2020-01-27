package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.constant.ExpressStatusEnum;
import cn.nju.edu.chemical_monitor_system.dao.ExpressDao;
import cn.nju.edu.chemical_monitor_system.dao.ProductDao;
import cn.nju.edu.chemical_monitor_system.dao.StoreDao;
import cn.nju.edu.chemical_monitor_system.dao.UserDao;
import cn.nju.edu.chemical_monitor_system.entity.*;
import cn.nju.edu.chemical_monitor_system.service.ExpressService;
import cn.nju.edu.chemical_monitor_system.vo.ExpressProductVO;
import cn.nju.edu.chemical_monitor_system.vo.ExpressVO;
import cn.nju.edu.chemical_monitor_system.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpressServiceImpl implements ExpressService {
    @Autowired
    private ExpressDao expressDao;
    @Autowired
    private StoreDao storeDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserDao userDao;

    @Override
    public ExpressVO createExpress(int inputStoreId, int outputStoreId, List<ProductVO> productVOS) {
        ExpressEntity expressEntity = new ExpressEntity();
        StoreEntity inputStore = storeDao.findFirstByStoreId(inputStoreId);
        expressEntity.setInputStore(inputStore);
        StoreEntity outputStore = storeDao.findFirstByStoreId(outputStoreId);
        expressEntity.setOutputStore(outputStore);

        //数据库设置字段时设定为notNULL，这里应该怎么填
        UserEntity fake = userDao.findFirstByUserId(1);
        expressEntity.setInputUser(fake);
        expressEntity.setOutputUser(fake);

        expressEntity.setStatus(ExpressStatusEnum.NOT_START.getCode());

        List<ExpressProductEntity> expressProductEntities = productVOS.stream()
                .map(productVO -> {
                    ExpressProductEntity expressProductEntity = new ExpressProductEntity();
                    expressProductEntity.setNumber(productVO.getNumber());
                    expressProductEntity.setExpressEntity(expressEntity);
                    ProductEntity p = productDao.findFirstByProductId(productVO.getProductId());
                    expressProductEntity.setProductEntity(p);
                    return expressProductEntity;
                }).collect(Collectors.toList());

        expressEntity.setExpressProductEntities(expressProductEntities);

        expressDao.save(expressEntity);
        return new ExpressVO(expressEntity);
    }

    @Override
    public ExpressVO inputExpress(int expressId, int userId) {
        ExpressEntity expressEntity = expressDao.findFirstByExpressId(expressId);
        expressEntity.setStatus(ExpressStatusEnum.IN_INVENTORY.getCode());
        UserEntity userEntity = userDao.findFirstByUserId(userId);
        expressEntity.setInputUser(userEntity);
        expressEntity.setInputTime(new Timestamp(System.currentTimeMillis()));
        expressDao.save(expressEntity);
        return new ExpressVO(expressEntity);
    }

    @Override
    public ExpressVO outputExpress(int expressId, int userId) {
        ExpressEntity expressEntity = expressDao.findFirstByExpressId(expressId);
        expressEntity.setStatus(ExpressStatusEnum.OUT_INVENTORY.getCode());
        UserEntity userEntity = userDao.findFirstByUserId(userId);
        expressEntity.setOutputUser(userEntity);
        expressEntity.setOutputTime(new Timestamp(System.currentTimeMillis()));
        expressDao.save(expressEntity);
        return new ExpressVO(expressEntity);
    }

    @Override
    public ExpressVO findExpress(int expressId) {
        ExpressEntity expressEntity = expressDao.findFirstByExpressId(expressId);
        return new ExpressVO(expressEntity);
    }

    @Override
    public List<ExpressProductVO> getProductExpress(int productId) {
        ProductEntity productEntity=productDao.findFirstByProductId(productId);
        return productEntity.getExpressProductEntities().stream()
                .map(ExpressProductVO::new)
                .collect(Collectors.toList());
    }

}
