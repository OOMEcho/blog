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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 博客配置业务实现层
 *
 * @author xuesong.lei
 * @since 2026-02-24
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
    @Transactional(rollbackFor = Exception.class)
    public String batchUpdate(List<BlogConfigDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return CommonConstants.SUCCESS_MESSAGE;
        }

        Map<String, String> keyValueMap = new LinkedHashMap<>();
        for (BlogConfigDTO dto : dtoList) {
            if (dto == null || dto.getConfigKey() == null || dto.getConfigKey().trim().isEmpty()) {
                continue;
            }
            keyValueMap.put(dto.getConfigKey().trim(), dto.getConfigValue());
        }

        if (keyValueMap.isEmpty()) {
            return CommonConstants.SUCCESS_MESSAGE;
        }

        LambdaQueryWrapper<BlogConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(BlogConfig::getConfigKey, keyValueMap.keySet());
        List<BlogConfig> existingConfigs = blogConfigMapper.selectList(queryWrapper);

        Map<String, BlogConfig> existingConfigMap = new HashMap<>();
        for (BlogConfig config : existingConfigs) {
            existingConfigMap.put(config.getConfigKey(), config);
        }

        Long userId = SecurityUtils.getUserId();
        for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
            BlogConfig existingConfig = existingConfigMap.get(entry.getKey());
            if (existingConfig != null) {
                existingConfig.setConfigValue(entry.getValue());
                existingConfig.setUpdateBy(userId);
                blogConfigMapper.updateById(existingConfig);
            } else {
                BlogConfig newConfig = new BlogConfig();
                newConfig.setConfigKey(entry.getKey());
                newConfig.setConfigValue(entry.getValue());
                newConfig.setUpdateBy(userId);
                blogConfigMapper.insert(newConfig);
            }
        }
        return CommonConstants.SUCCESS_MESSAGE;
    }
}
