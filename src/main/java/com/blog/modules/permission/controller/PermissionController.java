package com.blog.modules.permission.controller;

import com.blog.common.domain.vo.PageVO;
import com.blog.common.duplicate.PreventDuplicateSubmit;
import com.blog.common.log.BusinessType;
import com.blog.common.log.OperationLog;
import com.blog.common.validator.ValidGroup;
import com.blog.modules.permission.domain.dto.PermissionDTO;
import com.blog.modules.permission.domain.entity.Permission;
import com.blog.modules.permission.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026/1/12 23:11
 * @Description: 权限管理接口
 */
@RestController
@Api(tags = "权限管理接口")
@RequiredArgsConstructor
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    @ApiOperation("分页列表")
    @GetMapping("/pageList")
    public PageVO<Permission> pageList(PermissionDTO dto) {
        return permissionService.pageList(dto);
    }

    @ApiOperation("全部列表")
    @GetMapping("/list")
    public List<Permission> list(PermissionDTO dto) {
        return permissionService.list(dto);
    }

    @ApiOperation("修改权限状态")
    @GetMapping("/effective/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改权限状态", businessType = BusinessType.UPDATE)
    public String effective(@PathVariable("id") Long id) {
        return permissionService.effective(id);
    }

    @ApiOperation("新增权限")
    @PostMapping("/add")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "新增权限", businessType = BusinessType.INSERT)
    public String add(@Validated(ValidGroup.Create.class) @RequestBody PermissionDTO dto) {
        return permissionService.add(dto);
    }

    @ApiOperation("修改权限")
    @PutMapping("/update")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改权限", businessType = BusinessType.UPDATE)
    public String update(@Validated(ValidGroup.Update.class) @RequestBody PermissionDTO dto) {
        return permissionService.update(dto);
    }
}
