package com.wyj.apps.nevermore.service;

import com.wyj.apps.common.spring.service.BaseService;
import com.wyj.apps.nevermore.mapper.VideoCategoryMapper;
import com.wyj.apps.nevermore.model.bean.VideoCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created
 * Author: wyj
 * Date: 2019/8/16
 */

@Service
@Slf4j
public class VideoCategoryService extends BaseService<VideoCategory> {

    @Resource
    VideoCategoryMapper videoCategoryMapper;

    public List<VideoCategory> list(String query) {
        return videoCategoryMapper.queryByName(query);
    }

}
