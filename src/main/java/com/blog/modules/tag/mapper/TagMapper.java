package com.blog.modules.tag.mapper;

import com.blog.modules.tag.domain.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 针对表【t_tag(标签表)】的数据库操作Mapper
 *
 * @author xuesong.lei
 * @since 2026-02-24 10:00:00
 * @Entity: com.blog.modules.tag.domain.entity.Tag
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 根据文章ID查询标签列表
     *
     * @param articleId 文章ID
     * @return 标签列表
     */
    List<Tag> selectTagsByArticleId(@Param("articleId") Long articleId);

    /**
     * 查询标签列表(含已发布文章数量)
     */
    List<Tag> selectListWithArticleCount();
}
