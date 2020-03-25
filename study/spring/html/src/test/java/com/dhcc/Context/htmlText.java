package com.dhcc.Context;

import com.dhcc.Context.dao.ContextMapper;
import com.dhcc.Context.domain.Context;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
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
        for (int j = 21; j <= 30; j++) {
            //获取内容
            String getContextUrl = "http://www.lkong.net/thread-196993-"+j+"-1.html";
            headers.add("cookie",cookie);
            HttpEntity<MultiValueMap> requestBody1 = new HttpEntity<MultiValueMap>(headers);
            ResponseEntity<String> responseEntity1 = restTemplate.exchange(getContextUrl, HttpMethod.GET,requestBody1,String.class);
            String body = responseEntity1.getBody();
            Document document = Jsoup.parse(body);
            Elements mes = document.getElementsByClass("postmessage");
            Elements xw1 = document.getElementsByClass("xw1");
            Elements xg1 = document.getElementsByClass("xg1");
            for (int i = 0; i < mes.size(); i++) {
                Context context = new Context();
                context.setUsername(xw1.get(i).text());
                context.setContext(mes.get(i).text());
                context.setDatetime(new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(xg1.get(i).text()));
                contextMapper.insert(context);
                System.out.println("第"+j+"页第"+(i+1)+"楼 用户:"+xw1.get(i).text()+" 时间:"+xg1.get(i).text()+"  内容："+mes.get(i).text());
            }
            //Thread.sleep(5000);
            //System.out.println(body);
        }






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
