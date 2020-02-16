package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.ExpressProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public interface ExpressProductDao extends JpaRepository<ExpressProductEntity, Serializable> {
    List<ExpressProductEntity> findByProductId(int productId);

    @Query(value = "select c.CAS_id, c.Name, sum(iob.Number) from express e, expressproduct ep, product p, cas c, Store s" +
            " where e.Express_id = ep.Express_id and ep.Product_id = p.Product_id and p.CAS_id = c.CAS_id" +
            " and e.Input_time > ?1 and e.Input_time < ?2 and e.Input_Store_id = ?3" +
            " group by c.CAS_id order by c.CAS_id")
    List<Object> findByInputStore(Timestamp start, Timestamp end, int inputStoreId);

    @Query(value = "select c.CAS_id, c.Name, sum(iob.Number) from express e, expressproduct ep, product p, cas c, Store s" +
            " where e.Express_id = ep.Express_id and ep.Product_id = p.Product_id and p.CAS_id = c.CAS_id" +
            " and e.Output_time > ?1 and e.Output_time < ?2 and e.Output_Store_id = ?3" +
            " group by c.CAS_id order by c.CAS_id")
    List<Object> findByOutputStore(Timestamp start, Timestamp end, int outputStoreId);

    @Query(value = "select c.CAS_id, c.Name, sum(iob.Number) from express e, expressproduct ep, product p, cas c, Store s" +
            " where e.Express_id = ep.Express_id and ep.Product_id = p.Product_id and p.CAS_id = c.CAS_id" +
            " and e.Input_time > ?1 and e.Input_time < ?2 and e.Input_Store_id = ?3 and c.CAS_id = ?4")
    List<Object> findByInputStoreAndCasId(Timestamp start, Timestamp end, int inputStoreId, int casId);

    @Query(value = "select c.CAS_id, c.Name, sum(iob.Number) from express e, expressproduct ep, product p, cas c, Store s" +
            " where e.Express_id = ep.Express_id and ep.Product_id = p.Product_id and p.CAS_id = c.CAS_id" +
            " and e.Output_time > ?1 and e.Output_time < ?2 and e.Output_Store_id = ?3 and c.CAS_id = ?4")
    List<Object> findByOutputStoreAndCasId(Timestamp start, Timestamp end, int outputStoreId, int casId);
}
