package com.xuecheng.framework.domain.cms.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: 李锦卓
 * Time: 2019/4/15 10:27
 */
@Data
@NoArgsConstructor
public class CmsPostPageResult extends ResponseResult {
    private String pageUrl;
    public CmsPostPageResult(ResultCode resultCode,String pageUrl){
        super(resultCode);
        this.pageUrl = pageUrl;
    }
}