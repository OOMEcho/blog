package com.blog.modules.blog.controller;

import com.blog.common.domain.dto.PageDTO;
import com.blog.common.domain.vo.PageVO;
import com.blog.modules.article.domain.vo.ArticleVO;
import com.blog.modules.blog.service.BlogService;
import com.blog.modules.blogconfig.domain.entity.BlogConfig;
import com.blog.modules.category.domain.entity.Category;
import com.blog.modules.comment.domain.dto.CommentDTO;
import com.blog.modules.comment.domain.vo.CommentVO;
import com.blog.modules.link.domain.entity.FriendLink;
import com.blog.modules.tag.domain.entity.Tag;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public PageVO<ArticleVO> articles(PageDTO pageDTO,
                                      @RequestParam(required = false) Long categoryId,
                                      @RequestParam(required = false) Long tagId,
                                      @RequestParam(required = false) String keyword) {
        return blogService.articles(pageDTO, categoryId, tagId, keyword);
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
    public PageVO<ArticleVO> search(PageDTO pageDTO, @RequestParam(required = false) String keyword) {
        return blogService.search(pageDTO, keyword);
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

    @ApiOperation("获取文章评论列表(树形)")
    @GetMapping("/comments/{articleId}")
    public List<CommentVO> comments(@PathVariable("articleId") Long articleId) {
        return blogService.comments(articleId);
    }

    @ApiOperation("提交评论")
    @PostMapping("/comments")
    public String addComment(@Validated @RequestBody CommentDTO dto, HttpServletRequest request) {
        return blogService.addComment(dto, request);
    }
}
