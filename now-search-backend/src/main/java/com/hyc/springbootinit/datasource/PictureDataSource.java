package com.hyc.springbootinit.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.springbootinit.factory.SearchAllStrategyFactory;
import com.hyc.springbootinit.model.entity.Picture;
import com.hyc.springbootinit.model.enums.SearchTypeEnum;
import com.hyc.springbootinit.service.PictureService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hyc
 */
@Service
public class PictureDataSource implements SearchDataSource<Picture>, InitializingBean {

    @Resource
    private PictureService pictureService;

    @Override
    public Page<Picture> doSearch(String searchText, long pageNum, long pageSize) {
        return pictureService.searchPicture(searchText, pageNum, pageSize);
    }

    @Override
    public void afterPropertiesSet() {
        SearchAllStrategyFactory.register(SearchTypeEnum.PICTURE.getValue(), this);
    }

}
