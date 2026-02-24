package com.blog.modules.comment.service;

import com.blog.modules.comment.domain.entity.Comment;
import com.blog.modules.comment.domain.vo.CommentVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 评论信息转换类
 */
@Mapper(componentModel = "spring")
public interface CommentConvert {

    CommentVO toCommentVo(Comment comment);

    List<CommentVO> toCommentVoList(List<Comment> comments);
}
