package com.blog.modules.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.blog.common.constant.CommonConstants;
import com.blog.common.domain.dto.PageDTO;
import com.blog.common.domain.vo.PageVO;
import com.blog.common.exception.BusinessException;
import com.blog.common.ip2region.Ip2regionService;
import com.blog.common.result.ResultCodeEnum;
import com.blog.modules.article.domain.entity.Article;
import com.blog.modules.article.domain.vo.ArticleVO;
import com.blog.modules.article.mapper.ArticleMapper;
import com.blog.modules.article.service.ArticleConvert;
import com.blog.modules.blogconfig.domain.entity.BlogConfig;
import com.blog.modules.blogconfig.mapper.BlogConfigMapper;
import com.blog.modules.category.domain.entity.Category;
import com.blog.modules.category.mapper.CategoryMapper;
import com.blog.modules.comment.domain.dto.CommentDTO;
import com.blog.modules.comment.domain.entity.Comment;
import com.blog.modules.comment.domain.vo.CommentVO;
import com.blog.modules.comment.mapper.CommentMapper;
import com.blog.modules.comment.service.CommentConvert;
import com.blog.modules.link.domain.entity.FriendLink;
import com.blog.modules.link.mapper.FriendLinkMapper;
import com.blog.modules.tag.domain.entity.ArticleTag;
import com.blog.modules.tag.domain.entity.Tag;
import com.blog.modules.tag.mapper.ArticleTagMapper;
import com.blog.modules.tag.mapper.TagMapper;
import com.blog.modules.user.domain.entity.User;
import com.blog.modules.user.mapper.UserMapper;
import com.blog.utils.IpUtils;
import com.blog.utils.HtmlSanitizer;
import com.blog.utils.PageUtils;
import com.blog.utils.RequestUtils;
import com.blog.utils.SecurityUtils;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 博客前台接口
 */
