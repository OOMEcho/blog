package com.blog.modules.photography.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 摄影相册表
 *
 * @author xuesong.lei
 * @since 2026-10-19
 * @TableName t_photo_album
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel("摄影相册表")
@TableName(value = "t_photo_album")
public class PhotoAlbum implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 相册草稿内容
     */
    @ApiModelProperty("相册草稿内容")
    @TableField(value = "draft_json")
    @JsonIgnore
    private String draftJson;

    /**
     * 已发布的相册内容快照
     */
    @ApiModelProperty("已发布的相册内容快照")
    @TableField(value = "published_json")
    @JsonIgnore
    private String publishedJson;

    /**
     * 相册状态：DRAFT草稿、PUBLISHED已发布、OFFLINE已下架
     */
    @ApiModelProperty("相册状态：DRAFT草稿、PUBLISHED已发布、OFFLINE已下架")
    @TableField(value = "status")
    private String status;

    /**
     * 相册修订版本，用于并发修改校验
     */
    @ApiModelProperty("相册修订版本，用于并发修改校验")
    @TableField(value = "revision")
    private Integer revision;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;
}
