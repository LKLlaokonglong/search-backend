package com.hyc.springbootinit.esdao.manager;

import javax.annotation.Resource;

import com.hyc.springbootinit.manager.CosManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Cos 操作测试
 *
 * @author hyc
 */
@SpringBootTest
class CosManagerTest {

    @Resource
    private CosManager cosManager;

    @Test
    void putObject() {
        cosManager.putObject("test", "test.json");
    }
}