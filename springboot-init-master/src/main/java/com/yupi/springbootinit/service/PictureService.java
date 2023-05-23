package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.entity.Picture;

/**
 * @author hyc
 * 图片服务
 */
public interface PictureService {

    /**
     * 查找图片接口
     * @param searchText 搜索的内容
     * @param pageNum 页数
     * @param pageSize 数量
     * @return
     */
    Page<Picture> searchPicture(String searchText, long pageNum, long pageSize);

}
