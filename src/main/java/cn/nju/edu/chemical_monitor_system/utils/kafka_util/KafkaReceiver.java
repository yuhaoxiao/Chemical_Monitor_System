package cn.nju.edu.chemical_monitor_system.utils.kafka_util;

import java.util.Optional;

import cn.nju.edu.chemical_monitor_system.utils.web_socket_util.WebSocketUtil;
import cn.nju.edu.chemical_monitor_system.vo.ExpressVO;
import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.hibernate.boot.spi.InFlightMetadataCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaReceiver {

    @Autowired
    WebSocketUtil webSocketUtil;
    private static Logger logger = LoggerFactory.getLogger(KafkaReceiver.class);

    @KafkaListener(topics = {"ManagerMessage"})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            ExpressVO expressVO = JSON.parseObject(message.toString(), ExpressVO.class);
            webSocketUtil.sendInfo(JSON.toJSONString(expressVO));
            logger.info("----------------- record =" + record);
            logger.info("------------------ message =" + expressVO);
        }
    }
}
