package com.peteralbus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.peteralbus.domain.Comment;
import com.peteralbus.mapper.CommentMapper;
import com.peteralbus.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Comment service.
 * @author PeterAlbus
 * Created on 2022/3/28.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentServiceImpl implements CommentService
{
    private final CommentMapper commentMapper;

    @Override
    public List<Comment> getCommentByBlogId(Long blogId)
    {
        QueryWrapper<Comment> commentQueryWrapper=new QueryWrapper<>();
        commentQueryWrapper.eq("comment_target",1);
        commentQueryWrapper.eq("comment_target_id",blogId);
        return commentMapper.selectList(commentQueryWrapper);
    }

    @Override
    public List<Comment> getCommentByUserId(Long userId)
    {
        QueryWrapper<Comment> commentQueryWrapper=new QueryWrapper<>();
        commentQueryWrapper.eq("comment_target_id",userId);
        return commentMapper.selectList(commentQueryWrapper);
    }

    @Override
    public List<Comment> getCommentByCommentId(Long commentId)
    {
        QueryWrapper<Comment> commentQueryWrapper=new QueryWrapper<>();
        commentQueryWrapper.eq("comment_target",2);
        commentQueryWrapper.eq("comment_target_id",commentId);
        return commentMapper.selectList(commentQueryWrapper);
    }

    @Override
    public Comment getCommentById(Long commentId)
    {
        return commentMapper.selectById(commentId);
    }

    @Override
    public int addComment(Comment comment)
    {
        return commentMapper.insert(comment);
    }

    @Override
    public int updateComment(Comment comment)
    {
        return commentMapper.updateById(comment);
    }

    @Override
    public int deleteComment(Comment comment)
    {
        return commentMapper.deleteById(comment);
    }
}
