package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {
    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Test
    public void findAll(){
        int page = 0; //从0开始
        int size = 10;  //每页记录数
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }
    @Test
    public void save(){
        //定义实体类
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("首页");
        cmsPageRepository.save(cmsPage);

    }
    @Test
    public void develop(){
        cmsPageRepository.deleteById("5c1e74347045941e10519e91");
    }
    @Test
    public void edit(){
        Optional<CmsPage> byId = cmsPageRepository.findById("5c1e74bc704594360069f73a");
        if (byId.isPresent()) {
            CmsPage cmsPage = byId.get();
            cmsPage.setPageName("首页2");
            cmsPageRepository.save(cmsPage);
        }
    }
    @Test
    public void findByPageName(){
        String pageName = "首页2";
        CmsPage byPageName = cmsPageRepository.findByPageName(pageName);
        System.out.println(byPageName);
    }
    @Test
    public void testfindAll(){
        //条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        //页面别名模糊查询
        exampleMatcher = exampleMatcher.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        //条件值
        CmsPage cmsPage = new CmsPage();
        //站点id
        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        //模板id
        cmsPage.setTemplateId("5a962c16b00ffc514038fafd");
        //创建实例
        Example<CmsPage> cmsPageExample = Example.of(cmsPage, exampleMatcher);
        Pageable pageable = new PageRequest(0, 10);
        Page<CmsPage> all = cmsPageRepository.findAll(cmsPageExample, pageable);
        System.out.println(all);
    }

}