package com.ouweicong.common.pojo;

import lombok.Data;

@Data
public class Comment extends System{

    private String commentId;
    //评论内容
    private String content;

    private String userId;
    //父级评论id
    private String pid;

    private String shopId;


}
