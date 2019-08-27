package com.itheima.store.domain;

import java.util.List;

/**
 * \* User: 李锦卓
 * \* Time: 2018/7/10 20:06
 * \
 */
public class PageBean<T> {
    //当前页数
    private Integer currPage;
    //每页显示个数
    private Integer pageSize;
    //总记录数
    private Integer totalCount;
    //总页数
    private  Integer totoPage;
    //当前页数据
    private List<T> list;

    public Integer getCurrPage() {
        return currPage;
    }

    public void setCurrPage(Integer currPage) {
        this.currPage = currPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTotoPage() {
        return totoPage;
    }

    public void setTotoPage(Integer totoPage) {
        this.totoPage = totoPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}