package com.blog.modules.tag.service.impl;

import com.blog.common.constant.CommonConstants;
import com.blog.common.domain.vo.PageVO;
import com.blog.common.exception.BusinessException;
import com.blog.modules.tag.domain.dto.TagDTO;
import com.blog.modules.tag.domain.entity.Tag;
import com.blog.modules.tag.mapper.TagMapper;
import com.blog.modules.tag.service.TagConvert;
import com.blog.modules.tag.service.TagService;
import com.blog.utils.PageUtils;
import com.blog.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 标签业务实现层
 *
 * @author xuesong.lei
 * @since 2026-02-24 10:00:00
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;

    private final TagConvert tagConvert;

    @Override
    public PageVO<Tag> pageList(TagDTO dto) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(dto.getName()), Tag::getName, dto.getName());
        return PageUtils.of(dto).paging(tagMapper, queryWrapper);
    }

    @Override
    public List<Tag> list() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        return tagMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(TagDTO dto) {
        Tag tag = tagConvert.toTag(dto);

        // 检查标签名称是否重复
        checkDuplicateName(tag);

        tag.setCreateBy(SecurityUtils.getUserId());
        tagMapper.insert(tag);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String update(TagDTO dto) {
        Tag tag = tagConvert.toTag(dto);

        // 检查标签名称是否重复（排除自身）
        checkDuplicateName(tag);

        tag.setUpdateBy(SecurityUtils.getUserId());
        tagMapper.updateById(tag);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(Long id) {
        tagMapper.deleteById(id);
        return CommonConstants.SUCCESS_MESSAGE;
    }

    private void checkDuplicateName(Tag tag) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getName, tag.getName())
                .ne(ObjectUtils.isNotNull(tag.getId()), Tag::getId, tag.getId());

        if (tagMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("标签名称已存在");
        }
    }
}
