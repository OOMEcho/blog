package com.blog.modules.notification.service;

import com.blog.common.domain.vo.PageVO;
import com.blog.modules.notification.domain.dto.NotificationDTO;
import com.blog.modules.notification.domain.entity.Notification;
import com.blog.modules.notification.domain.vo.NotificationVO;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24
 * @Description: 通知业务层
 */
public interface NotificationService {

    /**
     * 我的通知列表(分页)
     *
     * @param dto 查询参数
     * @return 通知分页列表
     */
    PageVO<NotificationVO> pageList(NotificationDTO dto);

    /**
     * 未读通知数量
     *
     * @return 未读数量
     */
    Long unreadCount();

    /**
     * 标记单条已读
     *
     * @param id 通知ID
     * @return 响应消息
     */
    String read(Long id);

    /**
     * 全部标记已读
     *
     * @return 响应消息
     */
    String readAll();

    /**
     * 创建通知(内部调用)
     *
     * @param notification 通知实体
     */
    void create(Notification notification);
}
