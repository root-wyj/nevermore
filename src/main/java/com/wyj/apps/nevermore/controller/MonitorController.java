package com.wyj.apps.nevermore.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created
 * Author: wyj
 * Date: 2019/8/14
 */


@RestController
@RequestMapping("/monitor")
@Slf4j
public class MonitorController {


    @GetMapping("/index")
    public String index() {
        return "Index Ok";
    }



    @Value("${nevermore.files.video}")
    private String fileDir;

    @PostMapping("/upload")
    public String upload(@RequestParam("file")MultipartFile originFile) {
        log.info("上传文件。file:{}", originFile.getOriginalFilename());

        File newF = new File(fileDir+originFile.getOriginalFilename());

        BufferedInputStream bis = null;
        FileOutputStream fos = null;

        try {
            if (!newF.getParentFile().exists()) {
                newF.getParentFile().mkdirs();
            }

            if (!newF.exists()) {
                newF.createNewFile();
            }
            bis = new BufferedInputStream(originFile.getInputStream());
            fos = new FileOutputStream(newF);

            byte[] data = new byte[1024*1024*2]; //2M
            int length = 0;
            while((length = bis.read(data)) > 0) {
                fos.write(data, 0, length);
            }
            fos.flush();
            log.info("上传文件成功。newFile:{}", newF.getAbsolutePath());
            return "success";
        } catch (IOException e) {
            log.error("上传文件. 出错。newFile:{}, e:{}", newF.getAbsolutePath(), e);
            return "failed";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.error("关闭文件失败 fos。originFile:{}, newF:{}, e:{}", originFile.getOriginalFilename(), newF.getAbsolutePath(), e);
                }
            }

            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error("关闭文件失败 bis. originFile:{}, newF:{}, e:{}", originFile.getOriginalFilename(), newF.getAbsolutePath(), e);
                }
            }
        }



    }

}
