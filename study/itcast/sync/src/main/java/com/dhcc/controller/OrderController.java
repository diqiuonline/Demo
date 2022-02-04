package com.dhcc.controller;

import com.dhcc.util.SynchronizedByKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrderController {
    Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    SynchronizedByKey synchronizedByKey;


    @ResponseBody
    @RequestMapping(value = "/process/{orerId}")
    public void processs(@PathVariable("orerId") String orderId)  {

        synchronizedByKey.exec(orderId,()->{
            logger.info("[{}] 开始", orderId);
            try {
                //Thread.sleep(1500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("[{}] 结束", orderId);

        });



    }
}
