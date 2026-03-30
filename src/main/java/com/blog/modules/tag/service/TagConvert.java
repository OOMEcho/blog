package com.blog.modules.tag.service;

import com.blog.modules.tag.domain.dto.TagDTO;
import com.blog.modules.tag.domain.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 标签实体类转换
 *
 * @author xuesong.lei
 * @since 2026-02-24 10:00:00
 */
@Mapper(componentModel = "spring")
public interface TagConvert {

    @Mapping(target = "articleCount", ignore = true)
    Tag toTag(TagDTO dto);
}
