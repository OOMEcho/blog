package com.blog.modules.tag.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 文章标签关联表
 * @TableName t_article_tag
 */
@Data
@Accessors(chain = true)
@ApiModel("文章标签关联表")
@TableName(value = "t_article_tag")
public class ArticleTag implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文章ID
     */
    @ApiModelProperty("文章ID")
    @TableField(value = "article_id")
    private Long articleId;

    /**
     * 标签ID
     */
    @ApiModelProperty("标签ID")
    @TableField(value = "tag_id")
    private Long tagId;
}
