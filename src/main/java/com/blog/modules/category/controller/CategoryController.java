package com.blog.modules.category.controller;

import com.blog.common.domain.vo.PageVO;
import com.blog.common.duplicate.PreventDuplicateSubmit;
import com.blog.common.log.BusinessType;
import com.blog.common.log.OperationLog;
import com.blog.common.validator.ValidGroup;
import com.blog.modules.category.domain.dto.CategoryDTO;
import com.blog.modules.category.domain.entity.Category;
import com.blog.modules.category.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 分类管理接口
 */
@RestController
@Api(tags = "分类管理接口")
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation("分页列表")
    @GetMapping("/pageList")
    public PageVO<Category> pageList(CategoryDTO dto) {
        return categoryService.pageList(dto);
    }

    @ApiOperation("列表")
    @GetMapping("/list")
    public List<Category> list() {
        return categoryService.list();
    }

    @ApiOperation("新增分类")
    @PostMapping("/add")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "新增分类", businessType = BusinessType.INSERT)
    public String add(@Validated(ValidGroup.Create.class) @RequestBody CategoryDTO dto) {
        return categoryService.add(dto);
    }

    @ApiOperation("修改分类")
    @PutMapping("/update")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改分类", businessType = BusinessType.UPDATE)
    public String update(@Validated(ValidGroup.Update.class) @RequestBody CategoryDTO dto) {
        return categoryService.update(dto);
    }

    @ApiOperation("删除分类")
    @DeleteMapping("/delete/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "删除分类", businessType = BusinessType.DELETE)
    public String delete(@PathVariable("id") Long id) {
        return categoryService.delete(id);
    }
}
