package com.wyj.apps.nevermore.test;

import com.wyj.apps.nevermore.model.bean.VideoCategory;
import com.wyj.apps.nevermore.service.VideoCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created
 * Author: wyj
 * Date: 2019/8/16
 */

@Component
@Slf4j
public class WyjTest extends BaseTest{

    @Resource
    VideoCategoryService videoCategoryService;

    @Test
    public void testTkMybatis() {
        List<VideoCategory> videoCategories = videoCategoryService.selectAll();
        log.info("testTkMybatis. videoCategories:{}", videoCategories);
    }
}
