package com.blog.modules.comment.domain.entity;

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
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 评论表
 * @TableName t_comment
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel("评论表")
@TableName(value = "t_comment")
public class Comment implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 逻辑删除标记(0=正常,1=删除)
     */
    @ApiModelProperty("逻辑删除标记(0=正常,1=删除)")
    @TableLogic
    @TableField(value = "deleted")
    private Integer deleted;

    /**
     * 文章ID
     */
    @ApiModelProperty("文章ID")
    @TableField(value = "article_id")
    private Long articleId;

    /**
     * 评论用户ID
     */
    @ApiModelProperty("评论用户ID")
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 父评论ID(顶级评论为空)
     */
    @ApiModelProperty("父评论ID(顶级评论为空)")
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 回复目标评论ID
     */
    @ApiModelProperty("回复目标评论ID")
    @TableField(value = "reply_to_id")
    private Long replyToId;

    /**
     * 评论内容
     */
    @ApiModelProperty("评论内容")
    @TableField(value = "content")
    private String content;

    /**
     * 状态(0=待审核,1=已通过,2=已拒绝)
     */
    @ApiModelProperty("状态(0=待审核,1=已通过,2=已拒绝)")
    @TableField(value = "status")
    private String status;

    /**
     * IP地址
     */
    @ApiModelProperty("IP地址")
    @TableField(value = "ip")
    private String ip;

    /**
     * 归属地
     */
    @ApiModelProperty("归属地")
    @TableField(value = "location")
    private String location;

    /**
     * 浏览器
     */
    @ApiModelProperty("浏览器")
    @TableField(value = "browser")
    private String browser;

    /**
     * 操作系统
     */
    @ApiModelProperty("操作系统")
    @TableField(value = "os")
    private String os;
}
