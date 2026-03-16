package com.blog.modules.blog.controller;

import com.blog.common.domain.vo.PageVO;
import com.blog.modules.article.domain.vo.ArticleVO;
import com.blog.modules.blog.domain.dto.BlogArticleQueryDTO;
import com.blog.modules.blog.service.BlogService;
import com.blog.modules.blogconfig.domain.entity.BlogConfig;
import com.blog.modules.category.domain.entity.Category;
import com.blog.modules.link.domain.entity.FriendLink;
import com.blog.modules.tag.domain.entity.Tag;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    private final BlogService blogService;

    @ApiOperation("已发布文章列表(分页)")
    @GetMapping("/articles")
    public PageVO<ArticleVO> articles(BlogArticleQueryDTO dto) {
        return blogService.articles(dto);
    }

    @ApiOperation("文章详情")
    @GetMapping("/articles/{id}")
    public ArticleVO articleDetail(@PathVariable("id") Long id) {
        return blogService.articleDetail(id);
    }

    @ApiOperation("文章归档列表")
    @GetMapping("/archives")
    public List<ArticleVO> archives() {
        return blogService.archives();
    }

    @ApiOperation("搜索文章")
    @GetMapping("/search")
    public PageVO<ArticleVO> search(BlogArticleQueryDTO dto) {
        return blogService.search(dto);
    }

    @ApiOperation("所有分类列表")
    @GetMapping("/categories")
    public List<Category> categories() {
        return blogService.categories();
    }

    @ApiOperation("所有标签列表")
    @GetMapping("/tags")
    public List<Tag> tags() {
        return blogService.tags();
    }

    @ApiOperation("友情链接列表")
    @GetMapping("/links")
    public List<FriendLink> links() {
        return blogService.links();
    }

    @ApiOperation("博客配置列表")
    @GetMapping("/config")
    public List<BlogConfig> config() {
        return blogService.config();
    }
}
