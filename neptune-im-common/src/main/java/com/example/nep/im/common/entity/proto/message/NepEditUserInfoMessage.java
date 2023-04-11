package com.example.nep.im.common.entity.proto.message;


import com.example.nep.im.common.entity.proto.NepMessageBody;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class NepEditUserInfoMessage extends NepMessageBody {

    private Integer userId;

    private String nickname;

    private Integer gender;

    private Integer age;

    private Long birthday;

    private String location;

    private String selfSignature;

    private Integer friendshipAllowType;

}
