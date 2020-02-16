package cn.nju.edu.chemical_monitor_system.dao;

import cn.nju.edu.chemical_monitor_system.entity.InOutBatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public interface InoutBatchDao extends JpaRepository<InOutBatchEntity, Serializable> {

    List<InOutBatchEntity> findByStoreId(int storeId);

    List<InOutBatchEntity> findByBatchId(int batchId);

    List<InOutBatchEntity> findByBatchIdAndInout(int batchId, int isIn);

    List<InOutBatchEntity> findByBatchIdAndProductIdAndStoreIdAndInout(int batchId,int ProductId,int StoreId,int isIn);

    @Query(nativeQuery = true, value = "select c.CAS_id, c.Name, sum(iob.Number) from batch b, inoutbatch iob, product p, cas c " +
            "where iob.Batch_id = b.batch_id and iob.Product_id = p.Product_id and p.CAS_id = c.CAS_id" +
            " and b.Type = ?1 and b.Time > ?2 and b.Time < ?3 and iob.InOrOut = ?4" +
            " group by c.CAS_id order by c.CAS_id")
    List<Object> findByTypeAndInoutOfPark(String type, Timestamp start, Timestamp end, int isIn);

    @Query(nativeQuery = true, value = "select c.CAS_id, c.Name, sum(iob.Number) from batch b, inoutbatch iob, product p, cas c, " +
            "productionline pl, enterprise e where iob.Batch_id = b.batch_id and iob.Product_id = p.Product_id and p.CAS_id = c.CAS_id" +
            " and b.ProductionLine_id = pl.ProductionLine_id and e.Enterprise_id = pl.Enterprise_id" +
            " and b.Type = ?1 and b.Time > ?2 and b.Time < ?3 and iob.InOrOut = ?4 and e.Enterprise_id = ?5" +
            " group by c.CAS_id order by c.CAS_id")
    List<Object> findByTypeAndInoutOfEnterprise(String type, Timestamp start, Timestamp end, int isIn, int eid);

    @Query(nativeQuery = true, value = "select c.CAS_id, c.Name, sum(iob.Number) from batch b, inoutbatch iob, product p, cas c, " +
            "productionline pl where iob.Batch_id = b.batch_id and iob.Product_id = p.Product_id and p.CAS_id = c.CAS_id" +
            " and b.ProductionLine_id = pl.ProductionLine_id " +
            " and b.Type = ?1 and b.Time > ?2 and b.Time < ?3 and iob.InOrOut = ?4 and pl.ProductionLine_id = ?5" +
            " group by c.CAS_id order by c.CAS_id")
    List<Object> findByTypeAndInoutOfPL(String type, Timestamp start, Timestamp end, int isIn, int plid);

    @Query(nativeQuery = true, value = "select c.CAS_id, c.Name, sum(iob.Number) from batch b, inoutbatch iob, product p, cas c " +
            "where iob.Batch_id = b.batch_id and iob.Product_id = p.Product_id and p.CAS_id = c.CAS_id" +
            " and b.Type = ?1 and b.Time > ?2 and b.Time < ?3 and iob.InOrOut = ?4 and c.CAS_id = ?6")
    List<Object> findByTypeAndInoutAndCasIdOfPark(String type, Timestamp start, Timestamp end, int isIn, int casId);

    @Query(nativeQuery = true, value = "select c.CAS_id, c.Name, sum(iob.Number) from batch b, inoutbatch iob, product p, cas c, " +
            "productionline pl, enterprise e where iob.Batch_id = b.batch_id and iob.Product_id = p.Product_id and p.CAS_id = c.CAS_id" +
            " and b.ProductionLine_id = pl.ProductionLine_id and e.Enterprise_id = pl.Enterprise_id" +
            " and b.Type = ?1 and b.Time > ?2 and b.Time < ?3 and iob.InOrOut = ?4 and e.Enterprise_id = ?5" +
            " and c.CAS_id = ?6")
    List<Object> findByTypeAndInoutAndCasIdOfEnterprise(String type, Timestamp start, Timestamp end, int isIn, int eid, int casId);

    @Query(nativeQuery = true, value = "select c.CAS_id, c.Name, sum(iob.Number) from batch b, inoutbatch iob, product p, cas c, " +
            "productionline pl where iob.Batch_id = b.batch_id and iob.Product_id = p.Product_id and p.CAS_id = c.CAS_id" +
            " and b.ProductionLine_id = pl.ProductionLine_id " +
            " and b.Type = ?1 and b.Time > ?2 and b.Time < ?3 and iob.InOrOut = ?4 and pl.ProductionLine_id = ?5" +
            " and c.CAS_id = ?6")
    List<Object> findByTypeAndInoutAndCasIdOfPL(String type, Timestamp start, Timestamp end, int isIn, int plid, int casId);
}