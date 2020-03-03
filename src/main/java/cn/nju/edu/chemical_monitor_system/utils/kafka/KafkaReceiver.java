package cn.nju.edu.chemical_monitor_system.utils.kafka;

import java.util.Optional;

import cn.nju.edu.chemical_monitor_system.constant.ConstantVariables;
import cn.nju.edu.chemical_monitor_system.utils.socket.WebSocketUtil;
import cn.nju.edu.chemical_monitor_system.vo.ExpressVO;
import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;

@Component(value = "kafkaReceiver")
public class KafkaReceiver {

    @Autowired
    WebSocketUtil webSocketUtil;
    @Autowired
    private KafkaListenerEndpointRegistry registry;
    private static Logger logger = LoggerFactory.getLogger(KafkaReceiver.class);
    static final String id=ConstantVariables.KAFKA_LISTENER_ID;

    @KafkaListener(id = id,topics = {"ManagerMessage"})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            ExpressVO expressVO = JSON.parseObject(message.toString(), ExpressVO.class);
            webSocketUtil.sendInfo(JSON.toJSONString(message));
            logger.info("----------------- record =" + record);
            logger.info("------------------ message =" + expressVO);
        }
    }
    public void stop() {
        logger.info("KafkaListener stop...");
        registry.getListenerContainer(id).stop();
    }

    public void start() {
        logger.info("KafkaListener start...");
        registry.getListenerContainer(id).start();
    }
}
