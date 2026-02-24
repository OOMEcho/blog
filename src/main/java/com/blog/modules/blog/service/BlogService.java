package com.blog.modules.blog.service;

import com.blog.common.domain.dto.PageDTO;
import com.blog.common.domain.vo.PageVO;
import com.blog.modules.article.domain.vo.ArticleVO;
import com.blog.modules.blogconfig.domain.entity.BlogConfig;
import com.blog.modules.category.domain.entity.Category;
import com.blog.modules.comment.domain.dto.CommentDTO;
import com.blog.modules.comment.domain.vo.CommentVO;
import com.blog.modules.link.domain.entity.FriendLink;
import com.blog.modules.tag.domain.entity.Tag;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24
 * @Description: 博客前台业务接口
 */
public interface BlogService {

    /**
     * 已发布文章列表（分页）
     */
    PageVO<ArticleVO> articles(PageDTO pageDTO, Long categoryId, Long tagId, String keyword);

    /**
     * 文章详情（含浏览量+1）
     */
    ArticleVO articleDetail(Long id);

    /**
     * 文章归档列表
     */
    List<ArticleVO> archives();

    /**
     * 搜索文章
     */
    PageVO<ArticleVO> search(PageDTO pageDTO, String keyword);

    /**
     * 所有分类列表
     */
    List<Category> categories();

    /**
     * 所有标签列表
     */
    List<Tag> tags();

    /**
     * 友情链接列表
     */
    List<FriendLink> links();

    /**
     * 博客配置列表
     */
    List<BlogConfig> config();

    /**
     * 获取文章评论列表（树形）
     */
    List<CommentVO> comments(Long articleId);

    /**
     * 提交评论
     */
    String addComment(CommentDTO dto, HttpServletRequest request);
}
