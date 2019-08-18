package com.wyj.apps.nevermore.controller.web;

import com.wyj.apps.common.core.apiresult.ApiResultVo;
import com.wyj.apps.nevermore.NevermoreApplication;
import com.wyj.apps.nevermore.model.bean.Video;
import com.wyj.apps.nevermore.model.vo.AppVideoQueryVo;
import com.wyj.apps.nevermore.model.vo.VideoVo;
import com.wyj.apps.nevermore.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created
 * Author: wyj
 * Date: 2019/8/17
 */

@RestController
@RequestMapping(NevermoreApplication.APP_URL_PREFIX+"/video")
@Slf4j
public class VideoController {

    @Resource
    VideoService videoService;

    @GetMapping("/list/{id}")
    public ApiResultVo<List<VideoVo>> list(@PathVariable("id") Long id) {
        AppVideoQueryVo queryVo = new AppVideoQueryVo();
        queryVo.setId(id);
        List<Video> videos = videoService.appQuery(queryVo);
        List<VideoVo> ret = new ArrayList<>();
        if (videos != null && !videos.isEmpty()) {
            ret = videos.stream().map(item -> {
                VideoVo vo = new VideoVo();
                vo.setVideoId(item.getId());
                vo.setVideoName(item.getVideoName());
                vo.setVideoDesc(item.getVideoDesc());
                vo.setVideoCategoryId(item.getVideoCategoryId());
                vo.setVideoCategoryName(item.getVideoCategoryName());
                vo.setVideoSize(item.getVideoLengthStr());
                vo.setVideoPath(item.getVideoPath());
                vo.setVideoImagePath(item.getFrameImagePath());
                vo.setCreateTime(item.getCreateTime());
                return vo;
            }).collect(Collectors.toList());
        }
        return ApiResultVo.buildSuccess(ret);
    }

}
