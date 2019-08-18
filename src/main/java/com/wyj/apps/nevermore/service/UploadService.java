package com.wyj.apps.nevermore.service;

import com.wyj.apps.common.core.exception.BusinessException;
import com.wyj.apps.nevermore.model.bean.Video;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created
 * Author: wyj
 * Date: 2019/8/15
 */


@Service
@Slf4j
public class UploadService {

    @Value("${nevermore.files.video}")
    private String videoDir;

    @Value("${nevermore.files.image}")
    private String imageDir;

    @Value("${nevermore.resource.dir}")
    private String resourceDir;

    @Value("${nevermore.resourceHost}")
    private String resourceHost;



    public String upload(Long videoId, MultipartFile originFile) {
        log.info("UploadService.upload|上传视频, originFile:{}, videoId:{}", originFile.getOriginalFilename(), videoId);

        //1. check 原视频


        //2. 上传 到临时目录
        File tmpFile = new File(videoDir+originFile.getOriginalFilename());
        try {
            doUploadaa(originFile, tmpFile);
            log.info("UploadService.upload|上传视频 成功。originFile:{}, tmpFile:{}", originFile.getOriginalFilename(), tmpFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("UploadService.upload|上传视频 失败。originFile:{}, e:{}", originFile, e);
            return "failed";
        }

        //3. rename 到真的目录
        File destFile = new File(videoDir+originFile.getOriginalFilename());
        if (!tmpFile.renameTo(destFile)) {
            boolean tmpDeleted = tmpFile.delete();
            log.error("UploadService.upload|上传视频 重命名临时文件失败. tmpFile:{}, destFile:{}, tmpDeleted:{}",
                    tmpFile.getAbsolutePath(), destFile.getAbsolutePath(), tmpDeleted);
            return "failed";
        }

        //4. 更新库

        //5. 启动异步任务 取出视频图片
        File imageFile = new File(imageDir+originFile.getOriginalFilename()+"-image.jpg");
        try {
            doFrameImage(destFile, imageFile);
        } catch (IOException e) {
            log.error("从视频中截取图片失败。destFile:{}, e:{}", destFile.getAbsolutePath(), e);
        }


        return "success";
    }

    public String doUpload(MultipartFile originFile) {

        //2. 上传 到临时目录
        String fileName = getUploadFileName(originFile);
        try {
            File destFile = new File(resourceDir+videoDir+fileName);
            doUploadaa(originFile, destFile);
            log.info("UploadService.upload|上传视频 成功。originFile:{}, destFile:{}", originFile.getOriginalFilename(), destFile.getAbsolutePath());
            return resourceHost+videoDir+fileName;
        } catch (IOException e) {
            log.error("UploadService.upload|上传视频 失败。originFile:{}, e:{}", originFile, e);
            throw new BusinessException("上传文件失败。originFile:"+originFile.getOriginalFilename()+", msg:"+e.getMessage());
        }
    }

    public long getSizeByFileName(String fileName) {
        try {
            String[] split = fileName.split("-");
            return Long.valueOf(split[0]);
        } catch (Exception e) {
            log.error("UploadService.getSizeByFileName|获取文件大小出错。fileName:{}, e:{}", fileName, e);
            throw e;
        }
    }

    public String getVideoPath(Video video) {
        String videoUrl = video.getVideoPath();
        String[] splitUrls = videoUrl.split("/");
        return resourceDir+videoDir+splitUrls[splitUrls.length-1];
    }

    public String getImagePath(File videoFile) {
        String videoName = videoFile.getName();
        String originName = videoName.substring(0, videoName.lastIndexOf("."));
        return resourceDir+imageDir+originName+"-image-"+System.currentTimeMillis()+".jpg";
    }

    public String getImageUrl(File imageFile) {
        return resourceHost+imageDir+imageFile.getName();
    }

    private String getUploadFileName(MultipartFile originFile) {
        return originFile.getSize()+"-"+System.currentTimeMillis()+"-"+originFile.getOriginalFilename();
    }



    private void doUploadaa(MultipartFile originFile, File destFile) throws IOException {
        if (destFile.exists()) {
            throw new RuntimeException("该文件已存在："+destFile.getAbsolutePath());
        }

        BufferedInputStream bis = null;
        FileOutputStream fos = null;

        try {
            if (!destFile.getParentFile().exists()) {
                if (!destFile.getParentFile().mkdirs()) {
                    throw new RuntimeException("创建目录失败："+destFile.getParentFile().getAbsolutePath());
                }
            }

            if (!destFile.exists()) {
                if (!destFile.createNewFile()) {
                    throw new RuntimeException("创建文件失败："+destFile.getAbsolutePath());
                }
            }
            bis = new BufferedInputStream(originFile.getInputStream());
            fos = new FileOutputStream(destFile);

            byte[] data = new byte[1024*1024*2]; //2M
            int length;
            while((length = bis.read(data)) > 0) {
                fos.write(data, 0, length);
            }
            fos.flush();
            System.out.println(String.format("upload file success。originFile:%s, destFile:%s", originFile.getOriginalFilename(), destFile.getAbsolutePath()));
        } catch (IOException e) {
            if (destFile.exists() && destFile.delete()) {
                if (!destFile.delete()) {
                    System.err.println(String.format("upload file:%s failed, and delete tmpFile:%s failed.", originFile.getOriginalFilename(), destFile.getAbsolutePath()));
                }
            }
            throw new IOException("upload file error. file:{}"+destFile.getAbsolutePath(), e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    System.err.println(String.format("关闭文件失败 fos。originFile:%s, destFile:%s, msg:%s", originFile.getOriginalFilename(), destFile.getAbsolutePath(), e.getMessage()));
                    e.printStackTrace();
                }
            }

            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    System.err.println(String.format("关闭文件失败 bis. originFile:%s, destFile:%s, msg:%s", originFile.getOriginalFilename(), destFile.getAbsolutePath(), e.getMessage()));
                    e.printStackTrace();
                }
            }
        }
    }


    public void doFrameImage(File videoFile, File imageFrame) throws IOException {
        long startTime = System.currentTimeMillis();

        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
        try {
            if (!imageFrame.getParentFile().exists()) {
                if (!imageFrame.getParentFile().mkdirs()) {
                    throw new RuntimeException("创建目录失败："+imageFrame.getParentFile().getAbsolutePath());
                }
            }

            if (!imageFrame.exists()) {
                if (!imageFrame.createNewFile()) {
                    throw new RuntimeException("创建文件失败："+imageFrame.getAbsolutePath());
                }
            }

            ff.start();
            int lengthInFrames = ff.getLengthInFrames();
            int i = 0;
            Frame frame = null;

            while (i < lengthInFrames) {
                frame = ff.grabFrame();
                if ((i > 50) && frame.image != null) {
                    break;
                }
                i++;
            }

            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage bufferedImage = converter.getBufferedImage(frame);


            // 对截取的帧进行等比例缩放
            ImageIO.write(bufferedImage, "jpg", imageFrame);

            ff.stop();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw ioe;
        }

        System.out.println("用时："+(System.currentTimeMillis() - startTime));
    }

}
