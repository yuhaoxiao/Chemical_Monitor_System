import cn.nju.edu.chemical_monitor_system.dao.ProductDao;
import cn.nju.edu.chemical_monitor_system.entity.ProductEntity;
import cn.nju.edu.chemical_monitor_system.utils.safe_util.SafeUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class KMeansTest extends BaseTest {
    @Autowired
    SafeUtil safeUtil;
    @Autowired
    ProductDao productDao;
    @Test
    public void testSetAndGet() {
        List<ProductEntity> productEntities = productDao.findAll().subList(0,9);
        ProductEntity productEntity=productDao.findByProductId(10);
        safeUtil.getSafeProducts(productEntity, productEntities).forEach(e->System.out.println(e.getProductId()));
    }
}
