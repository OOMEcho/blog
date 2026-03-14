package com.blog.modules.comment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.blog.common.constant.CommonConstants;
import com.blog.common.exception.BusinessException;
import com.blog.common.domain.vo.PageVO;
import com.blog.common.event.DataChangePublisher;
import com.blog.modules.article.domain.entity.Article;
import com.blog.modules.article.mapper.ArticleMapper;
import com.blog.modules.comment.domain.dto.CommentDTO;
import com.blog.modules.comment.domain.entity.Comment;
import com.blog.modules.comment.domain.vo.CommentVO;
import com.blog.modules.comment.mapper.CommentMapper;
import com.blog.modules.comment.service.CommentConvert;
import com.blog.modules.comment.service.CommentService;
import com.blog.modules.notification.domain.entity.Notification;
import com.blog.modules.notification.domain.enums.NotificationType;
import com.blog.modules.user.domain.entity.User;
import com.blog.modules.user.mapper.UserMapper;
import com.blog.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 评论业务实现层
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    private final CommentConvert commentConvert;

    private final UserMapper userMapper;

    private final ArticleMapper articleMapper;

    private final DataChangePublisher dataChangePublisher;

    @Override
    public PageVO<CommentVO> pageList(CommentDTO dto) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(ObjectUtils.isNotNull(dto.getArticleId()), Comment::getArticleId, dto.getArticleId())
                .eq(ObjectUtils.isNotNull(dto.getUserId()), Comment::getUserId, dto.getUserId())
                .eq(StringUtils.isNotBlank(dto.getStatus()), Comment::getStatus, dto.getStatus())
                .between(ObjectUtils.isNotNull(dto.getBeginTime()) && ObjectUtils.isNotNull(dto.getEndTime()),
                        Comment::getCreateTime,
                        dto.getBeginTime(), dto.getEndTime())
                .orderByDesc(Comment::getCreateTime);

        PageVO<CommentVO> pageResult = PageUtils.of(dto).pagingAndConvert(commentMapper, queryWrapper, commentConvert::toCommentVo);
        enrichCommentVoList(pageResult.getRecords());
        return pageResult;
    }

    @Override
    public String updateStatus(Long id, String status) {
        if (ObjectUtils.isNull(id)) {
            throw new BusinessException("评论不存在");
        }
        if (!"1".equals(status) && !"2".equals(status)) {
            throw new BusinessException("审核状态不合法，只能为通过(1)或拒绝(2)");
        }

        Comment comment = commentMapper.selectById(id);
        if (ObjectUtils.isNull(comment)) {
            throw new BusinessException("评论不存在");
        }
        if (!"0".equals(comment.getStatus())) {
            throw new BusinessException("该评论不是待审核状态，无法审核");
        }

        comment.setStatus(status);
        commentMapper.updateById(comment);

        notifyCommenterAuditResult(comment, status);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    public String delete(Long id) {
        commentMapper.deleteById(id);
        return CommonConstants.SUCCESS_MESSAGE;
    }

    /**
     * 批量填充评论VO的关联信息
     *
     * @param commentVoList 评论VO列表
     */
    private void enrichCommentVoList(List<CommentVO> commentVoList) {
        if (ObjectUtils.isEmpty(commentVoList)) {
            return;
        }

        for (CommentVO commentVo : commentVoList) {
            // 填充评论用户昵称和头像
            if (ObjectUtils.isNotNull(commentVo.getUserId())) {
                User user = userMapper.selectById(commentVo.getUserId());
                if (ObjectUtils.isNotNull(user)) {
                    commentVo.setNickname(user.getNickname());
                    commentVo.setAvatar(user.getAvatar());
                }
            }

            // 填充文章标题
            if (ObjectUtils.isNotNull(commentVo.getArticleId())) {
                Article article = articleMapper.selectById(commentVo.getArticleId());
                if (ObjectUtils.isNotNull(article)) {
                    commentVo.setArticleTitle(article.getTitle());
                }
            }

            // 填充回复目标用户昵称
            if (ObjectUtils.isNotNull(commentVo.getReplyToId())) {
                Comment replyToComment = commentMapper.selectById(commentVo.getReplyToId());
                if (ObjectUtils.isNotNull(replyToComment) && ObjectUtils.isNotNull(replyToComment.getUserId())) {
                    User replyToUser = userMapper.selectById(replyToComment.getUserId());
                    if (ObjectUtils.isNotNull(replyToUser)) {
                        commentVo.setReplyToNickname(replyToUser.getNickname());
                    }
                }
            }
        }
    }

    /**
     * 评论审核结果通知评论者
     *
     * @param comment 评论实体
     * @param status  审核状态（1=通过，2=拒绝）
     */
    private void notifyCommenterAuditResult(Comment comment, String status) {
        if (ObjectUtils.isNull(comment.getUserId())) {
            return;
        }

        String articleTitle = "未知文章";
        if (ObjectUtils.isNotNull(comment.getArticleId())) {
            Article article = articleMapper.selectById(comment.getArticleId());
            if (ObjectUtils.isNotNull(article)) {
                articleTitle = article.getTitle();
            }
        }

        Notification notification = new Notification();
        notification.setUserId(comment.getUserId());
        notification.setType(NotificationType.COMMENT);
        notification.setRelatedId(comment.getArticleId());

        if ("1".equals(status)) {
            notification.setTitle("评论审核通过");
            notification.setContent("您在文章《" + articleTitle + "》下的评论已通过审核");
        } else {
            notification.setTitle("评论审核拒绝");
            notification.setContent("您在文章《" + articleTitle + "》下的评论未通过审核");
        }

        dataChangePublisher.publishNotification(notification);
    }
}
