package com.blog.modules.notification.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.blog.modules.notification.domain.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24
 * @Description: 站内通知表
 * @TableName t_notification
 */
@Data
@Accessors(chain = true)
@ApiModel("站内通知表")
@TableName(value = "t_notification")
public class Notification implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("接收人用户ID")
    @TableField(value = "user_id")
    private Long userId;

    @ApiModelProperty("通知标题")
    @TableField(value = "title")
    private String title;

    @ApiModelProperty("通知内容")
    @TableField(value = "content")
    private String content;

    @ApiModelProperty("通知类型(AUDIT-审核,SYSTEM-系统,COMMENT-评论)")
    @TableField(value = "type")
    private NotificationType type;

    @ApiModelProperty("关联业务ID(如文章ID)")
    @TableField(value = "related_id")
    private Long relatedId;

    @ApiModelProperty("是否已读(0-未读,1-已读)")
    @TableField(value = "is_read")
    private Integer isRead;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
