package com.luojing.api;

import com.luojing.domain.Responses;
import com.sun.org.apache.regexp.internal.RE;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * User: 李锦卓
 * Time: 2019/8/23 16:48
 */
@Api(value = "鲸落文化智能文本处理集",description = "文字识别接口提供多种识别方式")
public interface DiscernControllerApi {

    @ApiOperation("1、关键词提取")
    Responses gjName(MultipartFile file);

    @ApiOperation("2、短语提取")
    Responses dytq(MultipartFile file);

    @ApiOperation("3、自动摘要")
    Responses zdzy(MultipartFile file);

    @ApiOperation("4、音译人名识别")
    Responses yyName(MultipartFile file);

    @ApiOperation("5、地名识别")
    Responses diName(MultipartFile file);

    @ApiOperation("6、机构名识别")
    Responses jgName(MultipartFile file);

    @ApiOperation("7、文字转拼音")
    Responses pyzh(MultipartFile file);

    @ApiOperation("8、繁体转换简体")
    Responses fzj(MultipartFile file);

    @ApiOperation("9、简体转换繁体")
    Responses jzf(MultipartFile file);





}