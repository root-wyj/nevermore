package com.wyj.apps.nevermore.controller.back;

import com.alibaba.fastjson.JSON;
import com.wyj.apps.common.core.apiresult.ApiResultVo;
import com.wyj.apps.common.utils.AsyncTask;
import com.wyj.apps.nevermore.NevermoreApplication;
import com.wyj.apps.nevermore.model.bean.Video;
import com.wyj.apps.nevermore.model.vo.OperVideoQueryVo;
import com.wyj.apps.nevermore.service.UploadService;
import com.wyj.apps.nevermore.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created
 * Author: wyj
 * Date: 2019/8/16
 */

@RestController
@RequestMapping(NevermoreApplication.APP_URL_PREFIX+"/back/video")
@Slf4j
public class OperVideoController {

    @Value("${nevermore.files.video}")
    private String videoDir;

    @Value("${nevermore.files.image}")
    private String imageDir;

    @Value("${nevermore.files.tmp}")
    private String tmpDir;


    @Resource
    UploadService uploadService;

    @Resource
    VideoService videoService;


    @PostMapping("/uploadVideo")
    public ApiResultVo upload(@RequestParam("file") MultipartFile originFile) {
        log.info("OperVideoController.upload|上传文件。fileName:{}, fileSize:{}",
                originFile.getOriginalFilename(), originFile.getSize());
        String filePath = uploadService.doUpload(originFile);
        return ApiResultVo.buildSuccess(filePath);
    }

    @PostMapping("/saveVideo")
    public ApiResultVo save(@RequestBody Video video) {

        Assert.notNull(video.getVideoPath(), "video 路径不能为空");

        String[] splitPath = video.getVideoPath().split("/");
        String videoName = splitPath[splitPath.length-1];
        long videoSize = uploadService.getSizeByFileName(videoName);

        Video newVideo = new Video();
        newVideo.setVideoPath(video.getVideoPath());
        newVideo.setVideoName(StringUtils.isEmpty(video.getVideoName()) ?
                videoName : video.getVideoName());
        newVideo.setVideoDesc(video.getVideoDesc());
        newVideo.setVideoCategoryId(video.getVideoCategoryId());
        newVideo.setVideoCategoryName(video.getVideoCategoryName());
        newVideo.setVideoLength(videoSize);
        newVideo.setVideoLengthStr(String.format("%.2fM", videoSize*1.0/1024/1024));
        if (videoService.save(newVideo) > 0) {
            AsyncTask.doAsyncTask(() -> updateVideoImage(newVideo));
            return ApiResultVo.buildSuccess(true);
        } else {
            return ApiResultVo.buildSuccess(false);
        }
    }

    @PostMapping("/editVideo")
    public ApiResultVo edit(@RequestBody Video video) {
        Video originVideo = videoService.selectByKey(video.getId());
        if (!video.getVideoPath().equals(originVideo.getVideoPath())) {
            String[] splitPath = video.getVideoPath().split("/");
            String videoName = splitPath[splitPath.length-1];
            long videoSize = uploadService.getSizeByFileName(videoName);
            video.setVideoLength(videoSize);
            video.setVideoLengthStr(String.format("%.2fM", videoSize*1.0/1024/1024));
            AsyncTask.doAsyncTask(() -> updateVideoImage(video));
        }
        return ApiResultVo.buildSuccess(videoService.update(video) > 1);
    }

    @GetMapping("/get/{id}")
    public ApiResultVo<Video> get(@PathVariable("id")Long id) {
        return ApiResultVo.buildSuccess(videoService.selectByKey(id));
    }

    @GetMapping("/list")
    public ApiResultVo<List<Video>> list(OperVideoQueryVo queryVo) {
        log.info("OperVideoController.list|oper list. query:{}", JSON.toJSONString(queryVo));
        return ApiResultVo.buildSuccess(videoService.operQuery(queryVo));
    }

    private String updateVideoImage(Video video) {
        String videoPath = uploadService.getVideoPath(video);
        File videoFile = new File(videoPath);

        if (!videoFile.exists()) {
            log.error("OperVideoController.updateVideoImage|更新video image失败。video 不存在. videoFile:{}", videoFile.getAbsolutePath());
            return null;
        }

        File imageFile = new File(uploadService.getImagePath(videoFile));
        try {
            uploadService.doFrameImage(videoFile, imageFile);
            log.info("OperVideoController.updateVideoImage| 截取图片成功。videoFile:{}",
                    videoFile.getAbsolutePath(), imageFile.getAbsolutePath());
        } catch (Exception e) {
            log.error("OperVideoController.updateVideoImage|从视频中截取图片失败。destFile:{}, e:{}", videoFile.getAbsolutePath(), e);
            return null;
        }

        Video updateVideo = new Video();
        updateVideo.setId(video.getId());
        updateVideo.setFrameImagePath(uploadService.getImageUrl(imageFile));
        videoService.updateByPrimaryKeySelective(updateVideo);
        log.info("OperVideoController.updateVideoImage| 更新图片成功。videoId:{}, imagePath:{}",
                updateVideo.getId(), updateVideo.getFrameImagePath());
        return video.getFrameImagePath();
    }


}
