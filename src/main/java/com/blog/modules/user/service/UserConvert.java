package com.blog.modules.user.service;

import com.blog.modules.user.domain.dto.UserDTO;
import com.blog.modules.user.domain.entity.User;
import com.blog.modules.user.domain.vo.UserVO;
import org.mapstruct.Mapper;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/14 11:29
 * @Description: 用户信息转换类
 */
@Mapper(componentModel = "spring")
public interface UserConvert {

    UserVO toUserVo(User user);

    User toUser(UserDTO dto);
}
