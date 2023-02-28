package com.example.neptune.im.common.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h3>所有请求实体类共有的字段</h3>
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NeptuneRequestHeader
{

    private Integer appId;

}
