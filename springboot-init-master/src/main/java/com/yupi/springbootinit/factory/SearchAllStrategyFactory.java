package com.yupi.springbootinit.factory;

import com.yupi.springbootinit.datasource.PictureDataSource;
import com.yupi.springbootinit.datasource.PostDataSource;
import com.yupi.springbootinit.datasource.SearchDataSource;
import com.yupi.springbootinit.datasource.UserDataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hyc
 */

@Component
public class SearchAllStrategyFactory {


    @Resource
    private PostDataSource postDataSource;

    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private UserDataSource userDataSource;


    private final static Map<String, SearchDataSource<?>> FACTORY = new ConcurrentHashMap<>();

    public static SearchDataSource getDataSourceByType(String type) {
            if (type == null) {
                return null;
            }
            return FACTORY.get(type);
    }

    public static void register(String type, SearchDataSource dataSource){
        Assert.notNull(type,"type can't be null");
        FACTORY.put(type, dataSource);
    }





}
