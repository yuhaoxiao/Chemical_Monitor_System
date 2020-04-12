package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.constant.BatchTypeEnum;
import cn.nju.edu.chemical_monitor_system.dao.*;
import cn.nju.edu.chemical_monitor_system.entity.*;
import cn.nju.edu.chemical_monitor_system.exception.MyException;
import cn.nju.edu.chemical_monitor_system.request.GetThroughputRequest;
import cn.nju.edu.chemical_monitor_system.service.ThroughputCapacityService;
import cn.nju.edu.chemical_monitor_system.vo.CASThroughputVO;
import cn.nju.edu.chemical_monitor_system.vo.CasTimeVO;
import cn.nju.edu.chemical_monitor_system.vo.CasVO;
import cn.nju.edu.chemical_monitor_system.vo.ThroughputVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ThroughputCapacityServiceImpl implements ThroughputCapacityService {
    @Autowired
    BatchDao batchDao;
    @Autowired
    InoutBatchDao inoutBatchDao;
    @Autowired
    ProductDao productDao;
    @Autowired
    ProductionLineDao productionLineDao;
    @Autowired
    EnterpriseDao enterpriseDao;
    @Autowired
    ExpressDao expressDao;
    private static SimpleDateFormat yearSdf = new SimpleDateFormat("yyyy");
    private static SimpleDateFormat monthSdf = new SimpleDateFormat("yyyy-MM");
    private static SimpleDateFormat daysSdf = new SimpleDateFormat("yyyy-MM-dd");
    private static int entityType; // 0代表整个园区，1代表企业，2代表生产线，3代表仓库
    private static int entityId; // 企业、生产线或仓库 id
    private static int timeType; // 0代表年，1代表季度，2代表月，3代表日
    private static String startTime;// 开始时间
    private static String endTime;// 结束时间
    private static int casId; // 如果不为0，代表只查这一个化学品的吞吐量
    private static Map<Integer, String> inoutBatchId2Time = new HashMap<>();//batchId->时间（时间的字符格式根据timeType定制）
    private static Map<Integer, String> expressProductId2Time = new HashMap<>();//expressProductId->时间（时间的字符格式根据timeType定制）

    private static Date start;
    private static Date end;
    @Override
    public ThroughputVO getThroughput(GetThroughputRequest getThroughputRequest) {
        entityType = getThroughputRequest.getEntityType();
        entityId = getThroughputRequest.getEntityId();
        timeType = getThroughputRequest.getTimeType();
        startTime = getThroughputRequest.getStart();
        endTime = getThroughputRequest.getEnd();
        casId = getThroughputRequest.getCasId();
        inoutBatchId2Time.clear();
        List<BatchEntity> batchEntities = new ArrayList<>();
        List<String> times = getTimeList();
        start = String2Date(times.get(0));
        end = String2Date(times.get(times.size() - 1));
        if (entityType == 0) {
            batchEntities = batchDao.findByTimeBetween(start, end);
        } else if (entityType == 1) {
            Optional<EnterpriseEntity> option = enterpriseDao.findById(entityId);
            if (option.isPresent()) {
                List<Integer> productionLineIds = productionLineDao.findByEnterpriseEntity(option.get())
                        .stream().map(ProductionLineEntity::getProductionLineId).collect(Collectors.toList());
                batchEntities = batchDao.findByTimeBetweenAndProductionLineIdIn(start, end, productionLineIds);
            } else {
                throw new MyException("不存在对应企业");
            }
        } else if (entityType == 2) {
            batchEntities = batchDao.findByTimeBetweenAndProductionLineId(start, end, entityId);
        } else if (entityType == 3) {
            batchEntities = batchDao.findByTimeBetween(start, end);
        }
        return getThroughput(batchEntities);
    }

    private ThroughputVO getThroughput(List<BatchEntity> batchEntities) {
        List<Integer> consumeAndProduceIds = new ArrayList<>();
        List<Integer> inIds = new ArrayList<>();
        List<Integer> outIds = new ArrayList<>();
        if (entityType == 0) {
            for (BatchEntity batchEntity : batchEntities) {
                if (batchEntity.getType() == BatchTypeEnum.PRODUCE.getCode() || batchEntity.getType() == BatchTypeEnum.DESTROY.getCode()) {
                    consumeAndProduceIds.add(batchEntity.getBatchId());
                } else if (batchEntity.getType() == BatchTypeEnum.IN_PARK.getCode()) {
                    inIds.add(batchEntity.getBatchId());
                } else {
                    outIds.add(batchEntity.getBatchId());
                }
                inoutBatchId2Time.put(batchEntity.getBatchId(), timestamp2String(batchEntity.getTime()));
            }
        } else if (entityType == 1 || entityType == 2) {
            for (BatchEntity batchEntity : batchEntities) {
                if (batchEntity.getType() == BatchTypeEnum.PRODUCE.getCode() || batchEntity.getType() == BatchTypeEnum.DESTROY.getCode()) {
                    consumeAndProduceIds.add(batchEntity.getBatchId());
                }
                inoutBatchId2Time.put(batchEntity.getBatchId(), timestamp2String(batchEntity.getTime()));
            }
        } else if (entityType == 3) {
            for (BatchEntity batchEntity : batchEntities) {
                if (batchEntity.getType() == BatchTypeEnum.IN_PARK.getCode() || batchEntity.getType() == BatchTypeEnum.PRODUCE.getCode()) {
                    inIds.add(batchEntity.getBatchId());
                }
                if (batchEntity.getType() == BatchTypeEnum.OUT_PARK.getCode() || batchEntity.getType() == BatchTypeEnum.PRODUCE.getCode() || batchEntity.getType() == BatchTypeEnum.DESTROY.getCode()) {
                    outIds.add(batchEntity.getBatchId());
                }
                inoutBatchId2Time.put(batchEntity.getBatchId(), timestamp2String(batchEntity.getTime()));
            }
        }
        List<String> times = getTimeList();
        times.remove(times.size() - 1);
        if (entityType != 3) {
            List<InOutBatchEntity> consume = inoutBatchDao.findByBatchIdInAndInout(consumeAndProduceIds, 1);
            List<InOutBatchEntity> produce = inoutBatchDao.findByBatchIdInAndInout(consumeAndProduceIds, 0);
            List<InOutBatchEntity> in = inoutBatchDao.findByBatchIdInAndInout(inIds, 0);
            List<InOutBatchEntity> out = inoutBatchDao.findByBatchIdInAndInout(outIds, 1);
            return new ThroughputVO(times, transfer(consume), transfer(produce), transfer(in), transfer(out));
        } else {
            List<InOutBatchEntity> consume = new ArrayList<>();
            List<InOutBatchEntity> produce = new ArrayList<>();
            List<InOutBatchEntity> in = inoutBatchDao.findByBatchIdInAndInoutAndStoreId(inIds, 0, entityId);
            List<InOutBatchEntity> in2 = expressDao.findByInputTimeBetweenAndAndInputStoreId(start, end,entityId).stream().map(e -> {
                List<InOutBatchEntity> inOutBatchEntities = new ArrayList<>();
                for (ExpressProductEntity expressProductEntity : e.getExpressProductEntities()) {
                    expressProductId2Time.put(expressProductEntity.getExpressProductId(),timestamp2String(expressProductEntity.getExpressEntity().getInputTime()));
                    InOutBatchEntity inOutBatchEntity = new InOutBatchEntity();
                    inOutBatchEntity.setProductId(expressProductEntity.getProductId());
                    inOutBatchEntity.setNumber(expressProductEntity.getNumber());
                    inOutBatchEntity.setStatus(100);//特殊标记为expressProductEntity转换过来的
                    inOutBatchEntities.add(inOutBatchEntity);
                }
                return inOutBatchEntities;
            }).flatMap(Collection::stream).collect(Collectors.toList());
            in.addAll(in2);



            List<InOutBatchEntity> out = inoutBatchDao.findByBatchIdInAndInoutAndStoreId(outIds, 1, entityId);
            List<InOutBatchEntity> out2 = expressDao.findByOutputTimeBetweenAndAndOutputStoreId(start, end,entityId).stream().map(e -> {
                List<InOutBatchEntity> inOutBatchEntities = new ArrayList<>();
                for (ExpressProductEntity expressProductEntity : e.getExpressProductEntities()) {
                    expressProductId2Time.put(expressProductEntity.getExpressProductId(),timestamp2String(expressProductEntity.getExpressEntity().getOutputTime()));
                    InOutBatchEntity inOutBatchEntity = new InOutBatchEntity();
                    inOutBatchEntity.setProductId(expressProductEntity.getProductId());
                    inOutBatchEntity.setNumber(expressProductEntity.getNumber());
                    inOutBatchEntity.setStatus(100);//特殊标记为expressProductEntity转换过来的
                    inOutBatchEntities.add(inOutBatchEntity);
                }
                return inOutBatchEntities;
            }).flatMap(Collection::stream).collect(Collectors.toList());
            out.addAll(out2);
            return new ThroughputVO(times, transfer(consume), transfer(produce), transfer(in), transfer(out));
        }
    }

    private List<CASThroughputVO> transfer(List<InOutBatchEntity> l) {
          Map<CasTimeVO, Double> map = new HashMap<>();
        Set<CasVO> casSet = new HashSet<>();
        for (InOutBatchEntity inOutBatchEntity : l) {
            int productId = inOutBatchEntity.getProductId();
            ProductEntity productEntity = productDao.findByProductId(productId);
            if (casId != 0 && casId != productEntity.getCasEntity().getCasId()) {
                continue;
            }
            String realTime="";
            if(inOutBatchEntity.getStatus()==100){
                realTime=expressProductId2Time.get(inOutBatchEntity.getBatchId());
            }else{
                realTime=inoutBatchId2Time.get(inOutBatchEntity.getBatchId());
            }
            CasTimeVO casTimeVO = new CasTimeVO(productEntity.getCasEntity().getCasId(),
                    productEntity.getCasEntity().getName(),
                    realTime);
            map.put(casTimeVO, map.getOrDefault(casTimeVO, 0.0) + inOutBatchEntity.getNumber());
            casSet.add(new CasVO(productEntity.getCasEntity().getCasId(), productEntity.getCasEntity().getName()));
        }
        List<String> times = getTimeList();
        times.remove(times.size() - 1);

        List<CASThroughputVO> result = new ArrayList<>();
        for (CasVO casVO : new ArrayList<>(casSet)) {
            List<Double> throughput = new ArrayList<>();
            for (String time : times) {
                CasTimeVO casTimeVO = new CasTimeVO(casVO.getCasId(), casVO.getName(), time);
                 throughput.add(map.getOrDefault(casTimeVO, 0.0));
            }
            CASThroughputVO casThroughputVO = new CASThroughputVO(casVO.getCasId(), casVO.getName(), throughput, throughput.stream().reduce(Double::sum).orElse(0.0));
            result.add(casThroughputVO);
        }
        return result;
    }

    private static Date String2Date(String s) {
        Date result = null;
        try {
            if (timeType == 0) {
                result = yearSdf.parse(s);
            } else if (timeType == 1) {
                String[] temp = s.split("-Q");
                String year = temp[0];
                String season = temp[1];
                Calendar cal = Calendar.getInstance();
                cal.setTime(yearSdf.parse(year));
                cal.add(Calendar.MONTH, (Integer.parseInt(season) - 1) * 3);
                return cal.getTime();
            } else if (timeType == 2) {
                result = monthSdf.parse(s);
            } else if (timeType == 3) {
                result = daysSdf.parse(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String timestamp2String(Timestamp timestamp) {
        Date date = new Date(timestamp.getTime());
        String result = null;
        try {
            if (timeType == 0) {
                result = yearSdf.format(date);
            } else if (timeType == 1) {
                result = monthSdf.format(date);
                String[] temp = result.split("-");
                String year = temp[0];
                String season = (Integer.parseInt(temp[1]) - 1) / 3 + 1 + "";
                result = year + "-Q" + season;
            } else if (timeType == 2) {
                result = monthSdf.format(date);
            } else if (timeType == 3) {
                result = daysSdf.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static List<String> getTimeList() {
        List<String> result = new ArrayList<>();
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(String2Date(startTime));
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(String2Date(endTime));
        result.add(startTime);
        if (timeType == 0) {
            while (calEnd.after(calStart)) {
                calStart.add(Calendar.YEAR, 1);
                result.add(yearSdf.format(calStart.getTime()));
            }
            calStart.add(Calendar.YEAR, 1);
            result.add(yearSdf.format(calStart.getTime()));
        } else if (timeType == 1) {
            while (calEnd.after(calStart)) {
                calStart.add(Calendar.MONTH, 3);
                result.add(timestamp2String(new Timestamp(calStart.getTime().getTime())));
            }
            calStart.add(Calendar.MONTH, 3);
            result.add(timestamp2String(new Timestamp(calStart.getTime().getTime())));
        } else if (timeType == 2) {
            while (calEnd.after(calStart)) {
                calStart.add(Calendar.MONTH, 1);
                result.add(monthSdf.format(calStart.getTime()));
            }
            calStart.add(Calendar.MONTH, 1);
            result.add(monthSdf.format(calStart.getTime()));
        } else if (timeType == 3) {
            while (calEnd.after(calStart)) {
                calStart.add(Calendar.DAY_OF_MONTH, 1);
                result.add(daysSdf.format(calStart.getTime()));
            }
            calStart.add(Calendar.DAY_OF_MONTH, 1);
            result.add(daysSdf.format(calStart.getTime()));
        }
        return result;
    }
}
