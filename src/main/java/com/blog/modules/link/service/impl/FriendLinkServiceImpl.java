package com.blog.modules.link.service.impl;

import com.blog.common.constant.CommonConstants;
import com.blog.common.domain.vo.PageVO;
import com.blog.common.exception.BusinessException;
import com.blog.modules.link.domain.dto.FriendLinkDTO;
import com.blog.modules.link.domain.entity.FriendLink;
import com.blog.modules.link.mapper.FriendLinkMapper;
import com.blog.modules.link.service.FriendLinkConvert;
import com.blog.modules.link.service.FriendLinkService;
import com.blog.utils.PageUtils;
import com.blog.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 友情链接业务实现层
 */
@Service
@RequiredArgsConstructor
public class FriendLinkServiceImpl implements FriendLinkService {

    private final FriendLinkMapper friendLinkMapper;

    private final FriendLinkConvert friendLinkConvert;

    @Override
    public PageVO<FriendLink> pageList(FriendLinkDTO dto) {
        LambdaQueryWrapper<FriendLink> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(dto.getName()), FriendLink::getName, dto.getName());
        return PageUtils.of(dto).paging(friendLinkMapper, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(FriendLinkDTO dto) {
        FriendLink friendLink = friendLinkConvert.toFriendLink(dto);

        // 检查链接名称是否重复
        checkDuplicateName(friendLink);

        friendLink.setCreateBy(SecurityUtils.getUserId());
        friendLinkMapper.insert(friendLink);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String update(FriendLinkDTO dto) {
        FriendLink friendLink = friendLinkConvert.toFriendLink(dto);

        // 检查链接名称是否重复（排除自身）
        checkDuplicateName(friendLink);

        friendLink.setUpdateBy(SecurityUtils.getUserId());
        friendLinkMapper.updateById(friendLink);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(Long id) {
        friendLinkMapper.deleteById(id);
        return CommonConstants.SUCCESS_MESSAGE;
    }

    private void checkDuplicateName(FriendLink friendLink) {
        LambdaQueryWrapper<FriendLink> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FriendLink::getName, friendLink.getName())
                .ne(ObjectUtils.isNotNull(friendLink.getId()), FriendLink::getId, friendLink.getId());

        if (friendLinkMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("友情链接名称已存在");
        }
    }
}
