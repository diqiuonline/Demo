package com.dhcc.Context;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dhcc.Context.dao.ContextMapper;
import com.dhcc.Context.domain.Context;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/3/25 18:15
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class htmlText {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ContextMapper contextMapper;


    @Test
    @Transactional
    @Rollback(false)
    public void demo() throws Exception {
        //登陆url
        String url = "http://www.lkong.net/member.php?mod=logging&action=login&loginsubmit=yes&loginhash=LZqnM&inajax=1";
        //header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        //body
        MultiValueMap map = new LinkedMultiValueMap();
        map.add("username", "漫威");
        map.add("password", "nanguo@11.");
        map.add("answer", "diqiuonline@gmail.com");
        //HttpEntity
        HttpEntity<MultiValueMap> requestBody = new HttpEntity<MultiValueMap>(map, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST,requestBody, String.class);
        HttpHeaders responseEntityHeaders = responseEntity.getHeaders();
        String cookie = "";
        List<String> cookieList = responseEntityHeaders.get("Set-Cookie");
        for (String list1 : cookieList) {
            cookie += list1+"; ";
        }
        for (int j = 201; j <=216; j++) {
            //获取内容
            //String getContextUrl = "http://www.lkong.net/thread-196993-"+j+"-1.html";
            String getContextUrl = "http://www.lkong.net/thread-2205892-"+j+"-1.html";
            headers.add("cookie",cookie);
            HttpEntity<MultiValueMap> requestBody1 = new HttpEntity<MultiValueMap>(headers);
            ResponseEntity<String> responseEntity1 = restTemplate.exchange(getContextUrl, HttpMethod.GET,requestBody1,String.class);
            String body = responseEntity1.getBody();
            Document document = Jsoup.parse(body);
            //Elements mes = document.getElementsByClass("xw1");
            Elements text = document.getElementsByClass("t_f");
            Elements xg1 = document.getElementsByClass("authi");
            Elements brm = document.getElementsByClass("brm");

            for (int i = 0; i < text.size(); i++) {
                Context context = new Context();
                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse((xg1.get(i * 2 + 1).getElementsByTag("em").get(0).text()).substring(4));
                if (j == 1 && i <= 4) {
                    context.setId(i +1);
                    context.setUsername(xg1.get(i * 2).getElementsByClass("xw1").get(0).text());
                    context.setContext(text.get(i).text());
                    context.setDatetime(date);
                } else {
                    context.setId(Integer.valueOf(brm.get(i).getElementsByTag("em").get(0).text()));
                    context.setUsername(xg1.get(i * 2).getElementsByClass("xw1").get(0).text());
                    context.setContext(text.get(i).text());
                    context.setDatetime(date);
                }
                //contextMapper.insert(context);

                Context one = contextMapper.selectOne(new LambdaQueryWrapper<Context>().eq(Context::getDatetime, date));
                if (StringUtils.isEmpty(one)) {
                    contextMapper.insert(context);
                } else {
                    context.setId(one.getId());
                    contextMapper.updateById(context);
                }
                //System.out.println("第"+j+"页第"+(i+1)+"楼 用户:"+xg1.get(i * 2).getElementsByClass("xw1").get(0).text()+" 时间:"+xg1.get(i * 2 + 1).getElementsByTag("em").get(0).text()+"  内容："+text.get(i).text());
            }
            //Thread.sleep(5000);
            //System.out.println(body);
        }






    }

    @Test
    public void demo2() {
        Context context = new Context();
        context.setId(2);
        Context context1 = context.selectById();
        System.out.println(context1);
    }

    private String filterContext(String htmlStr) {
        String textStr = "";
        Pattern p_script;
        java.util.regex.Matcher m_script;
        Pattern p_style;
        java.util.regex.Matcher m_style;
        Pattern p_html;
        java.util.regex.Matcher m_html;
        String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
        String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
        p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签
        p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签
        p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签
        textStr = htmlStr;
        //剔除空格行
        textStr = textStr.replaceAll("[ ]+", " ");
        textStr = textStr.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");
        return textStr;// 返回文本字符串



    }
}
