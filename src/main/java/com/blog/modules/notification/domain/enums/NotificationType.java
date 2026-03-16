package com.blog.modules.notification.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24
 * @Description: 站内通知类型枚举
 */
@Getter
public enum NotificationType {

    AUDIT("AUDIT", "审核通知"),
    SYSTEM("SYSTEM", "系统通知");

    @EnumValue
    @JsonValue
    private final String value;

    private final String desc;

    NotificationType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
