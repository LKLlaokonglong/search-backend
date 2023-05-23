package com.yupi.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
import com.yupi.springbootinit.model.dto.search.SearchAllRequest;
import com.yupi.springbootinit.model.dto.user.UserQueryRequest;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.vo.PostVO;
import com.yupi.springbootinit.model.vo.SearchVO;
import com.yupi.springbootinit.model.vo.UserVO;
import com.yupi.springbootinit.service.PictureService;
import com.yupi.springbootinit.service.PostService;
import com.yupi.springbootinit.service.UserService;
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

    @Resource
    private PictureService pictureService;
    @Resource
    private UserService userService;
    @Resource
    private PostService postService;

    @PostMapping(value = "/all")
    public BaseResponse<SearchVO> listPictureByPage(@RequestBody SearchAllRequest searchAllRequest,
                                                          HttpServletRequest request) {
        if (searchAllRequest == null) {
            log.error("参数为空");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空!");
        }
        // 第几页
        long current = searchAllRequest.getCurrent();
        // 每页多少条数据
        long pageSize = searchAllRequest.getPageSize();
        // 搜索内容
        String searchContext = searchAllRequest.getSearchText();
        // 查询图片
        Page<Picture> picturePage = pictureService.searchPicture(searchContext, current, pageSize);
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setSearchText(searchContext);
        // 查询帖子
        Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
        // 查询用户
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchContext);
        Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
        // 封装SearchVO对象返回
        SearchVO searchVO = new SearchVO();
        searchVO.setPictureList(picturePage.getRecords());
        searchVO.setPostList(postVOPage.getRecords());
        searchVO.setUserList(userVOPage.getRecords());
        return ResultUtils.success(searchVO);
    }


}
