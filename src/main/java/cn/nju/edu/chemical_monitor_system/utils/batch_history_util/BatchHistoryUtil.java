package cn.nju.edu.chemical_monitor_system.utils.batch_history_util;

import cn.nju.edu.chemical_monitor_system.constant.HistoryEnum;
import cn.nju.edu.chemical_monitor_system.dao.BatchDao;
import cn.nju.edu.chemical_monitor_system.dao.ExpressProductDao;
import cn.nju.edu.chemical_monitor_system.dao.InoutBatchDao;
import cn.nju.edu.chemical_monitor_system.dao.ProductDao;
import cn.nju.edu.chemical_monitor_system.entity.ExpressProductEntity;
import cn.nju.edu.chemical_monitor_system.entity.InOutBatchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BatchHistoryUtil {
    @Autowired
    InoutBatchDao inoutBatchDao;
    @Autowired
    BatchDao batchDao;
    @Autowired
    ExpressProductDao expressProductDao;
    @Autowired
    ProductDao productDao;
    private HashMap<Integer, HistoryNode> hashMap = new HashMap<>();

    public HistoryNode getBeforeHistory(int batchId) {
        hashMap.clear();
        HistoryNode historyNode = new HistoryNode();
        historyNode.setBatchId(batchId);
        historyNode.setType(HistoryEnum.BATCH.getCode());
        List<Double> nums = new ArrayList<>();
        //找出批次的所有原材料
        List<InOutBatchEntity> inOutBatchEntities = inoutBatchDao.findByBatchIdAndInout(batchId, 1);
        List<HistoryNode> historyNodes = inOutBatchEntities.stream().map(e -> {
            HistoryNode temp =toHistoryNode(e);
            temp.setBatchId(productDao.findByProductId(e.getProductId()).getBatchId());
            nums.add(e.getNumber());
            return temp;
        }).collect(Collectors.toList());
        historyNode.setHistoryNodes(historyNodes);
        historyNode.setNums(nums);
        goBeforeHistory(historyNode);
        return historyNode;
    }

    private void goBeforeHistory(HistoryNode historyNode) {
        if (historyNode.getVisit() == 1) {
            return;
        }
        for (HistoryNode temp : historyNode.getHistoryNodes()) {
            List<Double> nums = new ArrayList<>();
            //先查一下有没有生产信息,一定要加上storeId的判断，防止跳过所有的物流操作
            List<InOutBatchEntity> inOutBatchEntities = inoutBatchDao.findByBatchIdAndProductIdAndStoreIdAndInout(
                    temp.getBatchId(), temp.getProductId(), temp.getStoreId(), 0);
            //确认是生产出来的,构建原材料list
            if (inOutBatchEntities.size() != 0) {
                HistoryNode batch;//前继批次节点
                //如果之前已经生成批次节点
                if (hashMap.containsKey(temp.getBatchId())) {
                    batch = hashMap.get(temp.getBatchId());
                } else {
                    //如果没有生成批次节点，则加上前继批次节点，并未批次节点加上前继产品
                    List<HistoryNode> historyNodes=inoutBatchDao.findByBatchIdAndInout(temp.getBatchId(), 1).stream().map(e->{
                        HistoryNode node=toHistoryNode(e);
                        node.setBatchId(productDao.findByProductId(e.getProductId()).getBatchId());
                        nums.add(e.getNumber());
                        return node;
                    }).collect(Collectors.toList());
                    //创建中继批次节点
                    batch = copy(temp);
                    batch.setHistoryNodes(historyNodes);
                    batch.setNums(nums);
                    hashMap.put(temp.getBatchId(), batch);//加入map防止批次节点重复
                }
                List<Double> l = new ArrayList<>();
                l.add(inOutBatchEntities.get(0).getNumber());
                List<HistoryNode> beforeList = new ArrayList<>();
                beforeList.add(batch);
                temp.setHistoryNodes(beforeList);
                temp.setNums(l);
                temp.setVisit(1);
                goBeforeHistory(batch);
            } else {
                //过滤，剩下那些仓库信息符合的物流
                List<ExpressProductEntity> expressProductEntities = expressProductDao.findByProductId(temp.getProductId()).stream()
                        .filter(e -> e.getExpressEntity().getInputStoreId() == temp.getStoreId()).collect(Collectors.toList());
                if (expressProductEntities.size() != 0) {
                    List<HistoryNode> historyNodes = new ArrayList<>();
                    List<Double> l=new ArrayList<>();
                    HistoryNode h = new HistoryNode();
                    ExpressProductEntity expressProductEntity = expressProductEntities.get(0);//原则上也就只能查出一个
                    //更新storeId、batchId才能进一步递归查出新数据
                    h.setNumber(expressProductEntity.getNumber());
                    l.add(expressProductEntity.getNumber());
                    h.setStoreId(expressProductEntity.getExpressEntity().getOutputStoreId());
                    h.setBatchId(temp.getBatchId());
                    h.setProductId(temp.getProductId());
                    h.setType(HistoryEnum.PRODUCT.getCode());
                    historyNodes.add(h);
                    temp.setHistoryNodes(historyNodes);
                    temp.setNums(l);
                    goBeforeHistory(temp);
                }
            }
        }
        historyNode.setVisit(1);
    }

    private HistoryNode copy(HistoryNode historyNode) {
        HistoryNode copyNode = new HistoryNode();
        copyNode.setType(HistoryEnum.BATCH.getCode());//中继节点
        copyNode.setProductId(historyNode.getProductId());
        copyNode.setBatchId(historyNode.getBatchId());
        copyNode.setStoreId(historyNode.getStoreId());
        copyNode.setNumber(historyNode.getNumber());
        return copyNode;
    }
    private static HistoryNode toHistoryNode(InOutBatchEntity inOutBatchEntity){
        HistoryNode node = new HistoryNode();
        node.setProductId(inOutBatchEntity.getProductId());
        node.setNumber(inOutBatchEntity.getNumber());
        node.setType(HistoryEnum.PRODUCT.getCode());//设置该节点是产品
        node.setStoreId(inOutBatchEntity.getStoreId());
        return node;
    }

}

