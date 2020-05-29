package com.uzykj.mall.util;

public final class PageUtil {
    private Integer index;
    private Integer count;
    private Integer total;
    private Integer pageStart;

    public PageUtil(Integer index, Integer count) {
        this.index = index;
        this.count = count;
    }

    public Boolean isHasPrev(){
        return index >= 1;
    }

    public Boolean isHasNext(){
        return index + 1 < getTotalPage();
    }

    public Integer getTotalPage(){
        return (int) Math.ceil((double) total / count);
    }

    public PageUtil(){

    }

    public Integer getPageStart() {
        if (index != null) {
            return index * count;
        } else {
            return pageStart;
        }
    }

    public PageUtil setPageStart(Integer pageStart) {
        this.pageStart = pageStart;
        return this;
    }

    public Integer getIndex() {
        return index;
    }

    public PageUtil setIndex(Integer index) {
        this.index = index;
        return this;
    }

    public Integer getCount() {
        return count;
    }

    public PageUtil setCount(Integer count) {
        this.count = count;
        return this;
    }

    public Integer getTotal() {
        return total;
    }

    public PageUtil setTotal(Integer total) {
        this.total = total;
        return this;
    }
}
