package com.hyc.springbootinit.factory;

import com.hyc.springbootinit.datasource.SearchDataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hyc
 */

@Component
public class SearchAllStrategyFactory {


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
