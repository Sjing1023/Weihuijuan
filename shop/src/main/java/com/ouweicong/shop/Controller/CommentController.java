package com.ouweicong.shop.Controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ouweicong.common.controller.Result;
import com.ouweicong.common.pojo.Comment;
import com.ouweicong.common.pojo.User;
import com.ouweicong.common.utils.JwtUtil;
import com.ouweicong.shop.service.CommentService;
import com.ouweicong.shop.service.Iml.CommentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;
    @Autowired
    CommentServiceImpl service;

    //新增评论
    @PostMapping("/add")
    public String add(@RequestBody Comment comment,@CookieValue("access_token") String token){
        User user = JwtUtil.verify(token);
        String userId = user.getUserId();
        comment.setUserId(userId);
        comment.setCommentId(UUID.randomUUID().toString());
        commentService.save(comment);
        return Result.success("新增评论成功");
    }

    //删除评论
    @DeleteMapping("/delete")
    public String delete(@RequestBody Comment comment){
        UpdateWrapper<Comment> updateWrapper =new UpdateWrapper<>();
        updateWrapper.eq("commentId",comment.getCommentId())
                    .set("sys_state",1);
        commentService.update(updateWrapper);
        return Result.success("评论删除成功");
    }

    //修改评论
    @PutMapping("update")
    public String update(@RequestBody Comment comment){
    UpdateWrapper<Comment> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("commentId",comment.getCommentId());
        commentService.updateById(comment);
        return Result.success("修改评论成功");
    }

    //连表查询
    @GetMapping("/queryComment")
    public String pageQuery(){
        //连表查询
        List<Map<String,String>> comments =service.getCommentJoinShop();
        return Result.success(comments);
    }
}
