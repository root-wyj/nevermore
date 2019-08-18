package com.wyj.apps.nevermore.model.bean;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created
 * Author: wyj
 * Date: 2019/8/16
 */


@Data
public class VideoCategory {

    @Id
    @GeneratedValue(generator = "Mysql", strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoryName;


}
