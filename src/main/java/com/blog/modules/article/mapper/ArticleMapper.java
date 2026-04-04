package com.blog.modules.article.mapper;

import com.blog.modules.article.domain.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 针对表【t_article(文章表)】的数据库操作Mapper
 *
 * @author xuesong.lei
 * @since 2026-02-24
 * @Entity: com.blog.modules.article.domain.entity.Article
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
