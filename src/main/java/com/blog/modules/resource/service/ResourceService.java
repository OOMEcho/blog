package com.blog.modules.resource.service;

import com.blog.common.domain.vo.PageVO;
import com.blog.modules.resource.domain.dto.ResourceDTO;
import com.blog.modules.resource.domain.vo.ResourceVO;

/**
 * 资源业务层
 *
 * @author xuesong.lei
 * @since 2026/1/12 23:16
 */
public interface ResourceService {

    /**
     * 分页列表
     */
    PageVO<ResourceVO> pageList(ResourceDTO dto);

    /**
     * 详情
     */
    ResourceVO detail(Long id);

    /**
     * 删除
     */
    String delete(Long id);

    /**
     * 新增
     */
    String add(ResourceDTO dto);

    /**
     * 修改
     */
    String update(ResourceDTO dto);
}
