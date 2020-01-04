package com.dhcc.demo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * User: 李锦卓
 * Time: 2018/12/16 17:40
 */
public class demo {
    public static void main(String[] args) {
        //创建mongo客户端
        MongoClient mongoClient = new MongoClient("127.0.0.1", 27017);
        //链接数据库
        MongoDatabase database = mongoClient.getDatabase("xc_cms");
        //链接collection
        MongoCollection<Document> collection = database.getCollection("cms_page");
        //查看第一个文档
        Document first = collection.find().first();
        //得到文档内容json字符串
        String s = first.toJson();
        System.out.println(s);

    }
}