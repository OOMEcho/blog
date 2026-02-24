package com.blog.modules.notification.domain.dto;

import com.blog.common.domain.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24
 * @Description: 通知DTO
 */
@Data
@ApiModel("通知DTO")
@EqualsAndHashCode(callSuper = true)
public class NotificationDTO extends PageDTO {

    @ApiModelProperty("通知类型(AUDIT/SYSTEM/COMMENT)")
    private String type;

    @ApiModelProperty("是否已读(0-未读,1-已读)")
    private Integer isRead;
}
