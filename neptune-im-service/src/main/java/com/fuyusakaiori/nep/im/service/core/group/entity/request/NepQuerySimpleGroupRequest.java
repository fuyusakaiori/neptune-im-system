package com.fuyusakaiori.nep.im.service.core.group.entity.request;


import com.example.nep.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepQuerySimpleGroupRequest {

    private NepRequestHeader requestHeader;

    /**
     * <h3>可以按照群号或者群名称查询</h3>
     */
    private Integer queryType;

    private String condition;

}
