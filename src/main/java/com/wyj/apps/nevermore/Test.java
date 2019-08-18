package com.wyj.apps.nevermore;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created
 * Author: wyj
 * Date: 2019/8/15
 */
public class Test {


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String videoPath = "/Users/wuyingjie/Downloads/LOL完整版_bilibili.mp4";
        String targetImgPath = "/Users/wuyingjie/Downloads/LOL完整版_bilibili_wyj_image"+System.currentTimeMillis()+".png";
        fetchFrame(videoPath, targetImgPath);
    }

    private static void fetchFrame(String videoFile, String frameFile)
            throws IOException, ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        File targetFile = new File(frameFile);
//        Test.class.getClassLoader().loadClass("org.bytedeco.ffmpeg.global.avutil");

        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
        ff.start();
        int lengthInFrames = ff.getLengthInFrames();
        int i = 0;
        Frame frame = null;

        while(i < lengthInFrames) {
            frame = ff.grabFrame();
            if ((i > 50) && frame.image != null) {
                break;
            }
            i++;
        }

        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);


        // 对截取的帧进行等比例缩放
        ImageIO.write(bufferedImage, "png", targetFile);

        ff.stop();

        System.out.println(System.currentTimeMillis() - startTime);
    }

}
