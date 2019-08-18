package com.wyj.apps.nevermore.test;

import com.wyj.apps.nevermore.NevermoreApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created
 * Author: wyj
 * Date: 2019/8/16
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NevermoreApplication.class)
@Slf4j
public class BaseTest {

    static {
        log.info("初始化 程序启动信息");
    }


    @Before
    public void before() {
        log.info("测试前初始化数据");
    }
}
