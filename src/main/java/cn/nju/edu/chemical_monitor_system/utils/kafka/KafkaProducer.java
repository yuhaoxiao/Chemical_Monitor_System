package cn.nju.edu.chemical_monitor_system.utils.kafka;

import cn.nju.edu.chemical_monitor_system.constant.ConstantVariables;
import cn.nju.edu.chemical_monitor_system.vo.ExpressVO;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private static Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    public void sendExpress(ExpressVO expressVO) {
        logger.info("发送消息 ----->>>>>  message = {}", expressVO);
        kafkaTemplate.send(ConstantVariables.MANAGER_MESSAGE, JSON.toJSONString(expressVO));
    }
}