package com.blog.modules.log.service.impl;

import cn.idev.excel.FastExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.blog.common.domain.vo.PageVO;
import com.blog.modules.log.domain.dto.SysOperateLogDTO;
import com.blog.modules.log.domain.entity.SysOperateLog;
import com.blog.modules.log.mapper.SysOperateLogMapper;
import com.blog.modules.log.service.SysOperateLogService;
import com.blog.utils.PageUtils;
import com.blog.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 系统操作日志业务实现层
 *
 * @author xuesong.lei
 * @since 2025/8/23 14:50
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysOperateLogServiceImpl implements SysOperateLogService {

    private final SysOperateLogMapper sysOperateLogMapper;

    @Override
    public PageVO<SysOperateLog> pageList(SysOperateLogDTO dto) {
        LambdaQueryWrapper<SysOperateLog> queryWrapper = getQueryWrapper(dto);
        return PageUtils.of(dto).paging(sysOperateLogMapper, queryWrapper);
    }

    @Override
    public void export(SysOperateLogDTO dto, HttpServletResponse response) {
        ResponseUtils.setExcelResponse(response);
        LambdaQueryWrapper<SysOperateLog> queryWrapper = getQueryWrapper(dto);
        try {
            FastExcel.write(response.getOutputStream(), SysOperateLog.class).sheet("操作日志").doWrite(sysOperateLogMapper.selectList(queryWrapper));
        } catch (IOException e) {
            log.error("操作日志导出失败", e);
            throw new RuntimeException("操作日志导出失败", e);
        }
    }

    private LambdaQueryWrapper<SysOperateLog> getQueryWrapper(SysOperateLogDTO dto) {
        LambdaQueryWrapper<SysOperateLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(dto.getModuleTitle()), SysOperateLog::getModuleTitle, dto.getModuleTitle())
                .like(StringUtils.isNotBlank(dto.getOperateUser()), SysOperateLog::getOperateUser, dto.getOperateUser())
                .eq(ObjectUtils.isNotEmpty(dto.getBusinessType()), SysOperateLog::getBusinessType, dto.getBusinessType())
                .eq(StringUtils.isNotBlank(dto.getOperateStatus()), SysOperateLog::getOperateStatus, dto.getOperateStatus())
                .between(ObjectUtils.isNotEmpty(dto.getBeginTime()) && ObjectUtils.isNotEmpty(dto.getEndTime()), SysOperateLog::getOperateTime, dto.getBeginTime(), dto.getEndTime())
                .orderByDesc(SysOperateLog::getOperateTime);
        return queryWrapper;
    }
}
