package com.blog.modules.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.blog.common.domain.vo.PageVO;
import com.blog.common.exception.BusinessException;
import com.blog.common.result.ResultCodeEnum;
import com.blog.modules.article.domain.entity.Article;
import com.blog.modules.article.domain.vo.ArticleVO;
import com.blog.modules.article.mapper.ArticleMapper;
import com.blog.modules.article.service.ArticleConvert;
import com.blog.modules.blog.domain.dto.BlogArticleQueryDTO;
import com.blog.modules.blog.service.BlogService;
import com.blog.modules.blogconfig.domain.entity.BlogConfig;
import com.blog.modules.blogconfig.mapper.BlogConfigMapper;
import com.blog.modules.category.domain.entity.Category;
import com.blog.modules.category.mapper.CategoryMapper;
import com.blog.modules.file.service.FileService;
import com.blog.modules.link.domain.entity.FriendLink;
import com.blog.modules.tag.domain.entity.ArticleTag;
import com.blog.modules.tag.domain.entity.Tag;
import com.blog.modules.tag.mapper.ArticleTagMapper;
import com.blog.modules.tag.mapper.TagMapper;
import com.blog.modules.link.mapper.FriendLinkMapper;
import com.blog.modules.openproject.domain.entity.OpenProject;
import com.blog.modules.openproject.mapper.OpenProjectMapper;
import com.blog.modules.user.domain.entity.User;
import com.blog.modules.user.mapper.UserMapper;
import com.blog.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 博客前台业务实现层
 *
 * @author xuesong.lei
 * @since 2026-02-24
 */
@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final ArticleMapper articleMapper;

    private final ArticleConvert articleConvert;

    private final CategoryMapper categoryMapper;

    private final TagMapper tagMapper;

    private final ArticleTagMapper articleTagMapper;

    private final UserMapper userMapper;

    private final FriendLinkMapper friendLinkMapper;

    private final OpenProjectMapper openProjectMapper;

    private final BlogConfigMapper blogConfigMapper;

    private final FileService fileService;

    @Override
    public PageVO<ArticleVO> articles(BlogArticleQueryDTO dto) {
        Long categoryId = dto.getCategoryId();
        Long tagId = dto.getTagId();
        String keyword = dto.getKeyword();

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

        // 标签筛选：先查关联表拿到 articleId 集合
        if (tagId != null) {
            LambdaQueryWrapper<ArticleTag> tagWrapper = new LambdaQueryWrapper<>();
            tagWrapper.eq(ArticleTag::getTagId, tagId);
            List<ArticleTag> articleTags = articleTagMapper.selectList(tagWrapper);
            List<Long> articleIds = articleTags.stream()
                    .map(ArticleTag::getArticleId)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(articleIds)) {
                return new PageVO<>(Collections.emptyList(), 0L, (long) dto.getPageNum(), (long) dto.getPageSize());
            }
            wrapper.in(Article::getId, articleIds);
        }

        // 排序：置顶 > sort > 创建时间
        wrapper.orderByDesc(Article::getIsTop)
                .orderByDesc(Article::getSort)
                .orderByDesc(Article::getCreateTime);

        return PageUtils.of(dto).pagingAndConvert(articleMapper, wrapper, article -> {
            ArticleVO vo = articleConvert.toArticleVo(article);
            enrichArticleVO(vo, article);
            // 列表不返回 content
            vo.setContent(null);
            return vo;
        });
    }

    @Override
    public ArticleVO articleDetail(Long id) {
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

    @Override
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

    @Override
    public PageVO<ArticleVO> search(BlogArticleQueryDTO dto) {
        String keyword = dto.getKeyword();

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

        return PageUtils.of(dto).pagingAndConvert(articleMapper, wrapper, article -> {
            ArticleVO vo = articleConvert.toArticleVo(article);
            enrichArticleVO(vo, article);
            vo.setContent(null);
            return vo;
        });
    }

    @Override
    public List<Category> categories() {
        return categoryMapper.selectListWithArticleCount();
    }

    @Override
    public List<Tag> tags() {
        return tagMapper.selectListWithArticleCount();
    }

    @Override
    public List<FriendLink> links() {
        LambdaQueryWrapper<FriendLink> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FriendLink::getStatus, "0")
                .orderByAsc(FriendLink::getSort);
        return friendLinkMapper.selectList(wrapper);
    }

    @Override
    public List<OpenProject> openProjects() {
        LambdaQueryWrapper<OpenProject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OpenProject::getStatus, "0")
                .orderByAsc(OpenProject::getSort)
                .orderByDesc(OpenProject::getCreateTime);
        return openProjectMapper.selectList(wrapper);
    }

    @Override
    public List<BlogConfig> config() {
        return blogConfigMapper.selectList(null);
    }

    /**
     * 填充文章VO的关联信息（分类名、作者、标签）
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
                vo.setAuthorAvatar(fileService.resolvePublicAccessUrl(author.getAvatar()));
            }
        }

        // 标签列表
        List<Tag> tagList = tagMapper.selectTagsByArticleId(article.getId());
        vo.setTagList(tagList);
    }
}
