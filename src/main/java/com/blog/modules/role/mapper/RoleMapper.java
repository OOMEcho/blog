package com.blog.modules.role.mapper;

import com.blog.modules.role.domain.dto.UserAndRoleQueryDTO;
import com.blog.modules.role.domain.entity.Role;
import com.blog.modules.user.domain.vo.UserVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 针对表【t_role(角色信息表)】的数据库操作Mapper
 *
 * @author xuesong.lei
 * @since 2025-08-30
 * @Entity: com.blog.modules.role.domain.entity.Role
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 分页查询已分配用户列表
     *
     * @param dto 查询参数
     * @return 用户列表
     */
    List<UserVO> allocatedList(@Param("dto") UserAndRoleQueryDTO dto);

    /**
     * 分页查询未分配用户列表
     *
     * @param dto 查询参数
     * @return 用户列表
     */
    List<UserVO> unallocatedList(@Param("dto") UserAndRoleQueryDTO dto);
}



