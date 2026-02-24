package com.blog.modules.notification.service;

import com.blog.modules.notification.domain.entity.Notification;
import com.blog.modules.notification.domain.vo.NotificationVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24
 * @Description: 通知对象转换器
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationConvert {

    NotificationVO toNotificationVo(Notification notification);

    List<NotificationVO> toNotificationVoList(List<Notification> notifications);
}
