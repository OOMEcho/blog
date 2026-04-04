package com.blog.modules.category.mapper;

import com.blog.modules.category.domain.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 针对表【t_category(分类表)】的数据库操作Mapper
 *
 * @author xuesong.lei
 * @since 2026-02-24
 * @Entity: com.blog.modules.category.domain.entity.Category
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 查询分类列表(含已发布文章数量)
     */
    List<Category> selectListWithArticleCount();
}
