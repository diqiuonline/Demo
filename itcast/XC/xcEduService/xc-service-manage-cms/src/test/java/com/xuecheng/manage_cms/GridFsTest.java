
package com.xuecheng.manage_cms;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * User: 李锦卓
 * Time: 2019/1/13 23:19
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFsTest {
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;


/**
     * 存入文件
     * @throws Exception
     */

    @Test
    public void testGridFs() throws Exception {
        //要存入的文件
        File file = new File("d:/course.ftl");
        //定义输入流
        FileInputStream fileInputStream = new FileInputStream(file);
        //向gridFs中存入文件
        ObjectId store = gridFsTemplate.store(fileInputStream, "课程详情模板文件", "");
        //得到文件id
        String s = store.toString(); //5cdbcb345dd2ee072c188a4f
        System.out.println(s);
    }

/**
     * 查询文件
     * @throws IOException
     */

    @Test
    public void queryFile() throws IOException {
        String fileId = "5c3c92f870459410f4fb62e0";
        //根据id查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        //打开下载流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建GridFsResource 用于获取流对象
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        //获取流中的数据
        String s = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        System.out.println(s);
    }

/**
     * 删除文件
     */

    @Test
    public void deleFile(){
        String fileId = "5cdbcdd45dd2ee1c8071da59";
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is(fileId)));
    }
}
