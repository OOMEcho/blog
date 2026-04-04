package com.blog.modules.category.service;

import com.blog.common.domain.vo.PageVO;
import com.blog.modules.category.domain.dto.CategoryDTO;
import com.blog.modules.category.domain.entity.Category;

import java.util.List;

/**
 * 分类业务层
 *
 * @author xuesong.lei
 * @since 2026-02-24
 */
public interface CategoryService {

    /**
     * 分页列表
     *
     * @param dto 查询参数
     * @return 分类分页列表
     */
    PageVO<Category> pageList(CategoryDTO dto);

    /**
     * 列表
     *
     * @return 分类列表
     */
    List<Category> list();

    /**
     * 新增分类
     *
     * @param dto 分类DTO
     * @return 响应消息
     */
    String add(CategoryDTO dto);

    /**
     * 修改分类
     *
     * @param dto 分类DTO
     * @return 响应消息
     */
    String update(CategoryDTO dto);

    /**
     * 删除分类
     *
     * @param id 分类ID
     * @return 响应消息
     */
    String delete(Long id);
}
