package com.blog.modules.tag.service;

import com.blog.modules.tag.domain.dto.TagDTO;
import com.blog.modules.tag.domain.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 标签实体类转换
 */
@Mapper(componentModel = "spring")
public interface TagConvert {

    @Mapping(target = "articleCount", ignore = true)
    Tag toTag(TagDTO dto);
}
