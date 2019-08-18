package com.wyj.apps.nevermore.service;

import com.wyj.apps.common.spring.service.BaseService;
import com.wyj.apps.nevermore.mapper.VideoMapper;
import com.wyj.apps.nevermore.model.bean.Video;
import com.wyj.apps.nevermore.model.vo.AppVideoQueryVo;
import com.wyj.apps.nevermore.model.vo.OperVideoQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created
 * Author: wyj
 * Date: 2019/8/17
 */
@Service
@Slf4j
public class VideoService extends BaseService<Video> {


    @Resource
    VideoMapper videoMapper;


    public List<Video> operQuery(OperVideoQueryVo query) {
        return videoMapper.queryOper(query);
    }

    public List<Video> appQuery(AppVideoQueryVo queryVo) {
        return videoMapper.query(queryVo);
    }
}
