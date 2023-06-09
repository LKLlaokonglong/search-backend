package com.hyc.springbootinit.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.springbootinit.common.BaseResponse;
import com.hyc.springbootinit.common.ErrorCode;
import com.hyc.springbootinit.common.ResultUtils;
import com.hyc.springbootinit.exception.BusinessException;
import com.hyc.springbootinit.exception.ThrowUtils;
import com.hyc.springbootinit.model.dto.picture.PictureQueryRequest;
import com.hyc.springbootinit.model.entity.Picture;
import com.hyc.springbootinit.service.PictureService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @author hyc
 */
@RestController
@RequestMapping(value = "/picture")
@Slf4j
public class PictureController {

    @Resource
    private PictureService pictureService;

    @PostMapping(value = "/list/page/vo")
    public BaseResponse<Page<Picture>> listPictureByPage(PictureQueryRequest pictureQueryRequest) {
        if (pictureQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        // 获取搜索内容
        String searchText = pictureQueryRequest.getSearchText();
        long pageSize = pictureQueryRequest.getPageSize();
        long current = pictureQueryRequest.getCurrent();
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);
        Page<Picture> picturePage = pictureService.searchPicture(searchText, current, pageSize);
        return ResultUtils.success(picturePage);
    }


}
