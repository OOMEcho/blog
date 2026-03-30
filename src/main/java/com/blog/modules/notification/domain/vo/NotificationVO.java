package com.blog.modules.notification.domain.vo;

import com.blog.modules.notification.domain.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 通知VO
 *
 * @author xuesong.lei
 * @since 2026-02-24
 */
@Data
@ApiModel("通知VO")
public class NotificationVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("通知标题")
    private String title;

    @ApiModelProperty("通知内容")
    private String content;

    @ApiModelProperty("通知类型(AUDIT/SYSTEM)")
    private NotificationType type;

    @ApiModelProperty("关联业务ID")
    private Long relatedId;

    @ApiModelProperty("是否已读(0-未读,1-已读)")
    private Integer isRead;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
