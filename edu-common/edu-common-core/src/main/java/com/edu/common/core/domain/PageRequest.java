package com.edu.common.core.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页请求基类
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向 asc/desc
     */
    private String orderDir = "asc";

    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }
}
