package com.blog.modules.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.blog.common.constant.CommonConstants;
import com.blog.common.domain.vo.PageVO;
import com.blog.common.event.DataChangePublisher;
import com.blog.common.exception.BusinessException;
import com.blog.common.result.ResultCodeEnum;
import com.blog.modules.article.domain.dto.ArticleAuditDTO;
import com.blog.modules.article.domain.dto.ArticleDTO;
import com.blog.modules.article.domain.entity.Article;
import com.blog.modules.article.domain.vo.ArticleVO;
import com.blog.modules.article.mapper.ArticleMapper;
import com.blog.modules.article.service.ArticleConvert;
import com.blog.modules.article.service.ArticleService;
import com.blog.modules.category.domain.entity.Category;
import com.blog.modules.category.mapper.CategoryMapper;
import com.blog.modules.file.service.FileService;
import com.blog.modules.notification.domain.entity.Notification;
import com.blog.modules.notification.domain.enums.NotificationType;
import com.blog.modules.tag.domain.entity.ArticleTag;
import com.blog.modules.tag.mapper.ArticleTagMapper;
import com.blog.modules.tag.mapper.TagMapper;
import com.blog.modules.user.domain.entity.User;
import com.blog.modules.user.mapper.UserMapper;
import com.blog.utils.HtmlSanitizer;
import com.blog.utils.PageUtils;
import com.blog.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 文章业务实现层
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper;

    private final ArticleConvert articleConvert;

    private final CategoryMapper categoryMapper;

    private final UserMapper userMapper;

    private final TagMapper tagMapper;

    private final ArticleTagMapper articleTagMapper;

    private final DataChangePublisher dataChangePublisher;

    private final FileService fileService;

    @Override
    public PageVO<ArticleVO> pageList(ArticleDTO dto) {

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.isNotBlank(dto.getTitle()), Article::getTitle, dto.getTitle())
                .eq(StringUtils.isNotBlank(dto.getStatus()), Article::getStatus, dto.getStatus())
                .eq(ObjectUtils.isNotNull(dto.getCategoryId()), Article::getCategoryId, dto.getCategoryId())
                .eq(!SecurityUtils.isAdmin(), Article::getCreateBy, SecurityUtils.getUserId())// 非管理员只能查询自己的文章
                .between(ObjectUtils.isNotNull(dto.getBeginTime()) && ObjectUtils.isNotNull(dto.getEndTime()),
                        Article::getCreateTime,
                        dto.getBeginTime(), dto.getEndTime())
                .orderByDesc(Article::getIsTop)
                .orderByAsc(Article::getSort)
                .orderByDesc(Article::getCreateTime);

        PageVO<ArticleVO> pageResult = PageUtils.of(dto).pagingAndConvert(articleMapper, queryWrapper, articleConvert::toArticleVo);
        enrichArticleVoList(pageResult.getRecords());
        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(ArticleDTO dto) {
        // 非管理员不允许直接发布，只能保存草稿(0)或提交审核(2)
        enforceArticleStatus(dto.getStatus());

        Article article = articleConvert.toArticle(dto);
        article.setCreateBy(SecurityUtils.getUserId());
        // XSS防护：标题和摘要转纯文本，内容做白名单清洗
        sanitizeArticle(article);
        articleMapper.insert(article);

        saveArticleTags(article.getId(), dto.getTagIds());

        // 提交审核时通知管理员
        if ("2".equals(dto.getStatus())) {
            notifyAdminsForAudit(article);
        }

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String update(ArticleDTO dto) {
        Article dbArticle = articleMapper.selectById(dto.getId());
        if (ObjectUtils.isNull(dbArticle)) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND);
        }
        ensureCanManageArticle(dbArticle);

        // 非管理员不允许直接发布，只能保存草稿(0)或提交审核(2)
        enforceArticleStatus(dto.getStatus());

        Article article = articleConvert.toArticle(dto);
        article.setCreateBy(dbArticle.getCreateBy());
        article.setUpdateBy(SecurityUtils.getUserId());
        // XSS防护：标题和摘要转纯文本，内容做白名单清洗
        sanitizeArticle(article);
        articleMapper.updateById(article);

        saveArticleTags(dto.getId(), dto.getTagIds());

        // 提交审核时通知管理员
        if ("2".equals(dto.getStatus())) {
            notifyAdminsForAudit(article);
        }

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(Long id) {
        Article dbArticle = articleMapper.selectById(id);
        if (ObjectUtils.isNull(dbArticle)) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND);
        }
        ensureCanManageArticle(dbArticle);

        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, id));

        articleMapper.deleteById(id);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    /**
     * 保存文章标签关联关系
     *
     * @param articleId 文章ID
     * @param tagIds    标签ID列表
     */
    private void saveArticleTags(Long articleId, List<Long> tagIds) {
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, articleId));

        if (ObjectUtils.isNotEmpty(tagIds)) {
            for (Long tagId : tagIds) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(articleId);
                articleTag.setTagId(tagId);
                articleTagMapper.insert(articleTag);
            }
        }
    }

    /**
     * 批量填充文章VO的关联信息
     *
     * @param articleVoList 文章VO列表
     */
    private void enrichArticleVoList(List<ArticleVO> articleVoList) {
        if (ObjectUtils.isEmpty(articleVoList)) {
            return;
        }

        for (ArticleVO articleVo : articleVoList) {
            if (ObjectUtils.isNotNull(articleVo.getCategoryId())) {
                Category category = categoryMapper.selectById(articleVo.getCategoryId());
                if (ObjectUtils.isNotNull(category)) {
                    articleVo.setCategoryName(category.getName());
                }
            }

            if (ObjectUtils.isNotNull(articleVo.getAuthorId())) {
                User user = userMapper.selectById(articleVo.getAuthorId());
                if (ObjectUtils.isNotNull(user)) {
                    articleVo.setAuthorName(user.getNickname());
                    articleVo.setAuthorAvatar(fileService.resolvePublicAccessUrl(user.getAvatar()));
                }
            }

            articleVo.setTagList(tagMapper.selectTagsByArticleId(articleVo.getId()));
        }
    }

    /**
     * 填充单个文章VO的关联信息
     *
     * @param articleVo 文章VO
     * @param article   文章实体
     */
    private void enrichArticleVo(ArticleVO articleVo, Article article) {
        if (ObjectUtils.isNotNull(article.getCategoryId())) {
            Category category = categoryMapper.selectById(article.getCategoryId());
            if (ObjectUtils.isNotNull(category)) {
                articleVo.setCategoryName(category.getName());
            }
        }

        if (ObjectUtils.isNotNull(article.getCreateBy())) {
            User user = userMapper.selectById(article.getCreateBy());
            if (ObjectUtils.isNotNull(user)) {
                articleVo.setAuthorId(user.getId());
                articleVo.setAuthorName(user.getNickname());
                articleVo.setAuthorAvatar(fileService.resolvePublicAccessUrl(user.getAvatar()));
            }
        }

        articleVo.setTagList(tagMapper.selectTagsByArticleId(article.getId()));
    }

    /**
     * 文章内容XSS清洗
     * 标题和摘要：移除所有HTML标签，只保留纯文本
     * 文章内容(Markdown)：白名单清洗，移除script/iframe等危险标签，保留安全的HTML标签
     *
     * @param article 文章实体
     */
    private void sanitizeArticle(Article article) {
        article.setTitle(HtmlSanitizer.toPlainText(article.getTitle()));
        if (article.getSummary() != null) {
            article.setSummary(HtmlSanitizer.toPlainText(article.getSummary()));
        }
        article.setContent(HtmlSanitizer.sanitizeMarkdown(article.getContent()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String audit(Long id, ArticleAuditDTO dto) {
        String status = dto.getStatus();
        String rejectReason = dto.getRejectReason();

        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND);
        }

        // 只有待审核的文章才能审核
        if (!"2".equals(article.getStatus())) {
            throw new BusinessException("该文章不是待审核状态，无法审核");
        }

        // status 只接受 1(通过) 或 3(拒绝)
        if (!"1".equals(status) && !"3".equals(status)) {
            throw new BusinessException("审核状态不合法，只能为通过(1)或拒绝(3)");
        }

        if ("3".equals(status) && !StringUtils.isNotBlank(rejectReason)) {
            throw new BusinessException("审核拒绝时请填写拒绝原因");
        }

        article.setStatus(status);
        article.setUpdateBy(SecurityUtils.getUserId());

        if ("3".equals(status)) {
            article.setRejectReason(rejectReason);
        } else {
            article.setRejectReason(null);
        }

        articleMapper.updateById(article);

        // 异步通知文章作者审核结果
        notifyAuthorAuditResult(article, status, rejectReason);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    /**
     * 强制文章状态限制：非管理员不允许直接将文章设为已发布(1)
     *
     * @param status 文章状态
     */
    private void enforceArticleStatus(String status) {
        if ("1".equals(status) && !SecurityUtils.isAdmin()) {
            throw new BusinessException("非管理员不能直接发布文章，请提交审核");
        }
    }

    private void ensureCanManageArticle(Article article) {
        if (SecurityUtils.isAdmin()) {
            return;
        }
        Long currentUserId = SecurityUtils.getUserId();
        if (currentUserId == null || !currentUserId.equals(article.getCreateBy())) {
            throw new BusinessException("无权限操作该文章");
        }
    }

    /**
     * 通知所有管理员有文章待审核
     *
     * @param article 文章实体
     */
    private void notifyAdminsForAudit(Article article) {
        User author = userMapper.selectById(article.getCreateBy());
        String authorName = author != null ? author.getNickname() : "未知用户";

        Notification notification = new Notification();
        notification.setTitle("文章审核通知");
        notification.setContent("用户「" + authorName + "」提交了文章《" + article.getTitle() + "》等待审核");
        notification.setType(NotificationType.AUDIT);
        notification.setRelatedId(article.getId());

        // 通过事件异步发送通知给所有管理员
        dataChangePublisher.publishNotification(notification);
    }

    /**
     * 通知文章作者审核结果
     *
     * @param article      文章实体
     * @param status       审核状态(1=通过,3=拒绝)
     * @param rejectReason 拒绝原因
     */
    private void notifyAuthorAuditResult(Article article, String status, String rejectReason) {
        Notification notification = new Notification();
        notification.setUserId(article.getCreateBy());
        notification.setType(NotificationType.AUDIT);
        notification.setRelatedId(article.getId());

        if ("1".equals(status)) {
            notification.setTitle("文章审核通过");
            notification.setContent("您的文章《" + article.getTitle() + "》已审核通过并发布");
        } else {
            notification.setTitle("文章审核拒绝");
            notification.setContent("您的文章《" + article.getTitle() + "》被拒绝" +
                    (StringUtils.isNotBlank(rejectReason) ? "，原因：" + rejectReason : ""));
        }

        // 通过事件异步发送通知
        dataChangePublisher.publishNotification(notification);
    }
}
