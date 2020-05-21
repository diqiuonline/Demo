package com.xuecheng.framework.domain.course.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * User: 李锦卓
 * Time: 2018/12/16 21:20
 */
@Data
@ToString
public class QueryPageRequest {
    //接受页面查询的查询条件
    //站点id
    @ApiModelProperty("父节点id")
    private String parengid;

}