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
     * 根据配置键获取配置值
     *
     * @param key 配置键
     * @return 配置值
     */
    String getByKey(String key);

    /**
     * 更新配置
     *
     * @param dto 配置DTO
     * @return 响应消息
     */
    String update(BlogConfigDTO dto);

    /**
     * 批量更新配置
     *
     * @param dtoList 配置DTO列表
     * @return 响应消息
     */
    String batchUpdate(List<BlogConfigDTO> dtoList);
}
