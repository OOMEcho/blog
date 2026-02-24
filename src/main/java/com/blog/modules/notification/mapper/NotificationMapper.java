package com.blog.modules.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.modules.notification.domain.entity.Notification;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24
 * @Description: 通知Mapper
 */
@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
}
