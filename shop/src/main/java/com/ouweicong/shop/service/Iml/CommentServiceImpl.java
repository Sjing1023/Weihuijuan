package com.ouweicong.shop.service.Iml;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ouweicong.common.pojo.Comment;
import com.ouweicong.shop.dao.CommentDao;
import com.ouweicong.shop.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment>implements CommentService {
    @Autowired
    private CommentDao commentDao;

    public List<Map<String,String>> getCommentJoinShop(){
        return commentDao.selectCommentJoinShop();
    }

}
