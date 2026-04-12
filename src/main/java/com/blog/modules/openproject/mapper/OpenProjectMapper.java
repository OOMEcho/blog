package com.blog.modules.openproject.mapper;

import com.blog.modules.openproject.domain.entity.OpenProject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 针对表【t_open_project(开源项目表)】的数据库操作Mapper
 *
 * @author xuesong.lei
 * @since 2026-04-12
 * @Entity com.blog.modules.openproject.domain.entity.OpenProject
 */
@Mapper
public interface OpenProjectMapper extends BaseMapper<OpenProject> {

}
