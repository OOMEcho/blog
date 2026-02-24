package com.blog.modules.notification.controller;

import com.blog.common.domain.vo.PageVO;
import com.blog.modules.notification.domain.dto.NotificationDTO;
import com.blog.modules.notification.domain.vo.NotificationVO;
import com.blog.modules.notification.service.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24
 * @Description: 站内通知接口
 */
@RestController
@Api(tags = "站内通知接口")
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @ApiOperation("我的通知列表(分页)")
    @GetMapping("/pageList")
    public PageVO<NotificationVO> pageList(NotificationDTO dto) {
        return notificationService.pageList(dto);
    }

    @ApiOperation("未读通知数量")
    @GetMapping("/unreadCount")
    public Long unreadCount() {
        return notificationService.unreadCount();
    }

    @ApiOperation("标记单条通知已读")
    @PutMapping("/read/{id}")
    public String read(@PathVariable("id") Long id) {
        return notificationService.read(id);
    }

    @ApiOperation("全部标记已读")
    @PutMapping("/readAll")
    public String readAll() {
        return notificationService.readAll();
    }
}
