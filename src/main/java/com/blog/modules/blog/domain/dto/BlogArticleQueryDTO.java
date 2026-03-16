package com.blog.modules.blog.domain.dto;

import com.blog.common.domain.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: xuesong.lei
 * @Date: 2026-03-16
 * @Description: 博客前台文章查询DTO
 */
@Data
@ApiModel("博客前台文章查询DTO")
@EqualsAndHashCode(callSuper = true)
public class BlogArticleQueryDTO extends PageDTO {

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("标签ID")
    private Long tagId;

    @ApiModelProperty("关键词")
    private String keyword;
}
