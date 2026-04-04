package com.blog.modules.blogconfig.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
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
 * 博客配置表
 *
 * @author xuesong.lei
 * @since 2026-02-24
 * @TableName t_blog_config
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel("博客配置表")
@TableName(value = "t_blog_config")
public class BlogConfig implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 配置键
     */
    @ApiModelProperty("配置键")
    @TableField(value = "config_key")
    private String configKey;

    /**
     * 配置值
     */
    @ApiModelProperty("配置值")
    @TableField(value = "config_value")
    private String configValue;

    /**
     * 配置描述
     */
    @ApiModelProperty("配置描述")
    @TableField(value = "description")
    private String description;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    @TableField(value = "update_by")
    private Long updateBy;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
