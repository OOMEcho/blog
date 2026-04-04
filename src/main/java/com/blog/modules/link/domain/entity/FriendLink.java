package com.blog.modules.link.domain.entity;

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
 * 友情链接表
 *
 * @author xuesong.lei
 * @since 2026-02-24
 * @TableName t_friend_link
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel("友情链接表")
@TableName(value = "t_friend_link")
public class FriendLink implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    @TableField(value = "create_by")
    private Long createBy;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    @TableField(value = "update_by")
    private Long updateBy;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 逻辑删除标记(0=正常,1=删除)
     */
    @ApiModelProperty("逻辑删除标记(0=正常,1=删除)")
    @TableLogic
    @TableField(value = "deleted")
    private Integer deleted;

    /**
     * 版本号,用于乐观锁
     */
    @ApiModelProperty("版本号,用于乐观锁")
    @Version
    @TableField(value = "version")
    private Integer version;

    /**
     * 链接名称
     */
    @ApiModelProperty("链接名称")
    @TableField(value = "name")
    private String name;

    /**
     * 链接地址
     */
    @ApiModelProperty("链接地址")
    @TableField(value = "url")
    private String url;

    /**
     * 链接Logo
     */
    @ApiModelProperty("链接Logo")
    @TableField(value = "logo")
    private String logo;

    /**
     * 链接描述
     */
    @ApiModelProperty("链接描述")
    @TableField(value = "description")
    private String description;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    @TableField(value = "sort")
    private Integer sort;

    /**
     * 状态(0=正常,1=停用)
     */
    @ApiModelProperty("状态(0=正常,1=停用)")
    @TableField(value = "status")
    private String status;
}
