package com.wyj.apps.nevermore.mapper;

import com.wyj.apps.common.spring.mapper.IMapper;
import com.wyj.apps.nevermore.model.bean.Video;
import com.wyj.apps.nevermore.model.vo.AppVideoQueryVo;
import com.wyj.apps.nevermore.model.vo.OperVideoQueryVo;
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
public interface VideoMapper extends IMapper<Video> {

    String TABLE_NAME = " video ";
    String COLUMNS = " id, video_name, video_desc, video_path, frame_image_path, video_length" +
            ", video_length_str, video_category_id, video_category_name, `status`, create_time, update_time ";


    @Select("<script> SELECT " + COLUMNS + " FROM " + TABLE_NAME +
            " WHERE 1=1 " +
            " <if test=\"query.categoryId != null\">and video_category_id=#{query.categoryId}</if>" +
            " <if test=\"query.name != null\">and video_name like concat('%', #{query.categoryId}, '%')</if>" +
            " order by ${query.orderBy} ${query.order}" +
            "</script>")
    List<Video> queryOper(@Param("query") OperVideoQueryVo queryVo);

    @Select("<script> SELECT " + COLUMNS + " FROM " + TABLE_NAME +
            " WHERE 1=1 " +
            " <if test=\"query.id != null\">id &lt; #{query.id}</if>" +
            " order by id desc" +
            " limit ${query.pageSize}" +
            "</script>")
    List<Video> query(@Param("query") AppVideoQueryVo queryVo);

}
