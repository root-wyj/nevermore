package com.wyj.apps.nevermore.model.vo;

import com.wyj.apps.common.core.query.BaseQuery;
import lombok.Data;

/**
 * Created
 * Author: wyj
 * Date: 2019/8/17
 */
@Data
public class OperVideoQueryVo extends BaseQuery {

    private Long categoryId;
    private String name;
}
