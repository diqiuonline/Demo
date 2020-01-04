package com.luojing;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * User: 李锦卓
 * Time: 2019/8/23 16:36
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class demo {
   @Test
    public void demo1(){
       String[] testCase = new String[]{
               "签约仪式前，秦光荣、李纪恒、仇和等一同会见了参加签约的企业家。",
               "王国强、高峰、汪洋、张朝阳光着头、韩寒、小四",
               "张浩和胡健康复员回家了",
               "王总和小丽结婚了",
               "编剧邵钧林和稽道青说",
               "这里有关天培的有关事迹",
               "龚学平等领导,邓颖超生前",
       };
       Segment segment = HanLP.newSegment().enableNameRecognize(true);
       for (String sentence : testCase)
       {
           List<Term> termList = segment.seg(sentence);
           System.out.println(termList);
       }
   }

}