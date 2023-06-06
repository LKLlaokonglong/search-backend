package com.hyc.springbootinit.controller;

import com.hyc.springbootinit.common.BaseResponse;
import com.hyc.springbootinit.common.ErrorCode;
import com.hyc.springbootinit.common.ResultUtils;
import com.hyc.springbootinit.exception.BusinessException;
import com.hyc.springbootinit.manager.SearchFacade;
import com.hyc.springbootinit.model.dto.search.SearchAllRequest;
import com.hyc.springbootinit.model.vo.SearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author hyc
 */
@RestController
@Slf4j
@RequestMapping(value = "/search")
public class SearchController {

    /**
     * 注入搜索门面
     */
    @Resource
    private SearchFacade searchFacade;

    @PostMapping(value = "/all")
    public BaseResponse<SearchVO> listPictureByPage(@RequestBody SearchAllRequest searchAllRequest,
                                                    HttpServletRequest request) {
        if (searchAllRequest == null) {
            log.error("参数异常");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数异常");
        }
        return ResultUtils.success(searchFacade.searchAll(searchAllRequest, request));
    }




}
