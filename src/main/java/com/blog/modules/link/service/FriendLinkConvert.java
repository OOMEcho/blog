package com.blog.modules.link.service;

import com.blog.modules.link.domain.dto.FriendLinkDTO;
import com.blog.modules.link.domain.entity.FriendLink;
import org.mapstruct.Mapper;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 友情链接实体类转换
 */
@Mapper(componentModel = "spring")
public interface FriendLinkConvert {

    FriendLink toFriendLink(FriendLinkDTO dto);
}
