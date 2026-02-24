package com.blog.modules.resource.service;

import com.blog.common.domain.vo.PageVO;
import com.blog.modules.resource.domain.dto.ResourceDTO;
import com.blog.modules.resource.domain.entity.Resource;

/**
 * @Author: xuesong.lei
 * @Date: 2026/1/12 23:16
 * @Description: 资源业务层
 */
public interface ResourceService {

    /**
     * 分页列表
     */
    PageVO<Resource> pageList(ResourceDTO dto);

    /**
     * 详情
     */
    Resource detail(Long id);

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
