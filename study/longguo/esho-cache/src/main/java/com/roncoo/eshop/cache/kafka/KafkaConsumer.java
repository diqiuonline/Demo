package com.roncoo.eshop.cache.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @Author 李锦卓
 * @Description kafka消费者
 * @Date 2022/7/17 20:23
 * @Version 1.0
 */
public class KafkaConsumer implements Runnable{
    private ConsumerConnector connector;
    private String topic;

    public KafkaConsumer(String topic) {
        this.connector = Consumer.createJavaConsumerConnector(createConsumerConfig());
        this.topic = topic;
    }


    @Override
    public void run() {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = connector.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        for (KafkaStream stream : streams) {
            new Thread(new KafkaMessageProcessor(stream)).start();
        }
    }

    /**
     * 创建kafka consumer config
     * @return
     */
    private static ConsumerConfig createConsumerConfig() {
        Properties props = new Properties();
        props.put("zookeeper.connect", "192.168.2.205:2181,192.168.2.205:2182,192.168.2.205:2183");
        props.put("group.id", "eshop-cache-group");
        props.put("zookeeper.session.timeout.ms", "4000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        return new ConsumerConfig(props);
    }



























}
