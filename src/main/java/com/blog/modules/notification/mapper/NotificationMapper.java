package com.blog.modules.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.modules.notification.domain.entity.Notification;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知Mapper
 *
 * @author xuesong.lei
 * @since 2026-02-24
 */
@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
}
