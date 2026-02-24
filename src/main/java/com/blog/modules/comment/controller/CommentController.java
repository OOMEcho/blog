package com.blog.modules.comment.controller;

import com.blog.common.domain.vo.PageVO;
import com.blog.common.duplicate.PreventDuplicateSubmit;
import com.blog.common.log.BusinessType;
import com.blog.common.log.OperationLog;
import com.blog.modules.comment.domain.dto.CommentDTO;
import com.blog.modules.comment.domain.vo.CommentVO;
import com.blog.modules.comment.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 评论接口
 */
@RestController
@Api(tags = "评论管理接口")
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @ApiOperation("分页查询评论列表")
    @GetMapping("/pageList")
    public PageVO<CommentVO> pageList(CommentDTO dto) {
        return commentService.pageList(dto);
    }

    @ApiOperation("审核评论")
    @PutMapping("/updateStatus/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "审核评论", businessType = BusinessType.UPDATE)
    public String updateStatus(@PathVariable("id") Long id, @RequestParam String status) {
        return commentService.updateStatus(id, status);
    }

    @ApiOperation("删除评论")
    @DeleteMapping("/delete/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "删除评论", businessType = BusinessType.DELETE)
    public String delete(@PathVariable("id") Long id) {
        return commentService.delete(id);
    }
}
