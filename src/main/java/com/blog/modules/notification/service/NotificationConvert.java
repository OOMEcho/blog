package com.blog.modules.notification.service;

import com.blog.modules.notification.domain.entity.Notification;
import com.blog.modules.notification.domain.vo.NotificationVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * 通知对象转换器
 *
 * @author xuesong.lei
 * @since 2026-02-24
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationConvert {

    NotificationVO toNotificationVo(Notification notification);

    List<NotificationVO> toNotificationVoList(List<Notification> notifications);
}
