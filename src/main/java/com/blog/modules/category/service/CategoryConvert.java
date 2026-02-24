package com.blog.modules.category.service;

import com.blog.modules.category.domain.dto.CategoryDTO;
import com.blog.modules.category.domain.entity.Category;
import org.mapstruct.Mapper;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 分类实体类转换
 */
@Mapper(componentModel = "spring")
public interface CategoryConvert {

    Category toCategory(CategoryDTO dto);
}
