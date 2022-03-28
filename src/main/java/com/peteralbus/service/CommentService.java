package com.peteralbus.service;

import com.peteralbus.domain.Comment;

import java.util.List;

/**
 * The interface Comment service.
 *
 * @author PeterAlbus Created on 2022/3/28.
 */
public interface CommentService
{
    /**
     * Gets comment by blog id.
     *
     * @param blogId the blog id
     * @return the comment by blog id
     */
    List<Comment> getCommentByBlogId(Long blogId);

    /**
     * Gets comment by user id.
     *
     * @param userId the user id
     * @return the comment by user id
     */
    List<Comment> getCommentByUserId(Long userId);

    /**
     * Gets comment by comment id.
     *
     * @param commentId the comment id
     * @return the comment by comment id
     */
    List<Comment> getCommentByCommentId(Long commentId);

    /**
     * Gets comment by id.
     *
     * @param commentId the comment id
     * @return the comment by id
     */
    Comment getCommentById(Long commentId);

    /**
     * Add comment int.
     *
     * @param comment the comment
     * @return the int
     */
    int addComment(Comment comment);

    /**
     * Update comment int.
     *
     * @param comment the comment
     * @return the int
     */
    int updateComment(Comment comment);

    /**
     * Delete comment int.
     *
     * @param comment the comment
     * @return the int
     */
    int deleteComment(Comment comment);
}
