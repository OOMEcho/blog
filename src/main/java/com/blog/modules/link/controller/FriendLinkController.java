package com.blog.modules.link.controller;

import com.blog.common.domain.vo.PageVO;
import com.blog.common.duplicate.PreventDuplicateSubmit;
import com.blog.common.log.BusinessType;
import com.blog.common.log.OperationLog;
import com.blog.common.validator.ValidGroup;
import com.blog.modules.link.domain.dto.FriendLinkDTO;
import com.blog.modules.link.domain.entity.FriendLink;
import com.blog.modules.link.service.FriendLinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 友情链接接口
 *
 * @author xuesong.lei
 * @since 2026-02-24
 */
@RestController
@Api(tags = "友情链接接口")
@RequiredArgsConstructor
@RequestMapping("/link")
public class FriendLinkController {

    private final FriendLinkService friendLinkService;

    @ApiOperation("分页列表")
    @GetMapping("/pageList")
    public PageVO<FriendLink> pageList(FriendLinkDTO dto) {
        return friendLinkService.pageList(dto);
    }

    @ApiOperation("新增友情链接")
    @PostMapping("/add")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "新增友情链接", businessType = BusinessType.INSERT)
    public String add(@Validated(ValidGroup.Create.class) @RequestBody FriendLinkDTO dto) {
        return friendLinkService.add(dto);
    }

    @ApiOperation("修改友情链接")
    @PutMapping("/update")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改友情链接", businessType = BusinessType.UPDATE)
    public String update(@Validated(ValidGroup.Update.class) @RequestBody FriendLinkDTO dto) {
        return friendLinkService.update(dto);
    }

    @ApiOperation("删除友情链接")
    @DeleteMapping("/delete/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "删除友情链接", businessType = BusinessType.DELETE)
    public String delete(@PathVariable("id") Long id) {
        return friendLinkService.delete(id);
    }
}
