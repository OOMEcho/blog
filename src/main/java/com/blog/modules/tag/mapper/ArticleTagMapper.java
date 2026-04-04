package com.blog.modules.tag.mapper;

import com.blog.modules.tag.domain.entity.ArticleTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 针对表【t_article_tag(文章标签关联表)】的数据库操作Mapper
 *
 * @author xuesong.lei
 * @since 2026-02-24
 * @Entity: com.blog.modules.tag.domain.entity.ArticleTag
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {
}
