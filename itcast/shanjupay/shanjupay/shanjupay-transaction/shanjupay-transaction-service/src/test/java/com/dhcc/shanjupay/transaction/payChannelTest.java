package com.dhcc.shanjupay.transaction;

import com.dhcc.shanjupay.transaction.api.PayChannelService;
import com.dhcc.shanjupay.transaction.api.dto.PayChannelDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class payChannelTest {
    @Autowired
    private PayChannelService payChannelService;

    @Test
    public void demo1() {
        List<PayChannelDTO> shanju_c2b = payChannelService.queryPayChannelByPlatformChannel("shanju_c2b");
        System.out.println(shanju_c2b.toString());
    }

}