package com.hyc.springbootinit.manager;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.springbootinit.common.ErrorCode;
import com.hyc.springbootinit.datasource.PictureDataSource;
import com.hyc.springbootinit.datasource.PostDataSource;
import com.hyc.springbootinit.datasource.SearchDataSource;
import com.hyc.springbootinit.datasource.UserDataSource;
import com.hyc.springbootinit.exception.BusinessException;
import com.hyc.springbootinit.exception.ThrowUtils;
import com.hyc.springbootinit.factory.SearchAllStrategyFactory;
import com.hyc.springbootinit.model.dto.search.SearchAllRequest;
import com.hyc.springbootinit.model.entity.Picture;
import com.hyc.springbootinit.model.enums.SearchTypeEnum;
import com.hyc.springbootinit.model.vo.PostVO;
import com.hyc.springbootinit.model.vo.SearchVO;
import com.hyc.springbootinit.model.vo.UserVO;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

/**
 * 搜索门面
 * @author hyc
 */
@Component
@Slf4j
public class SearchFacade {

    @Resource
    private PictureDataSource pictureDataSource;
    @Resource
    private UserDataSource userDataSource;
    @Resource
    private PostDataSource postDataSource;

    public SearchVO searchAll(@RequestBody SearchAllRequest searchAllRequest, HttpServletRequest request) {
        if (searchAllRequest == null) {
            log.error("参数为空");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空!");
        }
        String type = searchAllRequest.getType();
        ThrowUtils.throwIf(StrUtil.isBlank(type), ErrorCode.PARAMS_ERROR);
        String searchText = searchAllRequest.getSearchText();
        long pageSize = searchAllRequest.getPageSize();
        long current = searchAllRequest.getCurrent();
        // 获取枚举对象
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
       if (searchTypeEnum == null) {
           // 没有具体的搜索tag 查询所有数据
           return queryAll(searchAllRequest);
       } else {
           // 获取对应的tag
           String value = searchTypeEnum.getValue();
           // 获取对应的数据源
           SearchDataSource<?> dataSource = SearchAllStrategyFactory.getDataSourceByType(value);
           // 搜索的tag不在工厂中 返回异常
           if (dataSource == null) {
               log.error("搜索类型异常");
               throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "搜索类型异常");
           }
           Page<?> page = dataSource.doSearch(searchText, current, pageSize);
           SearchVO searchVO = SearchVO.builder().dataList(page.getRecords()).build();
           return searchVO;
       }


    }


    /**
     * 没有指定搜索的类型 并发查询所有类型并返回
     * @param searchAllRequest 搜索请求DTO
     * @return
     */
    public SearchVO queryAll(SearchAllRequest searchAllRequest) {
        String searchText = searchAllRequest.getSearchText();
        long current = searchAllRequest.getCurrent();
        long pageSize = searchAllRequest.getPageSize();
        CompletableFuture<Page<Picture>> pictureCompletableFuture = CompletableFuture.supplyAsync(() -> {
            // 查询图片
            Page<Picture> picturePage = pictureDataSource.doSearch(searchText, current, pageSize);
            return picturePage;
        });
        CompletableFuture<Page<PostVO>> postCompletableFuture = CompletableFuture.supplyAsync(() -> {

            Page<PostVO> postVOPage = postDataSource.doSearch(searchText, current, pageSize);
            return postVOPage;
        });
        CompletableFuture<Page<UserVO>> userCompletableFuture = CompletableFuture.supplyAsync(() -> {
            // 查询用户
            Page<UserVO> userVOPage = userDataSource.doSearch(searchText, current, pageSize);
            return userVOPage;
        });
        CompletableFuture.allOf(pictureCompletableFuture, postCompletableFuture, userCompletableFuture).join();
        try {
            return SearchVO.builder().userList(userCompletableFuture.get().getRecords())
                    .pictureList(pictureCompletableFuture.get().getRecords())
                    .postList(postCompletableFuture.get().getRecords()).build();
        } catch (Exception e) {
            log.error("查询异常", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
        }

    }

}
