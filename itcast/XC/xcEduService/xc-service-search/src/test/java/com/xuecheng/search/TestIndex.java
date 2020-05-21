package com.xuecheng.search;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: 李锦卓
 * Time: 2019/4/17 17:33
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestIndex {
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private RestClient restClient;
    //创建索引库
    @Test
    public void testCreateIndex() throws IOException {
        //创建索引请求对象，并设置索引名称
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("xc_course");
        //设置索引参数
        createIndexRequest.settings(Settings.builder().put("number_of_shards", 1).put("number_of_replicas", 0));
        //设置映射
        createIndexRequest.mapping("doc", "{\n" +
                "\"properties\":{\n" +
                "\"name\":{\n" +
                "\t\"type\":\"text\",\n" +
                "\t\"analyzer\":\"ik_max_word\",\n" +
                "\t\"search_analyzer\":\"ik_smart\"\n" +
                "},\n" +
                "\"description\":{\n" +
                "\t\"type\":\"text\",\n" +
                "\t\"analyzer\":\"ik_max_word\",\n" +
                "\t\"search_analyzer\":\"ik_smart\"\n" +
                "},\n" +
                "\"studymodel\":{\n" +
                "\t\"type\":\"keyword\"\n" +
                "},\n" +
                "\"price\":{\n" +
                "\t\"type\":\"float\"\n" +
                "},\n" +
                "\"timestamp\":{\n" +
                "\t\"type\":\"date\",\n" +
                "\t\"format\":\"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\"\n" +
                "}\n" +
                "}\n" +
                "}", XContentType.JSON);
        //创建索引操作客户端
        IndicesClient indicesClient = client.indices();
        //创建响应对象
        CreateIndexResponse createIndexResponse = indicesClient.create(createIndexRequest);
        //得到响应结果
        boolean a = createIndexResponse.isShardsAcknowledged();
        System.out.println(a);
    }
    //删除索引库
    @Test
    public void deleIndex() throws IOException {
        //删除索引请求对象
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("xc_course");
        //删除索引
        DeleteIndexResponse delete = client.indices().delete(deleteIndexRequest);
        //删除索引响应结果
        boolean acknowledged = delete.isAcknowledged();
        System.out.println(acknowledged);
    }
    //添加文档
    @Test
    public void testAddDoc() throws IOException {
        //准备json数据
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "spring cloud实战");
        jsonMap.put("description", "本课程主要从四个章节进行讲解： 1.微服务架构入门 2.spring cloud 基础入门3.实战Spring Boot 4.注册中心eureka。");
        jsonMap.put("studymodel", "201001");
        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jsonMap.put("timestamp", dateFormat.format(new Date()));
        jsonMap.put("price", 5.6f);
        //索引请求对象
        IndexRequest indexRequest = new IndexRequest("xc_course", "doc");
        //指定索引文档内容
        indexRequest.source(jsonMap);
        //通过client进行http的请求
        IndexResponse indexResponse = client.index(indexRequest);
        //获取响应结果
        DocWriteResponse.Result result = indexResponse.getResult();
        System.out.println(result);
    }
    //查询文档
    @Test
    public void getDoc() throws IOException {
        GetRequest getRequest = new GetRequest("xc_course", "doc", "uSrFKmoBCeOijm3mHyHi");
        GetResponse getResponse = client.get(getRequest);
        boolean exists = getResponse.isExists();
        Map<String, Object> jsonMap = getResponse.getSourceAsMap();
        System.out.println(jsonMap);
    }
    //更新文档
    @Test
    public void updateDoc() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("xc_course", "doc", "uSrFKmoBCeOijm3mHyHi");
        Map<String, String> map = new HashMap<>();
        map.put("name", "spring cloud实战");
        updateRequest.doc(map);
        UpdateResponse update = client.update(updateRequest);
        RestStatus status = update.status();
        System.out.println(status);
    }
    //删除文档
    @Test
    public void deleDoc() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("xc_course", "doc", "uSrFKmoBCeOijm3mHyHi");
        //响应对象
        DeleteResponse deleteResponse = client.delete(deleteRequest);
        DocWriteResponse.Result result = deleteResponse.getResult();
        System.out.println(result);
    }

}