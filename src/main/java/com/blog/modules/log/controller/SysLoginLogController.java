package com.blog.modules.log.controller;

import com.blog.common.domain.vo.PageVO;
import com.blog.common.log.BusinessType;
import com.blog.common.log.OperationLog;
import com.blog.modules.log.domain.dto.SysLoginLogDTO;
import com.blog.modules.log.domain.entity.SysLoginLog;
import com.blog.modules.log.service.SysLoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/7 16:27
 * @Description: 登录日志接口
 */
@RestController
@Api(tags = "登录日志接口")
@RequiredArgsConstructor
@RequestMapping("/loginLog")
public class SysLoginLogController {

    private final SysLoginLogService sysLoginLogService;

    @ApiOperation("分页列表")
    @GetMapping("/pageList")
    public PageVO<SysLoginLog> pageList(SysLoginLogDTO dto) {
        return sysLoginLogService.pageList(dto);
    }

    @GetMapping("/export")
    @ApiOperation("导出登录日志")
    @OperationLog(moduleTitle = "导出登录日志", businessType = BusinessType.EXPORT)
    public void export(SysLoginLogDTO dto, HttpServletResponse response) {
        sysLoginLogService.export(dto, response);
    }
}
