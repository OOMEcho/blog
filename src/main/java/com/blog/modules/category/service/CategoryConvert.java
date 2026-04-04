package com.blog.modules.category.service;

import com.blog.modules.category.domain.dto.CategoryDTO;
import com.blog.modules.category.domain.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 分类实体类转换
 *
 * @author xuesong.lei
 * @since 2026-02-24
 */
@Mapper(componentModel = "spring")
public interface CategoryConvert {

    @Mapping(target = "articleCount", ignore = true)
    Category toCategory(CategoryDTO dto);
}
