package com.blog.modules.article.service;

import com.blog.modules.article.domain.dto.ArticleDTO;
import com.blog.modules.article.domain.entity.Article;
import com.blog.modules.article.domain.vo.ArticleVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 文章信息转换类
 */
@Mapper(componentModel = "spring")
public interface ArticleConvert {

    @Mapping(source = "authorId", target = "createBy")
    Article toArticle(ArticleDTO dto);

    @Mapping(source = "createBy", target = "authorId")
    ArticleVO toArticleVo(Article article);

    List<ArticleVO> toArticleVoList(List<Article> articles);
}
