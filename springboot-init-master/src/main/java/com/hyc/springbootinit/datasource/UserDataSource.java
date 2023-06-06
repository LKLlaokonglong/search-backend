package com.hyc.springbootinit.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.springbootinit.factory.SearchAllStrategyFactory;
import com.hyc.springbootinit.model.dto.user.UserQueryRequest;
import com.hyc.springbootinit.model.enums.SearchTypeEnum;
import com.hyc.springbootinit.model.vo.UserVO;
import com.hyc.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户搜索源
 * @author hyc
 */
@Service
@Slf4j
public class UserDataSource implements SearchDataSource<UserVO>, InitializingBean {

   @Resource
   private UserService userService;

    @Override
    public Page<UserVO> doSearch(String searchText, long pageNum, long pageSize) {
        // 参数类型不匹配 做一个参数转换(适配器模式)
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        userQueryRequest.setCurrent(pageNum);
        userQueryRequest.setPageSize(pageSize);
        Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
        return userVOPage;
    }

    @Override
    public void afterPropertiesSet() {
        SearchAllStrategyFactory.register(SearchTypeEnum.USER.getValue(), this);
    }

}
