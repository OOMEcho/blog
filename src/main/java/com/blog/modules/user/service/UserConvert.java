package com.blog.modules.user.service;

import com.blog.modules.user.domain.dto.UserDTO;
import com.blog.modules.user.domain.entity.User;
import com.blog.modules.user.domain.vo.UserVO;
import org.mapstruct.Mapper;

/**
 * 用户信息转换类
 *
 * @author xuesong.lei
 * @since 2025-09-14
 */
@Mapper(componentModel = "spring")
public interface UserConvert {

    UserVO toUserVo(User user);

    User toUser(UserDTO dto);
}
