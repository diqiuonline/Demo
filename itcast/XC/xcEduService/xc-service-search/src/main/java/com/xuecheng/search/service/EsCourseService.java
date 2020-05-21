package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: 李锦卓
 * Time: 2019/4/22 18:07
 */
@Service
@Transactional
public class EsCourseService {
    @Value("${xuecheng.course.index}")
    private String index;
    @Value("${xuecheng.media.index}")
    private String media_index;
    @Value("${xuecheng.course.type}")
    private String type;
    @Value("${xuecheng.media.type}")
    private String media_type;
    @Value("${xuecheng.course.source_field}")
    private String source_field;
    @Value("${xuecheng.media.source_field}")
    private String media_source_field;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    //课程搜索
    public QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam) {
        if (courseSearchParam == null) {
            courseSearchParam = new CourseSearchParam();
        }
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //过滤源字段
        String[] source_fieId_array = source_field.split(",");
        searchSourceBuilder.fetchSource(source_fieId_array, new String[]{});
        //创建布尔查询对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //搜索条件，根据关键字搜索
        if (StringUtils.isNotEmpty(courseSearchParam.getKeyword())) {
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(courseSearchParam.
                    getKeyword(), "name", "description", "teachplan")
                    .minimumShouldMatch("70%").field("name", 10);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }
        //根据一级分类
        if (StringUtils.isNotEmpty(courseSearchParam.getMt())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt", courseSearchParam.getMt()));
        }
        //根据二级分类
        if (StringUtils.isNotEmpty(courseSearchParam.getSt())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("st", courseSearchParam.getSt()));
        }
        //难度等级
        if (StringUtils.isNotEmpty(courseSearchParam.getGrade())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("grade", courseSearchParam.getGrade()));
        }
        //分页
        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 20;
        }
        int start = (page - 1) * size;
        searchSourceBuilder.from(start);
        searchSourceBuilder.size(size);
        //布尔查询
        searchSourceBuilder.query(boolQueryBuilder);
        //高亮设置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        //高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        searchSourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(searchSourceBuilder);
        QueryResult<CoursePub> queryResult = new QueryResult<>();
        ArrayList<CoursePub> list = new ArrayList<>();
        try {
            //执行搜索
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            //获取响应结果
            SearchHits hits = searchResponse.getHits();
            //匹配的总记录数
            long totalHits = hits.totalHits;
            queryResult.setTotal(totalHits);
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                CoursePub coursePub = new CoursePub();
                //原文档
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                //取出id
                String id = (String) sourceAsMap.get("id");
                coursePub.setId(id);
                //取出name
                String name = (String) sourceAsMap.get("name");
                //取出高亮字段
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                if (highlightFields != null) {
                    HighlightField highlightFieldName = highlightFields.get("name");
                    if (highlightFieldName != null) {
                        Text[] fragments = highlightFieldName.getFragments();
                        StringBuffer stringBuffer = new StringBuffer();
                        for (Text te : fragments) {
                            stringBuffer.append(te);
                        }
                        name = stringBuffer.toString();
                    }
                }
                coursePub.setName(name);
                //图片
                String pic = (String) sourceAsMap.get("pic");
                coursePub.setPic(pic);
                //价格
                if (sourceAsMap.get("price") != null) {
                    Double price = (Double) sourceAsMap.get("price");
                    coursePub.setPrice(price);
                }
                //旧价格
                if (sourceAsMap.get("price_old") != null) {
                    Double price_old = (Double) sourceAsMap.get("price_old");
                    coursePub.setPrice_old(price_old);
                }
                list.add(coursePub);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        queryResult.setList(list);
        return new QueryResponseResult<CoursePub>(CommonCode.SUCCESS,queryResult);
    }

    //从ES的客户端向ES请求查询索引信息
    public Map<String, CoursePub> getall(String courseId) {
        //定义一个搜索请求对象
        SearchRequest searchRequest = new SearchRequest(index);
        //定义type
        searchRequest.types(type);
        //定义searchSourcebuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置termquery
        searchSourceBuilder.query(QueryBuilders.termQuery("id", courseId));
        //过虑源字段，不用设置源字段，取出所有字段
        searchRequest.source(searchSourceBuilder);
        //最终要返回的课程信息
        Map<String, CoursePub> map = new HashMap<>();
        try {
            SearchResponse search = restHighLevelClient.search(searchRequest);
            SearchHits hits = search.getHits();
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                CoursePub coursePub = new CoursePub();
                //获取源文档的内容
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                String name = (String) sourceAsMap.get("name");
                String grade = (String) sourceAsMap.get("grade");
                String charge = (String) sourceAsMap.get("charge");
                String pic = (String) sourceAsMap.get("pic");
                String description = (String) sourceAsMap.get("description");
                String teachplan = (String) sourceAsMap.get("teachplan");
                coursePub.setId(courseId);
                coursePub.setName(name);
                coursePub.setPic(pic);
                coursePub.setGrade(grade);
                coursePub.setTeachplan(teachplan);
                coursePub.setDescription(description);
                map.put(courseId, coursePub);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    //根据多个课程计划id查询媒资信息
    public QueryResponseResult<TeachplanMediaPub> getMedia(String[] teachplanIds) {
        //定义一个搜索对象
        SearchRequest searchRequest = new SearchRequest(media_index);
        //指定type
        searchRequest.types(media_type);
        //定义searchsourcebuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置termsquery根据多个id查询
        searchSourceBuilder.query(QueryBuilders.termsQuery("teachplan_id", teachplanIds));
        //过滤源字段
        String[] includes = media_source_field.split(",");
        searchSourceBuilder.fetchSource(includes, new String[]{});
        searchRequest.source(searchSourceBuilder);
        //使用es客户端请求es
        List<TeachplanMediaPub> teachplanMediaPubList = new ArrayList<>();
        long total = 0;
        try {
            SearchResponse search = restHighLevelClient.search(searchRequest);
            SearchHits hits = search.getHits();
            total = hits.totalHits;
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                //取出课程计划媒资信息
                String courseid = (String) sourceAsMap.get("courseid");
                String media_id = (String) sourceAsMap.get("media_id");
                String media_url = (String) sourceAsMap.get("media_url");
                String teachplan_id = (String) sourceAsMap.get("teachplan_id");
                String media_fileoriginalname = (String) sourceAsMap.get("media_fileoriginalname");

                teachplanMediaPub.setCourseId(courseid);
                teachplanMediaPub.setMediaUrl(media_url);
                teachplanMediaPub.setMediaFileOriginalName(media_fileoriginalname);
                teachplanMediaPub.setMediaId(media_id);
                teachplanMediaPub.setTeachplanId(teachplan_id);
                teachplanMediaPubList.add(teachplanMediaPub);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //数据集合
        QueryResult<TeachplanMediaPub> queryResult = new QueryResult<>();
        queryResult.setTotal(total);
        queryResult.setList(teachplanMediaPubList);
        return new QueryResponseResult<>(CommonCode.SUCCESS, queryResult);
    }


}