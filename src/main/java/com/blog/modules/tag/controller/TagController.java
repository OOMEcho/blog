package com.blog.modules.tag.controller;

import com.blog.common.domain.vo.PageVO;
import com.blog.common.duplicate.PreventDuplicateSubmit;
import com.blog.common.log.BusinessType;
import com.blog.common.log.OperationLog;
import com.blog.common.validator.ValidGroup;
import com.blog.modules.tag.domain.dto.TagDTO;
import com.blog.modules.tag.domain.entity.Tag;
import com.blog.modules.tag.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签管理接口
 *
 * @author xuesong.lei
 * @since 2026-02-24 10:00:00
 */
@RestController
@Api(tags = "标签管理接口")
@RequiredArgsConstructor
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;

    @ApiOperation("分页列表")
    @GetMapping("/pageList")
    public PageVO<Tag> pageList(TagDTO dto) {
        return tagService.pageList(dto);
    }

    @ApiOperation("列表")
    @GetMapping("/list")
    public List<Tag> list() {
        return tagService.list();
    }

    @ApiOperation("新增标签")
    @PostMapping("/add")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "新增标签", businessType = BusinessType.INSERT)
    public String add(@Validated(ValidGroup.Create.class) @RequestBody TagDTO dto) {
        return tagService.add(dto);
    }

    @ApiOperation("修改标签")
    @PutMapping("/update")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改标签", businessType = BusinessType.UPDATE)
    public String update(@Validated(ValidGroup.Update.class) @RequestBody TagDTO dto) {
        return tagService.update(dto);
    }

    @ApiOperation("删除标签")
    @DeleteMapping("/delete/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "删除标签", businessType = BusinessType.DELETE)
    public String delete(@PathVariable("id") Long id) {
        return tagService.delete(id);
    }
}
