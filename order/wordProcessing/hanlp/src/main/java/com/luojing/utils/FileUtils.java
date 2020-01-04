package com.luojing.utils;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * User: 李锦卓
 * Time: 2019/8/23 22:30
 */
public class FileUtils {
    public String getConetxt(MultipartFile file) {
        String textFileName = file.getOriginalFilename();
        //创建一个空字符串存储读取到的数据
        String cnName = "";
        try {
            if(textFileName.endsWith(".doc")){ //判断文件格式
                InputStream fis = file.getInputStream();
                WordExtractor wordExtractor = new WordExtractor(fis);//使用HWPF组件中WordExtractor类从Word文档中提取文本或段落
                int i=1;
                for(String words : wordExtractor.getParagraphText()){//获取段落内容
                    cnName+=words;
                    i++;
                }
                fis.close();
            }
            if(textFileName.endsWith(".docx")){
                File uFile = new File("tempFile.docx");//创建一个临时文件
                if(!uFile.exists()){
                    uFile.createNewFile();
                }
                FileCopyUtils.copy(file.getBytes(), uFile);//复制文件内容
                OPCPackage opcPackage = POIXMLDocument.openPackage("tempFile.docx");//包含所有POI OOXML文档类的通用功能，打开一个文件包。
                XWPFDocument document = new XWPFDocument(opcPackage);//使用XWPF组件XWPFDocument类获取文档内容
                List<XWPFParagraph> paras = document.getParagraphs();
                int i=1;
                for(XWPFParagraph paragraph : paras){
                    String words = paragraph.getText();
                    cnName+=words;
                    i++;
                }
                uFile.delete();
            }
            if (textFileName.endsWith(".txt")) {
                InputStream inputStream = file.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                char[] chs = new char[1024];
                int len;
                while ((len = reader.read(chs)) != -1) {
                    cnName += new String(chs, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cnName;
    }
}