@RestController
@Api(tags = "博客前台接口")
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {

    private final ArticleMapper articleMapper;

    private final ArticleConvert articleConvert;

    private final CategoryMapper categoryMapper;

    private final TagMapper tagMapper;

    private final ArticleTagMapper articleTagMapper;

    private final CommentMapper commentMapper;

    private final CommentConvert commentConvert;

    private final UserMapper userMapper;

    private final FriendLinkMapper friendLinkMapper;

    private final BlogConfigMapper blogConfigMapper;

    private final Ip2regionService ip2regionService;

    // ==================== 文章相关 ====================

    @ApiOperation("已发布文章列表(分页)")
    @GetMapping("/articles")
    public PageVO<ArticleVO> articles(PageDTO pageDTO,
                                      @RequestParam(required = false) Long categoryId,
                                      @RequestParam(required = false) Long tagId,
                                      @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, "1");

        // 分类筛选
        if (categoryId != null) {
            wrapper.eq(Article::getCategoryId, categoryId);
        }

        // 关键词搜索
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Article::getTitle, keyword);
        }

        // 标签筛选：先查关联表拿到articleId集合
        if (tagId != null) {
            LambdaQueryWrapper<ArticleTag> tagWrapper = new LambdaQueryWrapper<>();
            tagWrapper.eq(ArticleTag::getTagId, tagId);
            List<ArticleTag> articleTags = articleTagMapper.selectList(tagWrapper);
            List<Long> articleIds = articleTags.stream()
                    .map(ArticleTag::getArticleId)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(articleIds)) {
                return new PageVO<>(Collections.emptyList(), 0L, (long) pageDTO.getPageNum(), (long) pageDTO.getPageSize());
            }
            wrapper.in(Article::getId, articleIds);
        }

        // 排序：置顶 > sort > 创建时间
        wrapper.orderByDesc(Article::getIsTop)
                .orderByDesc(Article::getSort)
                .orderByDesc(Article::getCreateTime);

        return PageUtils.of(pageDTO).pagingAndConvert(articleMapper, wrapper, article -> {
            ArticleVO vo = articleConvert.toArticleVo(article);
            enrichArticleVO(vo, article);
            // 列表不返回content
            vo.setContent(null);
            return vo;
        });
    }

    @ApiOperation("文章详情")
    @GetMapping("/articles/{id}")
    public ArticleVO articleDetail(@PathVariable("id") Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null || !"1".equals(article.getStatus())) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND);
        }

        // 浏览量 +1
        articleMapper.update(null,
                new UpdateWrapper<Article>()
                        .eq("id", id)
                        .setSql("view_count = view_count + 1"));

        // 重新查询获取最新浏览量
        article = articleMapper.selectById(id);

        ArticleVO vo = articleConvert.toArticleVo(article);
        enrichArticleVO(vo, article);
        return vo;
    }

    @ApiOperation("文章归档列表")
    @GetMapping("/archives")
    public List<ArticleVO> archives() {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, "1")
                .select(Article::getId, Article::getTitle, Article::getCreateTime)
                .orderByDesc(Article::getCreateTime);
        List<Article> articles = articleMapper.selectList(wrapper);
        return articles.stream().map(article -> {
            ArticleVO vo = new ArticleVO();
            vo.setId(article.getId());
            vo.setTitle(article.getTitle());
            vo.setCreateTime(article.getCreateTime());
            return vo;
        }).collect(Collectors.toList());
    }

    @ApiOperation("搜索文章")
    @GetMapping("/search")
    public PageVO<ArticleVO> search(PageDTO pageDTO,
                                    @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, "1");

        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Article::getTitle, keyword)
                    .or()
                    .like(Article::getSummary, keyword));
        }

        wrapper.orderByDesc(Article::getIsTop)
                .orderByDesc(Article::getSort)
                .orderByDesc(Article::getCreateTime);

        return PageUtils.of(pageDTO).pagingAndConvert(articleMapper, wrapper, article -> {
            ArticleVO vo = articleConvert.toArticleVo(article);
            enrichArticleVO(vo, article);
            vo.setContent(null);
            return vo;
        });
    }

    // ==================== 分类与标签 ====================

    @ApiOperation("所有分类列表")
    @GetMapping("/categories")
    public List<Category> categories() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        return categoryMapper.selectList(wrapper);
    }

    @ApiOperation("所有标签列表")
    @GetMapping("/tags")
    public List<Tag> tags() {
        return tagMapper.selectList(null);
    }

    // ==================== 友情链接 ====================

    @ApiOperation("友情链接列表")
    @GetMapping("/links")
    public List<FriendLink> links() {
        LambdaQueryWrapper<FriendLink> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FriendLink::getStatus, "0")
                .orderByAsc(FriendLink::getSort);
        return friendLinkMapper.selectList(wrapper);
    }

    // ==================== 博客配置 ====================

    @ApiOperation("博客配置列表")
    @GetMapping("/config")
    public List<BlogConfig> config() {
        return blogConfigMapper.selectList(null);
    }

    // ==================== 评论相关 ====================

    @ApiOperation("获取文章评论列表(树形)")
    @GetMapping("/comments/{articleId}")
    public List<CommentVO> comments(@PathVariable("articleId") Long articleId) {
        // 查询已通过的评论
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getArticleId, articleId)
                .eq(Comment::getStatus, "1")
                .orderByAsc(Comment::getCreateTime);
        List<Comment> comments = commentMapper.selectList(wrapper);

        if (CollectionUtils.isEmpty(comments)) {
            return Collections.emptyList();
        }

        // 收集所有用户ID并批量查询用户信息
        Set<Long> userIds = new HashSet<>();
        for (Comment comment : comments) {
            userIds.add(comment.getUserId());
        }
        Map<Long, User> userMap = new HashMap<>();
        if (!userIds.isEmpty()) {
            LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.in(User::getId, userIds);
            List<User> users = userMapper.selectList(userWrapper);
            for (User user : users) {
                userMap.put(user.getId(), user);
            }
        }

        // 转换为VO并填充用户信息
        List<CommentVO> voList = commentConvert.toCommentVoList(comments);
        for (CommentVO vo : voList) {
            User user = userMap.get(vo.getUserId());
            if (user != null) {
                vo.setNickname(user.getNickname());
                vo.setAvatar(user.getAvatar());
            }
            // 填充回复目标用户昵称
            if (vo.getReplyToId() != null) {
                for (Comment comment : comments) {
                    if (comment.getId().equals(vo.getReplyToId())) {
                        User replyToUser = userMap.get(comment.getUserId());
                        if (replyToUser != null) {
                            vo.setReplyToNickname(replyToUser.getNickname());
                        }
                        break;
                    }
                }
            }
        }

        // 构建树形结构
        Map<Long, CommentVO> voMap = new LinkedHashMap<>();
        for (CommentVO vo : voList) {
            voMap.put(vo.getId(), vo);
        }

        List<CommentVO> tree = new ArrayList<>();
        for (CommentVO vo : voList) {
            if (vo.getParentId() == null) {
                tree.add(vo);
            } else {
                CommentVO parent = voMap.get(vo.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(vo);
                }
            }
        }

        return tree;
    }

    @ApiOperation("提交评论")
    @PostMapping("/comments")
    public String addComment(@Validated @RequestBody CommentDTO dto) {
        Long userId = SecurityUtils.getUserId();

        HttpServletRequest request = RequestUtils.getRequest();
        String ip = IpUtils.getIpAddr(request);
        String location = ip2regionService.getRegion(ip);

        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader(CommonConstants.USER_AGENT));
        String browser = userAgent.getBrowser().getName();
        String os = userAgent.getOperatingSystem().getName();

        Comment comment = new Comment();
        comment.setArticleId(dto.getArticleId());
        // XSS防护：评论内容转纯文本，移除所有HTML标签
        comment.setContent(HtmlSanitizer.toPlainText(dto.getContent()));
        comment.setParentId(dto.getParentId());
        comment.setReplyToId(dto.getReplyToId());
        comment.setUserId(userId);
        comment.setIp(ip);
        comment.setLocation(location);
        comment.setBrowser(browser);
        comment.setOs(os);
        comment.setStatus("0"); // 待审核

        commentMapper.insert(comment);
        return CommonConstants.SUCCESS_MESSAGE;
    }

    // ==================== 私有方法 ====================

    /**
     * 填充文章VO的关联信息(分类名、作者、标签)
     */
    private void enrichArticleVO(ArticleVO vo, Article article) {
        // 分类名称
        if (article.getCategoryId() != null) {
            Category category = categoryMapper.selectById(article.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }
        }

        // 作者信息
        if (article.getCreateBy() != null) {
            User author = userMapper.selectById(article.getCreateBy());
            if (author != null) {
                vo.setAuthorId(author.getId());
                vo.setAuthorName(author.getNickname());
                vo.setAuthorAvatar(author.getAvatar());
            }
        }

        // 标签列表
        List<Tag> tagList = tagMapper.selectTagsByArticleId(article.getId());
        vo.setTagList(tagList);
    }
}
