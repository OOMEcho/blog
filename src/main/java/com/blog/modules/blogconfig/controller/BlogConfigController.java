package com.blog.modules.blogconfig.controller;

import com.blog.common.log.BusinessType;
import com.blog.common.log.OperationLog;
import com.blog.modules.blogconfig.domain.dto.BlogConfigDTO;
import com.blog.modules.blogconfig.domain.entity.BlogConfig;
import com.blog.modules.blogconfig.service.BlogConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 博客配置接口
 *
 * @author xuesong.lei
 * @since 2026-02-24 10:00:00
 */
@RestController
@Api(tags = "博客配置接口")
@RequiredArgsConstructor
@RequestMapping("/blogConfig")
public class BlogConfigController {

    private final BlogConfigService blogConfigService;

    @ApiOperation("获取所有配置")
    @GetMapping("/list")
    public List<BlogConfig> list() {
        return blogConfigService.list();
    }

    @ApiOperation("批量更新配置")
    @PutMapping("/batchUpdate")
    @OperationLog(moduleTitle = "批量更新博客配置", businessType = BusinessType.UPDATE)
    public String batchUpdate(@Validated @RequestBody List<BlogConfigDTO> dtoList) {
        return blogConfigService.batchUpdate(dtoList);
    }
}
