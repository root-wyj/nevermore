package com.wyj.apps.nevermore.controller;

import com.wyj.apps.common.core.apiresult.ApiResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created
 * Author: wyj
 * Date: 2019/8/19
 */

@RestController
@Slf4j
@RequestMapping("/test")
public class TestController {


    @GetMapping("/simpleGet/{id}")
    public String simpleGet(@PathVariable("id")String id, @RequestParam("name")String name) {
        log.info("id:{}, name:{}", id, name);
        return "success"+id+","+name;
    }

    @PostMapping("/simplePost")
    public ApiResultVo simplePost(@RequestBody ApiResultVo vo, HttpServletRequest request, HttpServletResponse response) {
        log.info("vo:{}, request:{}, response:{}", vo, request.getMethod(), response.getStatus());
        return vo;
    }

}
