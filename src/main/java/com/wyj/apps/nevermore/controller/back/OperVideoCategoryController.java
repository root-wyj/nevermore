package com.wyj.apps.nevermore.controller.back;

import com.wyj.apps.common.core.apiresult.ApiResultVo;
import com.wyj.apps.nevermore.NevermoreApplication;
import com.wyj.apps.nevermore.model.bean.VideoCategory;
import com.wyj.apps.nevermore.service.VideoCategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created
 * Author: wyj
 * Date: 2019/8/16
 */

@RestController
@RequestMapping(NevermoreApplication.APP_URL_PREFIX+"/back/videoCategory")
public class OperVideoCategoryController {


    @Resource
    VideoCategoryService videoCategoryService;

    @GetMapping("/list")
    public ApiResultVo<List<VideoCategory>> list(@RequestParam(name = "query", required = false)String query) {
        return ApiResultVo.buildSuccess(videoCategoryService.list(query));
    }

    @PostMapping("/add")
    public ApiResultVo add(@RequestBody VideoCategory videoCategory) {
        videoCategory.setId(null);
        return ApiResultVo.buildSuccess(videoCategoryService.save(videoCategory) > 0);
    }

}
