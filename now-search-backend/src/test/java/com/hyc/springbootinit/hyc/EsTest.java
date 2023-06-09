package com.hyc.springbootinit.hyc;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;

import javax.annotation.Resource;

@SpringBootTest
public class EsTest {

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void test() {

    }


}
