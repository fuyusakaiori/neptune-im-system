package com.example.neptune.im.common.entity.request;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h3>所有请求实体类共有的字段</h3>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepRequestHeader
{

    private Integer appId;

}
