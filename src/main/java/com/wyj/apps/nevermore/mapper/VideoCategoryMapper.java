package com.wyj.apps.nevermore.mapper;

import com.wyj.apps.common.spring.mapper.IMapper;
import com.wyj.apps.nevermore.model.bean.VideoCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created
 * Author: wyj
 * Date: 2019/8/16
 */

@Mapper
public interface VideoCategoryMapper extends IMapper<VideoCategory> {



    @Select("<script>SELECT id, category_name FROM video_category" +
            "<if test=\"@org.apache.commons.lang3.StringUtils@isEmpty(query)==false\">where category_name like concat('%',#{query},'%')</if>" +
//            "<if test=\"query!=null and query != ''\">where category_name like concat('%',#{query},'%')</if>" +
            "</script>")
    List<VideoCategory> queryByName(@Param("query") String query);


}
