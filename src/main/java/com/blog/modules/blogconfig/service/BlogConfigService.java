package com.blog.modules.blogconfig.service;

import com.blog.modules.blogconfig.domain.dto.BlogConfigDTO;
import com.blog.modules.blogconfig.domain.entity.BlogConfig;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 博客配置业务层
 */
public interface BlogConfigService {

    /**
     * 获取所有配置项
     *
     * @return 配置列表
     */
    List<BlogConfig> list();

    /**
     * 批量更新配置
     *
     * @param dtoList 配置DTO列表
     * @return 响应消息
     */
    String batchUpdate(List<BlogConfigDTO> dtoList);
}
