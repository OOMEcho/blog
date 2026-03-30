package com.blog.modules.link.mapper;

import com.blog.modules.link.domain.entity.FriendLink;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 针对表【t_friend_link(友情链接表)】的数据库操作Mapper
 *
 * @author xuesong.lei
 * @since 2026-02-24 10:00:00
 * @Entity: com.blog.modules.link.domain.entity.FriendLink
 */
@Mapper
public interface FriendLinkMapper extends BaseMapper<FriendLink> {

}
