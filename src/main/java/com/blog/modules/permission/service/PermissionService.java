package com.blog.modules.permission.service;

import com.blog.common.domain.vo.PageVO;
import com.blog.modules.permission.domain.dto.PermissionDTO;
import com.blog.modules.permission.domain.vo.PermissionVO;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026/1/12 23:18
 * @Description: 权限业务层
 */
public interface PermissionService {

    /**
     * 分页列表
     */
    PageVO<PermissionVO> pageList(PermissionDTO dto);

    /**
     * 全部列表
     */
    List<PermissionVO> list(PermissionDTO dto);

    /**
     * 修改权限状态
     */
    String effective(Long id);

    /**
     * 新增
     */
    String add(PermissionDTO dto);

    /**
     * 修改
     */
    String update(PermissionDTO dto);
}
