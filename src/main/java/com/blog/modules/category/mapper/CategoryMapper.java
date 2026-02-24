package com.blog.modules.category.mapper;

import com.blog.modules.category.domain.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 针对表【t_category(分类表)】的数据库操作Mapper
 * @Entity: com.blog.modules.category.domain.entity.Category
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
