package com.luojing.controller;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.py.Pinyin;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.luojing.api.DiscernControllerApi;
import com.luojing.domain.Responses;
import com.luojing.utils.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * User: 李锦卓
 * Time: 2019/8/23 16:51
 */
@RestController
@RequestMapping("/luojing/discern")
public class HanlpController implements DiscernControllerApi {

    @Override
    @PostMapping("/agjname")
    public Responses gjName(MultipartFile file) {
        FileUtils fileUtils = new FileUtils();
        String cnName = fileUtils.getConetxt(file);
        List<String> keywordList = HanLP.extractKeyword(cnName, 5);
        Responses responses = new Responses();
        responses.setData(keywordList.toString());
        return responses;
    }

    @Override
    @PostMapping("/bdytq")
    public Responses dytq(MultipartFile file) {
        FileUtils fileUtils = new FileUtils();
        String cnName = fileUtils.getConetxt(file);
        List<String> phraseList = HanLP.extractPhrase(cnName, 10);
        Responses responses = new Responses();
        responses.setData(phraseList.toString());
        return responses;
    }

    @Override
    @PostMapping("/czdzy")
    public Responses zdzy(MultipartFile file) {
        FileUtils fileUtils = new FileUtils();
        String cnName = fileUtils.getConetxt(file);
        List<String> sentenceList = HanLP.extractSummary(cnName, 3);
        Responses responses = new Responses();
        responses.setData(sentenceList.toString());
        return responses;
    }

    @Override
    @PostMapping("/dyyname")
    public Responses yyName(MultipartFile file) {
        FileUtils fileUtils = new FileUtils();
        String cnName = fileUtils.getConetxt(file);
        Segment segment = HanLP.newSegment().enableTranslatedNameRecognize(true);
        List<Term> termList = segment.seg(cnName);
        String cnname = "";
        for (Term name : termList) {
            String s = name.toString();
            if (s.endsWith("/nrf")) {
                String[] split = s.split("/");
                String s1 = split[0];
                cnname += s1 + ",";
            }
        }
        Responses responses = new Responses();
        responses.setData(cnname);
        return responses;
    }

    @Override
    @PostMapping("/ediname")
    public Responses diName(MultipartFile file) {
        FileUtils fileUtils = new FileUtils();
        String cnName = fileUtils.getConetxt(file);
        Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
        List<Term> termList = segment.seg(cnName);
        String cnname = "";
        for (Term name : termList) {
            String s = name.toString();
            if (s.endsWith("/ns")) {
                String[] split = s.split("/");
                String s1 = split[0];
                cnname += s1 + ",";
            }
        }
        Responses responses = new Responses();
        responses.setData(cnname);
        return responses;
    }

    @Override
    @PostMapping("/fjgname")
    public Responses jgName(MultipartFile file) {
        FileUtils fileUtils = new FileUtils();
        String cnName = fileUtils.getConetxt(file);
        Segment segment = HanLP.newSegment().enableOrganizationRecognize(true);
        List<Term> termList = segment.seg(cnName);
        String cnname = "";
        for (Term name : termList) {
            String s = name.toString();
            if (s.endsWith("/nt")) {
                String[] split = s.split("/");
                String s1 = split[0];
                cnname += s1 + ",";
            }
        }
        Responses responses = new Responses();
        responses.setData(cnname);
        return responses;
    }

    @Override
    @PostMapping("/gpyzh")
    public Responses pyzh(MultipartFile file) {
        FileUtils fileUtils = new FileUtils();
        String cnName = fileUtils.getConetxt(file);
        List<Pinyin> pinyinList = HanLP.convertToPinyinList(cnName);
        Responses responses = new Responses();
        responses.setData(pinyinList.toString());
        return responses;
    }
    @Override
    @PostMapping("/hfzj")
    public Responses fzj(MultipartFile file) {
        FileUtils fileUtils = new FileUtils();
        String cnName = fileUtils.getConetxt(file);
        String s = HanLP.convertToSimplifiedChinese(cnName);
        Responses responses = new Responses();
        responses.setData(s);
        return responses;
    }

    @Override
    @PostMapping("/ijzf")
    public Responses jzf(MultipartFile file) {
        FileUtils fileUtils = new FileUtils();
        String cnName = fileUtils.getConetxt(file);
        String s = HanLP.convertToTraditionalChinese(cnName);
        Responses responses = new Responses();
        responses.setData(s);
        return responses;
    }


}