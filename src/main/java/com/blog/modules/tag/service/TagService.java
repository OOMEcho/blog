package com.blog.modules.tag.service;

import com.blog.common.domain.vo.PageVO;
import com.blog.modules.tag.domain.dto.TagDTO;
import com.blog.modules.tag.domain.entity.Tag;

import java.util.List;

/**
 * 标签业务层
 *
 * @author xuesong.lei
 * @since 2026-02-24
 */
public interface TagService {

    /**
     * 分页列表
     *
     * @param dto 查询参数
     * @return 标签分页列表
     */
    PageVO<Tag> pageList(TagDTO dto);

    /**
     * 列表
     *
     * @return 标签列表
     */
    List<Tag> list();

    /**
     * 新增标签
     *
     * @param dto 标签DTO
     * @return 响应消息
     */
    String add(TagDTO dto);

    /**
     * 修改标签
     *
     * @param dto 标签DTO
     * @return 响应消息
     */
    String update(TagDTO dto);

    /**
     * 删除标签
     *
     * @param id 标签ID
     * @return 响应消息
     */
    String delete(Long id);
}
