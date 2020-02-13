package cn.nju.edu.chemical_monitor_system.utils.batch_history_util;

import cn.nju.edu.chemical_monitor_system.constant.HistoryEnum;
import cn.nju.edu.chemical_monitor_system.dao.BatchDao;
import cn.nju.edu.chemical_monitor_system.dao.ExpressDao;
import cn.nju.edu.chemical_monitor_system.dao.ExpressProductDao;
import cn.nju.edu.chemical_monitor_system.dao.InoutBatchDao;
import cn.nju.edu.chemical_monitor_system.entity.ExpressEntity;
import cn.nju.edu.chemical_monitor_system.entity.ExpressProductEntity;
import cn.nju.edu.chemical_monitor_system.entity.InOutBatchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BatchHistoryUtil {
    @Autowired
    InoutBatchDao inoutBatchDao;
    @Autowired
    BatchDao batchDao;
    @Autowired
    ExpressDao expressDao;
    @Autowired
    ExpressProductDao expressProductDao;

    public HistoryNode getBeforeHistory(int batchId) {
        HistoryNode historyNode = new HistoryNode();
        historyNode.setBatchId(batchId);
        //找出批次的所有原材料
        List<InOutBatchEntity> inOutBatchEntities = inoutBatchDao.findByBatchIdAndInout(batchId, 1);
        List<HistoryNode> historyNodes = inOutBatchEntities.stream().map(HistoryNode::new).collect(Collectors.toList());
        historyNode.setHistoryNodes(historyNodes);
        goBeforeHistory(historyNode);
        return historyNode;
    }

    public void goBeforeHistory(HistoryNode historyNode) {
        for (HistoryNode temp : historyNode.getHistoryNodes()) {
            //先查一下有没有生产信息,一定要加上storeId的判断，防止跳过所有的物流操作
            List<InOutBatchEntity> inOutBatchEntities = inoutBatchDao.findByBatchIdAndInoutAndProductIdAndStoreId(
                    temp.getBatchId(), temp.getProductId(), temp.getStoreId(), 0);
            //确认是生产出来的
            if (inOutBatchEntities.size() != 0) {
                List<HistoryNode> historyNodes = new ArrayList<>();
                for (InOutBatchEntity inOutBatchEntity : inoutBatchDao
                        .findByBatchIdAndInoutAndProductIdAndStoreId(temp.getBatchId(), temp.getProductId(), temp.getStoreId(), 1)) {
                    HistoryNode node = new HistoryNode(inOutBatchEntity);
                    node.setType(HistoryEnum.REACTANT.getCode());
                    historyNodes.add(node);
                }
                temp.setHistoryNodes(historyNodes);
                goBeforeHistory(historyNode);
            }else{
                //过滤，剩下那些仓库信息符合的物流
                List<ExpressProductEntity> expressProductEntities=expressProductDao.findByProductId(temp.getProductId()).stream()
                        .filter(e-> e.getExpressEntity().getOutputStoreId()==temp.getStoreId()).collect(Collectors.toList());
                if(expressProductEntities.size()!=0){
                    List<HistoryNode> historyNodes=new ArrayList<>();
                    HistoryNode h=new HistoryNode();
                    ExpressProductEntity expressProductEntity=expressProductEntities.get(0);//原则上也就只能查出一个
                    ////更新storeId才能进一步递归查出新数据
                    historyNode.setNumber(expressProductEntity.getNumber());
                    historyNode.setStoreId(expressProductEntity.getExpressEntity().getInputStoreId());
                    historyNode.setBatchId(temp.getBatchId());
                    historyNode.setProductId(temp.getProductId());
                    historyNode.setType(HistoryEnum.PRODUCT.getCode());
                    historyNodes.add(h);
                    temp.setHistoryNodes(historyNodes);
                    goBeforeHistory(historyNode);
                }
            }
        }
    }
}

