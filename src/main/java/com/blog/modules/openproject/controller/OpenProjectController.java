package com.blog.modules.openproject.controller;

import com.blog.common.domain.vo.PageVO;
import com.blog.common.duplicate.PreventDuplicateSubmit;
import com.blog.common.log.BusinessType;
import com.blog.common.log.OperationLog;
import com.blog.common.validator.ValidGroup;
import com.blog.modules.openproject.domain.dto.OpenProjectDTO;
import com.blog.modules.openproject.domain.entity.OpenProject;
import com.blog.modules.openproject.service.OpenProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 开源项目控制器
 *
 * @author xuesong.lei
 * @since 2026-04-12
 */
@Api(tags = "开源项目管理")
@RestController
@RequestMapping("/openProject")
@RequiredArgsConstructor
public class OpenProjectController {

    private final OpenProjectService openProjectService;

    @ApiOperation("分页查询开源项目")
    @GetMapping("/pageList")
    public PageVO<OpenProject> pageList(OpenProjectDTO dto) {
        return openProjectService.pageList(dto);
    }

    @ApiOperation("新增开源项目")
    @PostMapping("/add")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "开源项目管理", businessType = BusinessType.INSERT)
    public String add(@Validated(ValidGroup.Create.class) @RequestBody OpenProjectDTO dto) {
        return openProjectService.add(dto);
    }

    @ApiOperation("修改开源项目")
    @PutMapping("/update")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "开源项目管理", businessType = BusinessType.UPDATE)
    public String update(@Validated(ValidGroup.Update.class) @RequestBody OpenProjectDTO dto) {
        return openProjectService.update(dto);
    }

    @ApiOperation("删除开源项目")
    @DeleteMapping("/delete/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "开源项目管理", businessType = BusinessType.DELETE)
    public String delete(@PathVariable Long id) {
        return openProjectService.delete(id);
    }
}
