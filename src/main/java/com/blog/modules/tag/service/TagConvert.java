package com.blog.modules.tag.service;

import com.blog.modules.tag.domain.dto.TagDTO;
import com.blog.modules.tag.domain.entity.Tag;
import org.mapstruct.Mapper;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 标签实体类转换
 */
@Mapper(componentModel = "spring")
public interface TagConvert {

    Tag toTag(TagDTO dto);
}
