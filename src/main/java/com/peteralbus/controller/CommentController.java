package com.peteralbus.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.peteralbus.domain.Comment;
import com.peteralbus.domain.User;
import com.peteralbus.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Comment controller.
 *
 * @author PeterAlbus
 * Created on 2022/3/28.
 */
@RestController
@CrossOrigin
@RequestMapping("/comment")
public class CommentController
{
    static final String COMMENT="comment";
    CommentService commentService;

    @Autowired
    public void setCommentService(CommentService commentService)
    {
        this.commentService = commentService;
    }

    @RequestMapping("/getCommentByBlogId")
    public List<Comment> getCommentByBlogId(Long blogId)
    {
        return commentService.getCommentByBlogId(blogId);
    }

    @RequestMapping("/getCommentByCommentId")
    public List<Comment> getCommentByCommentId(Long commentId)
    {
        return commentService.getCommentByCommentId(commentId);
    }

    @RequestMapping("/getCommentByUserId")
    public List<Comment> getCommentByUserId(Long userId)
    {
        return commentService.getCommentByUserId(userId);
    }

    @RequestMapping("/getCommentById")
    public Comment getCommentById(Long commentId)
    {
        return commentService.getCommentById(commentId);
    }

    @RequestMapping("/addComment")
    public String addComment(Comment comment)
    {
        if(!StpUtil.hasPermission(COMMENT))
        {
            return "notLogin";
        }
        comment.setGmtCreate(LocalDateTime.now());
        comment.setGmtModified(LocalDateTime.now());
        if(comment.getCommentUserId()!=-1)
        {
            comment.setCommentUserId(Long.valueOf((String) StpUtil.getLoginId()));
        }
        if(commentService.addComment(comment)>0)
        {
            return "success";
        }
        return "fail";
    }

    @RequestMapping("/updateComment")
    public String updateComment(Comment comment)
    {
        if(!Long.valueOf((String) StpUtil.getLoginId()).equals(comment.getCommentUserId()))
        {
            return "noPermission";
        }
        comment.setGmtModified(LocalDateTime.now());
        if(commentService.updateComment(comment)>0)
        {
            return "success";
        }
        return "fail";
    }

    @RequestMapping("/deleteComment")
    public String deleteComment(Comment comment)
    {
        final String deleteComment="delete-comment";
        Long ownerId=1507660309008289794L;
        if(comment.getCommentUserId().equals(ownerId)&&!Long.valueOf((String) StpUtil.getLoginId()).equals(ownerId))
        {
            return "noPermission";
        }
        if((!Long.valueOf((String) StpUtil.getLoginId()).equals(comment.getCommentUserId()))&&!StpUtil.hasPermission(deleteComment))
        {
            return "noPermission";
        }
        if(commentService.deleteComment(comment)>0)
        {
            return "success";
        }
        return "fail";
    }
}
