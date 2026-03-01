package com.blog.modules.permission.service;

import com.blog.modules.permission.domain.dto.PermissionDTO;
import com.blog.modules.permission.domain.entity.Permission;
import com.blog.modules.permission.domain.vo.PermissionVO;
import org.mapstruct.Mapper;

/**
 * @Author: xuesong.lei
 * @Date: 2026/1/12 23:20
 * @Description: 权限类型转换类
 */
@Mapper(componentModel = "spring")
public interface PermissionConvert {

    Permission toPermission(PermissionDTO dto);

    PermissionVO toPermissionVo(Permission permission);
}
