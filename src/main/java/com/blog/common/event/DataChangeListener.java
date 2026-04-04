package com.blog.common.event;

import com.blog.common.constant.RedisConstants;
import com.blog.config.security.handler.MyFilterInvocationSecurityMetadataSource;
import com.blog.modules.log.domain.entity.SysOperateLog;
import com.blog.modules.log.mapper.SysOperateLogMapper;
import com.blog.modules.common.domain.dto.UserRegisterDTO;
import com.blog.modules.notification.domain.entity.Notification;
import com.blog.modules.notification.service.NotificationService;
import com.blog.modules.user.domain.entity.User;
import com.blog.modules.user.mapper.UserMapper;
import com.blog.utils.EmailUtils;
import com.blog.utils.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据变更监听器
 *
 * @author xuesong.lei
 * @since 2025-09-07
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataChangeListener {

    private final RedisUtils redisUtils;

    private final EmailUtils emailUtils;

    private final MyFilterInvocationSecurityMetadataSource securityMetadataSource;

    private final SysOperateLogMapper sysOperateLogMapper;

    private final NotificationService notificationService;

    private final UserMapper userMapper;

    @Async("asyncExecutor")
    @EventListener(DataChangeEvent.class)
    public void onDataChange(DataChangeEvent event) {
        switch (event.getType()) {
            case WHITELIST:
                handleWhitelistChange(event);
                break;
            case RESOURCE:
                handleResourceChange(event);
                break;
            case LOG:
                handleLog(event);
                break;
            case EMAIL:
                handleEmail(event);
                break;
            case NOTIFICATION:
                handleNotification(event);
                break;
            default:
                log.warn("未知事件类型: {}", event.getType());
        }
    }

    /**
     * 处理发送注册成功邮件
     */
    private void handleEmail(DataChangeEvent event) {
        UserRegisterDTO logData = (UserRegisterDTO) event.getPayload();
        emailUtils.sendWelcomeEmail(logData.getEmail(), logData.getUsername(), "blog");
        log.info("注册成功邮件发送完成，描述: {}", event.getDescription());
    }

    /**
     * 处理白名单变更
     */
    private void handleWhitelistChange(DataChangeEvent event) {
        redisUtils.delete(RedisConstants.WHITELIST);
        securityMetadataSource.loadDataSourceAllWhitelist();
        log.info("白名单数据刷新完成，描述: {}", event.getDescription());
    }

    /**
     * 处理资源变更
     */
    private void handleResourceChange(DataChangeEvent event) {
        redisUtils.delete(RedisConstants.RESOURCES);
        securityMetadataSource.loadDataSourceAllResource();
        log.info("资源数据刷新完成，描述: {}", event.getDescription());
    }

    /**
     * 处理操作日志入库
     */
    private void handleLog(DataChangeEvent event) {
        SysOperateLog logData = (SysOperateLog) event.getPayload();
        sysOperateLogMapper.insert(logData);
        log.debug("操作日志已保存: {}", logData);
    }

    /**
     * 处理站内通知
     * 如果通知已指定userId，则直接创建
     * 如果未指定userId（如审核通知需发给所有管理员），则查询所有管理员并逐一创建
     */
    private void handleNotification(DataChangeEvent event) {
        Notification notification = (Notification) event.getPayload();

        if (notification.getUserId() != null) {
            // 指定了接收人，直接创建
            notificationService.create(notification);
            log.info("站内通知已发送给用户[{}]: {}", notification.getUserId(), notification.getTitle());
        } else {
            // 未指定接收人，发送给所有管理员（user_id=1 或 admin 角色用户）
            // 通过 t_user_role 查询拥有 admin(role_id=1) 角色的用户
            List<User> adminUsers = userMapper.selectList(
                    new LambdaQueryWrapper<User>()
                            .inSql(User::getId, "SELECT user_id FROM t_user_role WHERE role_id = 1")
                            .eq(User::getStatus, "0")
            );

            for (User admin : adminUsers) {
                Notification adminNotification = new Notification();
                adminNotification.setUserId(admin.getId());
                adminNotification.setTitle(notification.getTitle());
                adminNotification.setContent(notification.getContent());
                adminNotification.setType(notification.getType());
                adminNotification.setRelatedId(notification.getRelatedId());
                notificationService.create(adminNotification);
            }
            log.info("站内通知已发送给{}位管理员: {}", adminUsers.size(), notification.getTitle());
        }
    }
}
