package com.dhcc;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {MQSpringBootApplication.class})
@RunWith(SpringRunner.class)
@Slf4j
public class ProducerTest {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void sendMasdsage() {
        rocketMQTemplate.convertAndSend("springboot-rocketmq",
                "Hello springboot-recketmq");
        log.info("启动成功");
    }
}
