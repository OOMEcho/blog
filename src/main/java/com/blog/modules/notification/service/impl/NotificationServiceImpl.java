package com.blog.modules.notification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.blog.common.constant.CommonConstants;
import com.blog.common.domain.vo.PageVO;
import com.blog.common.exception.BusinessException;
import com.blog.common.result.ResultCodeEnum;
import com.blog.modules.notification.domain.dto.NotificationDTO;
import com.blog.modules.notification.domain.entity.Notification;
import com.blog.modules.notification.domain.vo.NotificationVO;
import com.blog.modules.notification.mapper.NotificationMapper;
import com.blog.modules.notification.service.NotificationConvert;
import com.blog.modules.notification.service.NotificationService;
import com.blog.utils.PageUtils;
import com.blog.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 通知业务实现层
 *
 * @author xuesong.lei
 * @since 2026-02-24
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;

    private final NotificationConvert notificationConvert;

    @Override
    public PageVO<NotificationVO> pageList(NotificationDTO dto) {
        Long userId = SecurityUtils.getUserId();

        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notification::getUserId, userId)
                .eq(ObjectUtils.isNotNull(dto.getIsRead()), Notification::getIsRead, dto.getIsRead())
                .eq(ObjectUtils.isNotNull(dto.getType()), Notification::getType, dto.getType())
                .orderByDesc(Notification::getCreateTime);

        return PageUtils.of(dto).pagingAndConvert(notificationMapper, queryWrapper, notificationConvert::toNotificationVo);
    }

    @Override
    public Long unreadCount() {
        Long userId = SecurityUtils.getUserId();

        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notification::getUserId, userId)
                .eq(Notification::getIsRead, 0);

        return notificationMapper.selectCount(queryWrapper);
    }

    @Override
    public String read(Long id) {
        Long userId = SecurityUtils.getUserId();

        Notification notification = notificationMapper.selectById(id);
        if (notification == null || !notification.getUserId().equals(userId)) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND);
        }

        notification.setIsRead(1);
        notificationMapper.updateById(notification);
        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    public String readAll() {
        Long userId = SecurityUtils.getUserId();

        LambdaUpdateWrapper<Notification> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Notification::getUserId, userId)
                .eq(Notification::getIsRead, 0)
                .set(Notification::getIsRead, 1);

        notificationMapper.update(null, updateWrapper);
        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    public void create(Notification notification) {
        notification.setIsRead(0);
        notificationMapper.insert(notification);
    }
}
