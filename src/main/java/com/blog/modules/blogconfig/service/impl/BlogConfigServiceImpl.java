package com.blog.modules.blogconfig.service.impl;

import com.blog.common.constant.CommonConstants;
import com.blog.modules.blogconfig.domain.dto.BlogConfigDTO;
import com.blog.modules.blogconfig.domain.entity.BlogConfig;
import com.blog.modules.blogconfig.mapper.BlogConfigMapper;
import com.blog.modules.blogconfig.service.BlogConfigService;
import com.blog.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 博客配置业务实现层
 */
@Service
@RequiredArgsConstructor
public class BlogConfigServiceImpl implements BlogConfigService {

    private final BlogConfigMapper blogConfigMapper;

    @Override
    public List<BlogConfig> list() {
        LambdaQueryWrapper<BlogConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(BlogConfig::getId);
        return blogConfigMapper.selectList(queryWrapper);
    }

    @Override
    public String getByKey(String key) {
        LambdaQueryWrapper<BlogConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogConfig::getConfigKey, key);
        BlogConfig blogConfig = blogConfigMapper.selectOne(queryWrapper);
        return blogConfig != null ? blogConfig.getConfigValue() : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String update(BlogConfigDTO dto) {
        LambdaQueryWrapper<BlogConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogConfig::getConfigKey, dto.getConfigKey());
        BlogConfig blogConfig = blogConfigMapper.selectOne(queryWrapper);

        if (blogConfig != null) {
            blogConfig.setConfigValue(dto.getConfigValue());
            blogConfig.setUpdateBy(SecurityUtils.getUserId());
            blogConfigMapper.updateById(blogConfig);
        } else {
            blogConfig = new BlogConfig();
            blogConfig.setConfigKey(dto.getConfigKey());
            blogConfig.setConfigValue(dto.getConfigValue());
            blogConfig.setUpdateBy(SecurityUtils.getUserId());
            blogConfigMapper.insert(blogConfig);
        }

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchUpdate(List<BlogConfigDTO> dtoList) {
        for (BlogConfigDTO dto : dtoList) {
            update(dto);
        }
        return CommonConstants.SUCCESS_MESSAGE;
    }
}
