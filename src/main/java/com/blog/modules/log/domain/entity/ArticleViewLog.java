package com.blog.modules.log.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章访问日志表
 *
 * @author xuesong.lei
 * @since 2026-04-17
 * @TableName t_article_view_log
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel("文章访问日志表")
@TableName(value = "t_article_view_log")
public class ArticleViewLog implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("文章ID")
    @TableField(value = "article_id")
    private Long articleId;

    @ApiModelProperty("访问IP")
    @TableField(value = "view_ip")
    private String viewIp;

    @ApiModelProperty("访问地点")
    @TableField(value = "view_local")
    private String viewLocal;

    @ApiModelProperty("浏览器类型")
    @TableField(value = "view_browser")
    private String viewBrowser;

    @ApiModelProperty("操作系统")
    @TableField(value = "view_os")
    private String viewOs;

    @ApiModelProperty("访问时间")
    @TableField(value = "view_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date viewTime;
}
