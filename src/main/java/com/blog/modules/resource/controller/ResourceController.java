package com.blog.modules.resource.controller;

import com.blog.common.domain.vo.PageVO;
import com.blog.common.duplicate.PreventDuplicateSubmit;
import com.blog.common.log.BusinessType;
import com.blog.common.log.OperationLog;
import com.blog.common.validator.ValidGroup;
import com.blog.modules.resource.domain.dto.ResourceDTO;
import com.blog.modules.resource.domain.entity.Resource;
import com.blog.modules.resource.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: xuesong.lei
 * @Date: 2026/1/12 23:11
 * @Description: 资源管理接口
 */
@RestController
@Api(tags = "资源管理接口")
@RequiredArgsConstructor
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;

    @ApiOperation("分页列表")
    @GetMapping("/pageList")
    public PageVO<Resource> pageList(ResourceDTO dto) {
        return resourceService.pageList(dto);
    }

    @ApiOperation("详情")
    @GetMapping("/detail/{id}")
    public Resource detail(@PathVariable("id") Long id) {
        return resourceService.detail(id);
    }

    @ApiOperation("删除资源")
    @DeleteMapping("/delete/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "删除资源", businessType = BusinessType.DELETE)
    public String delete(@PathVariable("id") Long id) {
        return resourceService.delete(id);
    }

    @ApiOperation("新增资源")
    @PostMapping("/add")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "新增资源", businessType = BusinessType.INSERT)
    public String add(@Validated(ValidGroup.Create.class) @RequestBody ResourceDTO dto) {
        return resourceService.add(dto);
    }

    @ApiOperation("修改资源")
    @PutMapping("/update")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改资源", businessType = BusinessType.UPDATE)
    public String update(@Validated(ValidGroup.Update.class) @RequestBody ResourceDTO dto) {
        return resourceService.update(dto);
    }
}
