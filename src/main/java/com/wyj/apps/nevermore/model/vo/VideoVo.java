package com.wyj.apps.nevermore.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created
 * Author: wyj
 * Date: 2019/8/16
 */

@Data
public class VideoVo {

    Long videoId;
    String videoName;
    String videoDesc;
    String videoSize;
    Long videoCategoryId;
    String videoCategoryName;
    String videoImagePath;
    String videoPath;
    Date createTime;

}
