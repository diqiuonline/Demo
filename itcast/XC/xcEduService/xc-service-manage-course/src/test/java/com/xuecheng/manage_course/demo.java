package com.xuecheng.manage_course;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_course.client.CmsPageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * User:李锦卓
 * Time:2019/4/4 17:01
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class demo {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CmsPageClient cmsPageClient;
    @Test
    public void demo1() {
        //服务id
        String serviceId = "XC-SERVICE-MANAGE-CMS";
        for (int i = 1; i <= 10; i++) {
            //通过服务id调用
            ResponseEntity<CmsPage> entity = restTemplate.getForEntity("http://" + serviceId +
                    "/cms/page/get/5a795ac7dd573c04508f3a56", CmsPage.class);
            CmsPage body = entity.getBody();
            System.out.println(body);

        }
    }
    @Test
    public void demo2(){

        for (int i = 1; i <= 10; i++) {
            CmsPage cmsPageById = cmsPageClient.findCmsPageById("5a795ac7dd573c04508f3a56");
            System.out.println(cmsPageById);

        }
    }


}