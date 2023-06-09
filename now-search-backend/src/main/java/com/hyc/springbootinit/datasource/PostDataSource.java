package com.hyc.springbootinit.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.springbootinit.factory.SearchAllStrategyFactory;
import com.hyc.springbootinit.model.dto.post.PostQueryRequest;
import com.hyc.springbootinit.model.entity.Post;
import com.hyc.springbootinit.model.enums.SearchTypeEnum;
import com.hyc.springbootinit.model.vo.PostVO;
import com.hyc.springbootinit.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子服务实现
 *
 */
@Service
@Slf4j
public class PostDataSource implements SearchDataSource<PostVO>, InitializingBean {

    @Resource
    private PostService postService;

    @Override
    public Page<PostVO> doSearch(String searchText, long pageNum, long pageSize) {
        PostQueryRequest queryRequest = new PostQueryRequest();
        queryRequest.setSearchText(searchText);
        queryRequest.setCurrent(pageNum);
        queryRequest.setPageSize(pageSize);
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        Page<Post> postPage = postService.searchFromEs(queryRequest);
        return postService.getPostVOPage(postPage, request);
    }

    @Override
    public void afterPropertiesSet() {
        SearchAllStrategyFactory.register(SearchTypeEnum.POST.getValue(), this);
    }

}




