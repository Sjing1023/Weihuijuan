package com.ouweicong.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class User extends System {

    @TableId
    private String userId;

    private String userName;

    private String avatar;

    private int sex;

    private int status;

    private String mobilePhone;

    private String signature;
}
