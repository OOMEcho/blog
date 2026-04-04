package com.blog.common.event;

import com.blog.modules.log.domain.entity.SysOperateLog;
import com.blog.modules.common.domain.dto.UserRegisterDTO;
import com.blog.modules.notification.domain.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 数据变更发布器
 *
 * @author xuesong.lei
 * @since 2025-09-07
 */
@Component
@RequiredArgsConstructor
public class DataChangePublisher {

    private final ApplicationContext applicationContext;

    /**
     * 发布白名单变更事件
     */
    public void publishWhitelistChange(String desc) {
        applicationContext.publishEvent(new DataChangeEvent(DataChangeEvent.Type.WHITELIST, null, desc));
    }

    /**
     * 发布资源变更事件
     */
    public void publishResourceChange(String desc) {
        applicationContext.publishEvent(new DataChangeEvent(DataChangeEvent.Type.RESOURCE, null, desc));
    }

    /**
     * 发布操作日志事件
     */
    public void publishLog(SysOperateLog log) {
        applicationContext.publishEvent(new DataChangeEvent(DataChangeEvent.Type.LOG, log, "记录操作日志"));
    }

    /**
     * 发布注册成功发送邮件事件
     */
    public void publishSendRegisterSuccess(UserRegisterDTO registerDTO) {
        applicationContext.publishEvent(new DataChangeEvent(DataChangeEvent.Type.EMAIL, registerDTO, "注册成功发送邮件"));
    }

    /**
     * 发布站内通知事件
     */
    public void publishNotification(Notification notification) {
        applicationContext.publishEvent(new DataChangeEvent(DataChangeEvent.Type.NOTIFICATION, notification, "站内通知"));
    }
}
