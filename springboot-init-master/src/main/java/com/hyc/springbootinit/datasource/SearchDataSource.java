package com.hyc.springbootinit.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 数据源接口(新接入的数据源必须实现)
 * @author hyc
 */
public interface SearchDataSource<T> {

    /**
     * 统一的搜索接口
     * @param searchText 搜索内容
     * @param pageNum 页号
     * @param pageSize 页大小
     * @return
     */
    Page<T> doSearch(String searchText, long pageNum, long pageSize);


}
