package com.blog.modules.category.service.impl;

import com.blog.common.constant.CommonConstants;
import com.blog.common.domain.vo.PageVO;
import com.blog.common.exception.BusinessException;
import com.blog.modules.category.domain.dto.CategoryDTO;
import com.blog.modules.category.domain.entity.Category;
import com.blog.modules.category.mapper.CategoryMapper;
import com.blog.modules.category.service.CategoryConvert;
import com.blog.modules.category.service.CategoryService;
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
 * 分类业务实现层
 *
 * @author xuesong.lei
 * @since 2026-02-24 10:00:00
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    private final CategoryConvert categoryConvert;

    @Override
    public PageVO<Category> pageList(CategoryDTO dto) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(dto.getName()), Category::getName, dto.getName());
        return PageUtils.of(dto).paging(categoryMapper, queryWrapper);
    }

    @Override
    public List<Category> list() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        return categoryMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(CategoryDTO dto) {
        Category category = categoryConvert.toCategory(dto);

        // 检查分类名称是否重复
        checkDuplicateName(category);

        category.setCreateBy(SecurityUtils.getUserId());
        categoryMapper.insert(category);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String update(CategoryDTO dto) {
        Category category = categoryConvert.toCategory(dto);

        // 检查分类名称是否重复（排除自身）
        checkDuplicateName(category);

        category.setUpdateBy(SecurityUtils.getUserId());
        categoryMapper.updateById(category);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(Long id) {
        categoryMapper.deleteById(id);
        return CommonConstants.SUCCESS_MESSAGE;
    }

    private void checkDuplicateName(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getName, category.getName())
                .ne(ObjectUtils.isNotNull(category.getId()), Category::getId, category.getId());

        if (categoryMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("分类名称已存在");
        }
    }
}
