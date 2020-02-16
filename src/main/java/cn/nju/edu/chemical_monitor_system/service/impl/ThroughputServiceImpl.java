package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.constant.BatchTypeEnum;
import cn.nju.edu.chemical_monitor_system.dao.*;
import cn.nju.edu.chemical_monitor_system.entity.CasEntity;
import cn.nju.edu.chemical_monitor_system.entity.EnterpriseEntity;
import cn.nju.edu.chemical_monitor_system.entity.ProductionLineEntity;
import cn.nju.edu.chemical_monitor_system.entity.StoreEntity;
import cn.nju.edu.chemical_monitor_system.service.ThroughputService;
import cn.nju.edu.chemical_monitor_system.vo.CASThroughputVO;
import cn.nju.edu.chemical_monitor_system.vo.ThroughputVO;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ThroughputServiceImpl implements ThroughputService {

    @Autowired
    private InoutBatchDao inoutBatchDao;

    @Autowired
    private CasDao casDao;

    @Autowired
    private EnterpriseDao enterpriseDao;

    @Autowired
    private StoreDao storeDao;

    @Autowired
    private ExpressProductDao expressProductDao;

    @Autowired
    private ProductionLineDao productionLineDao;

    private List<String> times;

    private List<Pair<Timestamp, Timestamp>> timeIntervals;

    private static long dayMs = 86400000;

    @Override
    public ThroughputVO getEntityThroughput(int entityType, int entityId, int timeType, String start, String end) {
        if (timeType < 0 || timeType > 3) {
            return new ThroughputVO("时间类型不正确");
        }

        if (entityType < 0 || entityType > 3) {
            return new ThroughputVO("实体类型不正确");
        }

        this.times = new ArrayList<>();
        this.timeIntervals = new ArrayList<>();

        try {
            this.getTimeIntervals(start, end, timeType);
        } catch (Exception p) {
            return new ThroughputVO("时间格式不正确");
        }

        List<CASThroughputVO> produce = new ArrayList<>();
        List<CASThroughputVO> consume = new ArrayList<>();
        List<CASThroughputVO> in = new ArrayList<>();
        List<CASThroughputVO> out = new ArrayList<>();

        List<CasEntity> casEntities = casDao.findAllByOrderByCasId();
        int casNumber = casEntities.size();
        for (CasEntity cas : casEntities) {
            CASThroughputVO casThroughputVO = new CASThroughputVO(cas.getCasId(), cas.getName());
            produce.add(casThroughputVO);
            consume.add(casThroughputVO);
            in.add(casThroughputVO);
            out.add(casThroughputVO);
        }

        if (entityType == 0) {
            for (Pair<Timestamp, Timestamp> timePair : timeIntervals) {
                List<Object> produceResults = inoutBatchDao.findByTypeAndInoutOfPark(BatchTypeEnum.PRODUCE.getName(), timePair.getKey(), timePair.getValue(), 0);
                List<Object> consumeResults1 = inoutBatchDao.findByTypeAndInoutOfPark(BatchTypeEnum.PRODUCE.getName(), timePair.getKey(), timePair.getValue(), 1);
                List<Object> consumeResults2 = inoutBatchDao.findByTypeAndInoutOfPark(BatchTypeEnum.DESTROY.getName(), timePair.getKey(), timePair.getValue(), 1);
                List<Object> inResults = inoutBatchDao.findByTypeAndInoutOfPark(BatchTypeEnum.IN_PARK.getName(), timePair.getKey(), timePair.getValue(), 0);
                List<Object> outResults = inoutBatchDao.findByTypeAndInoutOfPark(BatchTypeEnum.PRODUCE.getName(), timePair.getKey(), timePair.getValue(), 1);

                for (int i = 0; i < casNumber; i++) {
                    produce.get(i).getThroughput().add((Double) ((Object[]) produceResults.get(i))[2]);
                    consume.get(i).getThroughput().add((Double) ((Object[]) consumeResults1.get(i))[2] + (Double) ((Object[]) consumeResults2.get(i))[2]);
                    in.get(i).getThroughput().add((Double) ((Object[]) inResults.get(i))[2]);
                    out.get(i).getThroughput().add((Double) ((Object[]) outResults.get(i))[2]);
                }
            }

            for (int i = 0; i < casNumber; i++) {
                produce.get(i).calculate();
                consume.get(i).calculate();
                in.get(i).calculate();
                out.get(i).calculate();
            }

        } else if (entityType == 1) {
            Optional<EnterpriseEntity> enterpriseOpt = enterpriseDao.findById(entityId);

            if (!enterpriseOpt.isPresent()) {
                return new ThroughputVO("企业id不存在");
            }

            for (Pair<Timestamp, Timestamp> timePair : timeIntervals) {
                List<Object> produceResults = inoutBatchDao.findByTypeAndInoutOfEnterprise(BatchTypeEnum.PRODUCE.getName(), timePair.getKey(), timePair.getValue(), 0, entityId);
                List<Object> consumeResults1 = inoutBatchDao.findByTypeAndInoutOfEnterprise(BatchTypeEnum.PRODUCE.getName(), timePair.getKey(), timePair.getValue(), 1, entityId);
                List<Object> consumeResults2 = inoutBatchDao.findByTypeAndInoutOfEnterprise(BatchTypeEnum.DESTROY.getName(), timePair.getKey(), timePair.getValue(), 1, entityId);

                for (int i = 0; i < casNumber; i++) {
                    produce.get(i).getThroughput().add((Double) ((Object[]) produceResults.get(i))[2]);
                    consume.get(i).getThroughput().add((Double) ((Object[]) consumeResults1.get(i))[2] + (Double) ((Object[]) consumeResults2.get(i))[2]);
                }
            }

            for (int i = 0; i < casNumber; i++) {
                produce.get(i).calculate();
                consume.get(i).calculate();
            }
        } else if (entityType == 2) {
            Optional<ProductionLineEntity> productionLineOpt = productionLineDao.findById(entityId);

            if (!productionLineOpt.isPresent()) {
                return new ThroughputVO("生产线id不存在");
            }

            for (Pair<Timestamp, Timestamp> timePair : timeIntervals) {
                List<Object> produceResults = inoutBatchDao.findByTypeAndInoutOfPL(BatchTypeEnum.PRODUCE.getName(), timePair.getKey(), timePair.getValue(), 0, entityId);
                List<Object> consumeResults1 = inoutBatchDao.findByTypeAndInoutOfPL(BatchTypeEnum.PRODUCE.getName(), timePair.getKey(), timePair.getValue(), 1, entityId);
                List<Object> consumeResults2 = inoutBatchDao.findByTypeAndInoutOfPL(BatchTypeEnum.DESTROY.getName(), timePair.getKey(), timePair.getValue(), 1, entityId);

                for (int i = 0; i < casNumber; i++) {
                    produce.get(i).getThroughput().add((Double) ((Object[]) produceResults.get(i))[2]);
                    consume.get(i).getThroughput().add((Double) ((Object[]) consumeResults1.get(i))[2] + (Double) ((Object[]) consumeResults2.get(i))[2]);
                }
            }

            for (int i = 0; i < casNumber; i++) {
                produce.get(i).calculate();
                consume.get(i).calculate();
            }
        } else {
            Optional<StoreEntity> storeOpt = storeDao.findById(entityId);

            if (!storeOpt.isPresent()) {
                return new ThroughputVO("仓库id不存在");
            }

            for (Pair<Timestamp, Timestamp> timePair : timeIntervals) {
                List<Object> inResults = expressProductDao.findByInputStore(timePair.getKey(), timePair.getValue(), entityId);
                List<Object> outResults = expressProductDao.findByOutputStore(timePair.getKey(), timePair.getValue(), entityId);

                for (int i = 0; i < casNumber; i++) {
                    in.get(i).getThroughput().add((Double) ((Object[]) inResults.get(i))[2]);
                    out.get(i).getThroughput().add((Double) ((Object[]) outResults.get(i))[2]);
                }
            }

            for (int i = 0; i < casNumber; i++) {
                in.get(i).calculate();
                out.get(i).calculate();
            }
        }
        return new ThroughputVO(this.times, consume, produce, in, out);
    }

    @Override
    public ThroughputVO getCasThroughput(int entityType, int entityId, int timeType, String start, String end, int casId) {
        if (timeType < 0 || timeType > 3) {
            return new ThroughputVO("时间类型不正确");
        }

        if (entityType < 0 || entityType > 3) {
            return new ThroughputVO("实体类型不正确");
        }

        this.times = new ArrayList<>();
        this.timeIntervals = new ArrayList<>();

        try {
            this.getTimeIntervals(start, end, timeType);
        } catch (Exception p) {
            return new ThroughputVO("时间格式不正确");
        }

        List<CASThroughputVO> produce = new ArrayList<>();
        List<CASThroughputVO> consume = new ArrayList<>();
        List<CASThroughputVO> in = new ArrayList<>();
        List<CASThroughputVO> out = new ArrayList<>();

        Optional<CasEntity> casOpt = casDao.findById(casId);

        if (!casOpt.isPresent()) {
            return new ThroughputVO("Cas id不存在");
        }

        CasEntity casEntity = casOpt.get();
        CASThroughputVO produceCasThroughputVO = new CASThroughputVO(casEntity.getCasId(), casEntity.getName());
        CASThroughputVO consumeCasThroughputVO = new CASThroughputVO(casEntity.getCasId(), casEntity.getName());
        CASThroughputVO inCasThroughputVO = new CASThroughputVO(casEntity.getCasId(), casEntity.getName());
        CASThroughputVO outThroughputVO = new CASThroughputVO(casEntity.getCasId(), casEntity.getName());
        produce.add(produceCasThroughputVO);
        consume.add(consumeCasThroughputVO);
        in.add(inCasThroughputVO);
        out.add(outThroughputVO);

        if (entityType == 0) {
            for (Pair<Timestamp, Timestamp> timePair : timeIntervals) {
                List<Object> produceResults = inoutBatchDao.findByTypeAndInoutAndCasIdOfPark(BatchTypeEnum.PRODUCE.getName(), timePair.getKey(), timePair.getValue(), 0, casId);
                List<Object> consumeResults1 = inoutBatchDao.findByTypeAndInoutAndCasIdOfPark(BatchTypeEnum.PRODUCE.getName(), timePair.getKey(), timePair.getValue(), 1, casId);
                List<Object> consumeResults2 = inoutBatchDao.findByTypeAndInoutAndCasIdOfPark(BatchTypeEnum.DESTROY.getName(), timePair.getKey(), timePair.getValue(), 1, casId);
                List<Object> inResults = inoutBatchDao.findByTypeAndInoutAndCasIdOfPark(BatchTypeEnum.IN_PARK.getName(), timePair.getKey(), timePair.getValue(), 0, casId);
                List<Object> outResults = inoutBatchDao.findByTypeAndInoutAndCasIdOfPark(BatchTypeEnum.PRODUCE.getName(), timePair.getKey(), timePair.getValue(), 1, casId);

                produce.get(0).getThroughput().add((Double) ((Object[]) produceResults.get(0))[2]);
                consume.get(0).getThroughput().add((Double) ((Object[]) consumeResults1.get(0))[2] + (Double) ((Object[]) consumeResults2.get(0))[2]);
                in.get(0).getThroughput().add((Double) ((Object[]) inResults.get(0))[2]);
                out.get(0).getThroughput().add((Double) ((Object[]) outResults.get(0))[2]);
            }

            produce.get(0).calculate();
            consume.get(0).calculate();
            in.get(0).calculate();
            out.get(0).calculate();
        } else if (entityType == 1) {
            Optional<EnterpriseEntity> enterpriseOpt = enterpriseDao.findById(entityId);

            if (!enterpriseOpt.isPresent()) {
                return new ThroughputVO("企业id不存在");
            }

            for (Pair<Timestamp, Timestamp> timePair : timeIntervals) {
                List<Object> produceResults = inoutBatchDao.findByTypeAndInoutAndCasIdOfEnterprise(BatchTypeEnum.PRODUCE.getName(), timePair.getKey(), timePair.getValue(), 0, entityId, casId);
                List<Object> consumeResults1 = inoutBatchDao.findByTypeAndInoutAndCasIdOfEnterprise(BatchTypeEnum.PRODUCE.getName(), timePair.getKey(), timePair.getValue(), 1, entityId, casId);
                List<Object> consumeResults2 = inoutBatchDao.findByTypeAndInoutAndCasIdOfEnterprise(BatchTypeEnum.DESTROY.getName(), timePair.getKey(), timePair.getValue(), 1, entityId, casId);

                produce.get(0).getThroughput().add((Double) ((Object[]) produceResults.get(0))[2]);
                consume.get(0).getThroughput().add((Double) ((Object[]) consumeResults1.get(0))[2] + (Double) ((Object[]) consumeResults2.get(0))[2]);
            }

            produce.get(0).calculate();
            consume.get(0).calculate();
        } else if (entityType == 2) {
            Optional<ProductionLineEntity> productionLineOpt = productionLineDao.findById(entityId);

            if (!productionLineOpt.isPresent()) {
                return new ThroughputVO("生产线id不存在");
            }

            for (Pair<Timestamp, Timestamp> timePair : timeIntervals) {
                List<Object> produceResults = inoutBatchDao.findByTypeAndInoutAndCasIdOfPL(BatchTypeEnum.PRODUCE.getName(), timePair.getKey(), timePair.getValue(), 0, entityId, casId);
                List<Object> consumeResults1 = inoutBatchDao.findByTypeAndInoutAndCasIdOfPL(BatchTypeEnum.PRODUCE.getName(), timePair.getKey(), timePair.getValue(), 1, entityId, casId);
                List<Object> consumeResults2 = inoutBatchDao.findByTypeAndInoutAndCasIdOfPL(BatchTypeEnum.DESTROY.getName(), timePair.getKey(), timePair.getValue(), 1, entityId, casId);

                produce.get(0).getThroughput().add((Double) ((Object[]) produceResults.get(0))[2]);
                consume.get(0).getThroughput().add((Double) ((Object[]) consumeResults1.get(0))[2] + (Double) ((Object[]) consumeResults2.get(0))[2]);
            }

            produce.get(0).calculate();
            consume.get(0).calculate();
        } else {
            Optional<StoreEntity> storeOpt = storeDao.findById(entityId);

            if (!storeOpt.isPresent()) {
                return new ThroughputVO("仓库id不存在");
            }

            for (Pair<Timestamp, Timestamp> timePair : timeIntervals) {
                List<Object> inResults = expressProductDao.findByInputStoreAndCasId(timePair.getKey(), timePair.getValue(), entityId, casId);
                List<Object> outResults = expressProductDao.findByOutputStoreAndCasId(timePair.getKey(), timePair.getValue(), entityId, casId);

                in.get(0).getThroughput().add((Double) ((Object[]) inResults.get(0))[2]);
                out.get(0).getThroughput().add((Double) ((Object[]) outResults.get(0))[2]);
            }

            in.get(0).calculate();
            out.get(0).calculate();
        }
        return new ThroughputVO(this.times, consume, produce, in, out);
    }

    private void getTimeIntervals(String start, String end, int timeType) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        if (timeType == 0) {
            int startYear = Integer.parseInt(start);
            int endYear = Integer.parseInt(end);
            for (int i = startYear; i < endYear; i++) {
                times.add(i + "");
                timeIntervals.add(new Pair<>(new Timestamp(dateFormat.parse(i + "-01-01").getTime()),
                        new Timestamp(dateFormat.parse((i + 1) + "-01-01").getTime())));
            }
        } else if (timeType == 1) {
            Date startDate = this.quarterToDate(start);
            Date endDate = this.getFirstDayOfNextXMonth(this.quarterToDate(end), 3);
            while (endDate.after(startDate)) {
                Date temp = this.getFirstDayOfNextXMonth(startDate, 3);
                times.add(dateToQuarter(startDate));
                timeIntervals.add(new Pair<>(new Timestamp(startDate.getTime()), new Timestamp(temp.getTime())));
                startDate = temp;
            }
        } else if (timeType == 2) {
            Date endDate = this.getFirstDayOfNextXMonth(dateFormat.parse(end + "-01"), 1);
            Date startDate = dateFormat.parse(start + "-01");
            while (endDate.after(startDate)) {
                Date temp = this.getFirstDayOfNextXMonth(startDate, 1);
                times.add(new Timestamp(startDate.getTime()).toString().substring(0, 7));
                timeIntervals.add(new Pair<>(new Timestamp(startDate.getTime()), new Timestamp(temp.getTime())));
                startDate = temp;
            }
        } else {
            long startTime = dateFormat.parse(start).getTime();
            long days = (dateFormat.parse(end).getTime() - dateFormat.parse(start).getTime()) / dayMs + 1;
            for (int i = 0; i < days; i++) {
                long dayStart = startTime + i * dayMs;
                long dayEnd = startTime + (i + 1) * dayMs;
                times.add(new Timestamp(dayStart).toString().substring(0, 10));
                timeIntervals.add(new Pair<>(new Timestamp(dayStart), new Timestamp(dayEnd)));
            }
        }
    }

    private Date getFirstDayOfNextXMonth(Date d, int x) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, x);
        return calendar.getTime();
    }

    private Date quarterToDate(String quarter) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        String date = quarter.substring(0, 5) + "0" + ((Integer.parseInt(quarter.substring(6, 7)) - 1) * 3 + 1) + "-01";
        return dateFormat.parse(date);
    }

    private String dateToQuarter(Date d) {
        return (d.getYear() + 1900) + "-Q" + (d.getMonth() / 3 + 1);
    }
}
