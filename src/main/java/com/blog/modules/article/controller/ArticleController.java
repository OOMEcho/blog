package com.blog.modules.article.controller;

import com.blog.common.domain.vo.PageVO;
import com.blog.common.duplicate.PreventDuplicateSubmit;
import com.blog.common.log.BusinessType;
import com.blog.common.log.OperationLog;
import com.blog.common.validator.ValidGroup;
import com.blog.modules.article.domain.dto.ArticleAuditDTO;
import com.blog.modules.article.domain.dto.ArticleDTO;
import com.blog.modules.article.domain.vo.ArticleVO;
import com.blog.modules.article.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 文章接口
 *
 * @author xuesong.lei
 * @since 2026-02-24 10:00:00
 */
@RestController
@Api(tags = "文章管理接口")
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @ApiOperation("分页查询文章列表")
    @GetMapping("/pageList")
    public PageVO<ArticleVO> pageList(ArticleDTO dto) {
        return articleService.pageList(dto);
    }

    @ApiOperation("新增文章")
    @PostMapping("/add")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "新增文章", businessType = BusinessType.INSERT)
    public String add(@Validated(ValidGroup.Create.class) @RequestBody ArticleDTO dto) {
        return articleService.add(dto);
    }

    @ApiOperation("修改文章")
    @PutMapping("/update")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改文章", businessType = BusinessType.UPDATE)
    public String update(@Validated(ValidGroup.Update.class) @RequestBody ArticleDTO dto) {
        return articleService.update(dto);
    }

    @ApiOperation("删除文章")
    @DeleteMapping("/delete/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "删除文章", businessType = BusinessType.DELETE)
    public String delete(@PathVariable("id") Long id) {
        return articleService.delete(id);
    }

    @ApiOperation("审核文章")
    @PutMapping("/audit/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "审核文章", businessType = BusinessType.UPDATE)
    public String audit(@PathVariable("id") Long id, @Validated @RequestBody ArticleAuditDTO dto) {
        return articleService.audit(id, dto);
    }
}
