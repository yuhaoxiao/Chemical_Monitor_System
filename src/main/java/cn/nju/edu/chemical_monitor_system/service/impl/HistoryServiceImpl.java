package cn.nju.edu.chemical_monitor_system.service.impl;

import cn.nju.edu.chemical_monitor_system.constant.HistoryEnum;
import cn.nju.edu.chemical_monitor_system.dao.*;
import cn.nju.edu.chemical_monitor_system.entity.ExpressProductEntity;
import cn.nju.edu.chemical_monitor_system.entity.InOutBatchEntity;
import cn.nju.edu.chemical_monitor_system.service.HistoryService;
import cn.nju.edu.chemical_monitor_system.utils.history.HistoryNode;
import cn.nju.edu.chemical_monitor_system.vo.LinkVO;
import cn.nju.edu.chemical_monitor_system.vo.NodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class HistoryServiceImpl implements HistoryService {
    @Autowired
    InoutBatchDao inoutBatchDao;
    @Autowired
    BatchDao batchDao;
    @Autowired
    ExpressProductDao expressProductDao;
    @Autowired
    CasDao casDao;
    @Autowired
    StoreDao storeDao;
    @Autowired
    ProductDao productDao;
    //第一次生成树的时候防止batch重复
    private HashMap<Integer, HistoryNode> hashMap = new HashMap<>();
    //NodeVO->nodeVO在list中index
    private static HashMap<NodeVO,Integer> index = new HashMap<>();
    private static List<NodeVO> nodes=new ArrayList<>();
    private static List<LinkVO> links=new ArrayList<>();
    //struct为0表示原料历史，为1表示产品用途
    private int struct;

    private static HashSet<Integer> expressIds=new HashSet<>();

    @Override
    public Map<String,Map> getHistory(int batchId){
        HistoryNode historyNode=getBeforeHistory(batchId,0);
        goHistory(historyNode);
        Map<String, List> data0 = new HashMap<>();
        data0.put("nodes", new ArrayList<>(nodes));
        data0.put("links", new ArrayList<>(links));

        nodes.clear();
        links.clear();
        expressIds.clear();

        historyNode=getBeforeHistory(batchId,1);
        goHistory(historyNode);
        Map<String, List> data1 = new HashMap<>();
        data1.put("nodes", new ArrayList<>(nodes));
        data1.put("links", new ArrayList<>(links));

        Map<String, Map> data = new HashMap<>();
        data.put("raws", data0);
        data.put("products", data1);
        return data;
    }
    private void goHistory(HistoryNode historyNode){
        int fromIndex=getIndex(historyNode);
        List<HistoryNode> historyNodes=historyNode.getHistoryNodes();
        if(historyNodes!=null) {
            for (int i = 0; i < historyNodes.size(); i++) {
                HistoryNode temp=historyNodes.get(i);
                int toIndex=getIndex(temp);
                LinkVO linkVO=new LinkVO(fromIndex,toIndex,historyNode.getNums().get(i));
                links.add(linkVO);
                goHistory(historyNode);
            }
        }
    }
    private int getIndex(HistoryNode historyNode){
        NodeVO nodeVO=historyNode2NodeVO(historyNode);
        if(!nodes.contains(nodeVO)){
            index.put(nodeVO,nodes.size());
            nodes.add(nodeVO);
        }
        return index.get(nodeVO);
    }
    private NodeVO historyNode2NodeVO(HistoryNode historyNode){
        NodeVO nodeVO=new NodeVO();
        nodeVO.setBatchId(historyNode.getBatchId());
        nodeVO.setType(historyNode.getType());
        if(historyNode.getType()!=2) {
            nodeVO.setProductName(productDao.findByProductId(historyNode.getProductId()).getCasEntity().getName());
            nodeVO.setStoreName(storeDao.findFirstByStoreId(historyNode.getStoreId()).getName());
        }
        else{
            nodeVO.setBatchType(batchDao.findFirstByBatchId(historyNode.getBatchId()).getType());
        }
        return nodeVO;
    }
    public HistoryNode getBeforeHistory(int batchId,int struct) {
        this.struct=struct;
        hashMap.clear();
        index.clear();
        HistoryNode historyNode = new HistoryNode();
        historyNode.setBatchId(batchId);
        historyNode.setType(HistoryEnum.BATCH.getCode());
        List<Double> nums = new ArrayList<>();
        //找出批次的所有原材料
        List<InOutBatchEntity> inOutBatchEntities = inoutBatchDao.findByBatchIdAndInout(batchId, 1-struct);
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
                    temp.getBatchId(), temp.getProductId(), temp.getStoreId(), struct);
            //确认是生产出来的,构建原材料list
            if (inOutBatchEntities.size() != 0) {
                HistoryNode batch;//前/后继批次节点
                //如果之前已经生成批次节点
                if (hashMap.containsKey(temp.getBatchId())) {
                    batch = hashMap.get(temp.getBatchId());
                } else {
                    //如果没有生成批次节点，则加上前继批次节点，并在批次节点加上前继产品
                    List<HistoryNode> historyNodes=inoutBatchDao.findByBatchIdAndInout(temp.getBatchId(), 1-struct).stream().map(e->{
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
                List<ExpressProductEntity> expressProductEntities;
                //过滤，剩下那些仓库信息符合的物流
                if(struct==0) {
                    expressProductEntities= expressProductDao.findByProductId(temp.getProductId()).stream()
                            .filter(e -> e.getExpressEntity().getInputStoreId() == temp.getStoreId()
                                    &&(!expressIds.contains(e.getExpressEntity().getExpressId())))
                            .collect(Collectors.toList());
                }else{
                    expressProductEntities= expressProductDao.findByProductId(temp.getProductId()).stream()
                            .filter(e -> e.getExpressEntity().getOutputStoreId() == temp.getStoreId()
                                    &&(!expressIds.contains(e.getExpressEntity().getExpressId())))
                            .collect(Collectors.toList());
                }
                if (expressProductEntities.size() != 0) {
                    List<HistoryNode> historyNodes = new ArrayList<>();
                    List<Double> l = new ArrayList<>();
                    for(ExpressProductEntity expressProductEntity:expressProductEntities) {
                        HistoryNode h = new HistoryNode();
                        //更新storeId、batchId才能进一步递归查出新数据
                        h.setNumber(expressProductEntity.getNumber());
                        if (struct == 0) {
                            h.setStoreId(expressProductEntity.getExpressEntity().getOutputStoreId());
                        } else {
                            h.setStoreId(expressProductEntity.getExpressEntity().getInputStoreId());
                        }
                        h.setBatchId(temp.getBatchId());
                        h.setProductId(temp.getProductId());
                        h.setType(HistoryEnum.PRODUCT.getCode());
                        historyNodes.add(h);
                        l.add(expressProductEntity.getNumber());
                        expressIds.add(expressProductEntity.getExpressEntity().getExpressId());
                    }
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
