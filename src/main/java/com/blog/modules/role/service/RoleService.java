package com.blog.modules.role.service;

import com.blog.common.domain.vo.PageVO;
import com.blog.modules.role.domain.dto.CancelAllDTO;
import com.blog.modules.role.domain.dto.CancelDTO;
import com.blog.modules.role.domain.dto.RoleDTO;
import com.blog.modules.role.domain.dto.UserAndRoleQueryDTO;
import com.blog.modules.role.domain.vo.RoleVO;
import com.blog.modules.user.domain.vo.UserVO;

import java.util.List;

/**
 * 角色业务层
 *
 * @author xuesong.lei
 * @since 2025-09-10
 */
public interface RoleService {

    /**
     * 分页列表
     *
     * @param dto 角色DTO
     * @return 角色列表
     */
    PageVO<RoleVO> pageList(RoleDTO dto);

    /**
     * 修改角色状态
     *
     * @param id 主键
     * @return 结果
     */
    String updateStatus(Long id);

    /**
     * 删除
     *
     * @param id 主键
     * @return 结果
     */
    String delete(Long id);

    /**
     * 新增
     *
     * @param dto 角色DTO
     * @return 结果
     */
    String add(RoleDTO dto);

    /**
     * 编辑
     *
     * @param dto 角色DTO
     * @return 结果
     */
    String update(RoleDTO dto);

    /**
     * 查询已分配用户角色列表
     *
     * @param dto 用户角色查询DTO
     * @return 结果
     */
    PageVO<UserVO> allocatedList(UserAndRoleQueryDTO dto);

    /**
     * 查询未分配用户角色列表
     *
     * @param dto 用户角色查询DTO
     * @return 结果
     */
    PageVO<UserVO> unallocatedList(UserAndRoleQueryDTO dto);

    /**
     * 取消授权用户
     *
     * @param dto dto
     * @return 结果
     */
    String cancel(CancelDTO dto);

    /**
     * 批量取消授权用户
     *
     * @param dto dto
     * @return 结果
     */
    String cancelAll(CancelAllDTO dto);

    /**
     * 批量选择授权用户
     *
     * @param dto dto
     * @return 结果
     */
    String selectAll(CancelAllDTO dto);

    /**
     * 获取角色的权限编码列表
     *
     * @param roleId 角色ID
     * @return 权限编码列表
     */
    List<String> getRolePermissions(Long roleId);

    /**
     * 给角色分配权限
     *
     * @param roleId    角色ID
     * @param permCodes 权限编码列表
     * @return 结果
     */
    String assignPermissions(Long roleId, List<String> permCodes);
}
