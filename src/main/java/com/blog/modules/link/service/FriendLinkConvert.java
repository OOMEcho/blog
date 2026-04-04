package com.blog.modules.link.service;

import com.blog.modules.link.domain.dto.FriendLinkDTO;
import com.blog.modules.link.domain.entity.FriendLink;
import org.mapstruct.Mapper;

/**
 * 友情链接实体类转换
 *
 * @author xuesong.lei
 * @since 2026-02-24
 */
@Mapper(componentModel = "spring")
public interface FriendLinkConvert {

    FriendLink toFriendLink(FriendLinkDTO dto);
}
