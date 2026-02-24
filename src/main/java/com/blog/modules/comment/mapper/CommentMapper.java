package com.blog.modules.comment.mapper;

import com.blog.modules.comment.domain.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 针对表【t_comment(评论表)】的数据库操作Mapper
 * @Entity: com.blog.modules.comment.domain.entity.Comment
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
