package com.wyj.apps.nevermore.model.bean;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created
 * Author: wyj
 * Date: 2019/8/16
 */

@Data
public class Video {

    @Id
    @GeneratedValue(generator = "Mysql", strategy = GenerationType.IDENTITY)
    private Long id;

    private String videoName;

    private String videoDesc;

    private String videoPath;

    private String frameImagePath;

    private Long videoLength;

    private String videoLengthStr;

    private Long videoCategoryId;

    private String videoCategoryName;

    private Integer status;

    private Date createTime;

    private Date updateTime;

}
