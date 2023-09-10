package com.peteralbus.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.peteralbus.config.PermissionConfig;
import com.peteralbus.domain.Comment;
import com.peteralbus.domain.Result;
import com.peteralbus.service.CommentService;
import com.peteralbus.util.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/comment")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentController {
    private final CommentService commentService;

    @RequestMapping("/getCommentByBlogId")
    public Result<List<Comment>> getCommentByBlogId(Long blogId) {
        return ResultUtil.success(commentService.getCommentByBlogId(blogId));
    }

    @RequestMapping("/getCommentByCommentId")
    public Result<List<Comment>> getCommentByCommentId(Long commentId) {
        return ResultUtil.success(commentService.getCommentByCommentId(commentId));
    }

    @RequestMapping("/getCommentByUserId")
    public Result<List<Comment>> getCommentByUserId(Long userId) {
        return ResultUtil.success(commentService.getCommentByUserId(userId));
    }

    @RequestMapping("/getCommentById")
    public Result<Comment> getCommentById(Long commentId) {
        return ResultUtil.success(commentService.getCommentById(commentId));
    }

    @RequestMapping("/addComment")
    public Result<?> addComment(Comment comment) {
        if (!StpUtil.hasPermission(PermissionConfig.COMMENT)) {
            return ResultUtil.error(403,"没有此操作的权限（未登录）");
        }
        comment.setGmtCreate(LocalDateTime.now());
        comment.setGmtModified(LocalDateTime.now());
        if (comment.getCommentUserId() != -1) {
            comment.setCommentUserId(Long.valueOf((String) StpUtil.getLoginId()));
        }
        if (commentService.addComment(comment) > 0) {
            return ResultUtil.success(comment);
        }
        return ResultUtil.error(500,"添加评论失败");
    }

    @RequestMapping("/updateComment")
    public Result<?> updateComment(Comment comment) {
        if (!Long.valueOf((String) StpUtil.getLoginId()).equals(comment.getCommentUserId())) {
            return ResultUtil.error(403,"没有此操作的权限");
        }
        comment.setGmtModified(LocalDateTime.now());
        if (commentService.updateComment(comment) > 0) {
            return ResultUtil.success(comment);
        }
        return ResultUtil.error(500,"更新评论失败");
    }

    @RequestMapping("/deleteComment")
    public Result<?> deleteComment(Comment comment) {
        if ((!Long.valueOf((String) StpUtil.getLoginId()).equals(comment.getCommentUserId())) && !StpUtil.hasPermission(PermissionConfig.DELETE_COMMENT)) {
            return ResultUtil.error(403,"没有此操作的权限");
        }
        if (commentService.deleteComment(comment) > 0) {
            return ResultUtil.success(null);
        }
        return ResultUtil.error(500,"删除评论失败,请联系站长");
    }
}
