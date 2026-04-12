package com.blog.modules.openproject.service;

import com.blog.common.domain.vo.PageVO;
import com.blog.modules.openproject.domain.dto.OpenProjectDTO;
import com.blog.modules.openproject.domain.entity.OpenProject;

/**
 * 开源项目服务接口
 *
 * @author xuesong.lei
 * @since 2026-04-12
 */
public interface OpenProjectService {

    /**
     * 分页查询开源项目
     *
     * @param dto 查询条件
     * @return 分页结果
     */
    PageVO<OpenProject> pageList(OpenProjectDTO dto);

    /**
     * 新增开源项目
     *
     * @param dto 数据
     * @return 结果
     */
    String add(OpenProjectDTO dto);

    /**
     * 修改开源项目
     *
     * @param dto 数据
     * @return 结果
     */
    String update(OpenProjectDTO dto);

    /**
     * 删除开源项目
     *
     * @param id ID
     * @return 结果
     */
    String delete(Long id);
}
