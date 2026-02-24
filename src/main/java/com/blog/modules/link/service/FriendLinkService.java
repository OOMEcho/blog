package com.blog.modules.link.service;

import com.blog.common.domain.vo.PageVO;
import com.blog.modules.link.domain.dto.FriendLinkDTO;
import com.blog.modules.link.domain.entity.FriendLink;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 友情链接业务层
 */
public interface FriendLinkService {

    /**
     * 分页列表
     *
     * @param dto 查询参数
     * @return 友情链接分页列表
     */
    PageVO<FriendLink> pageList(FriendLinkDTO dto);

    /**
     * 新增友情链接
     *
     * @param dto 友情链接DTO
     * @return 响应消息
     */
    String add(FriendLinkDTO dto);

    /**
     * 修改友情链接
     *
     * @param dto 友情链接DTO
     * @return 响应消息
     */
    String update(FriendLinkDTO dto);

    /**
     * 删除友情链接
     *
     * @param id 友情链接ID
     * @return 响应消息
     */
    String delete(Long id);
}
