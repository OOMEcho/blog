package com.blog.modules.openproject.service;

import com.blog.modules.openproject.domain.dto.OpenProjectDTO;
import com.blog.modules.openproject.domain.entity.OpenProject;
import org.mapstruct.Mapper;

/**
 * 开源项目实体类转换
 *
 * @author xuesong.lei
 * @since 2026-04-12
 */
@Mapper(componentModel = "spring")
public interface OpenProjectConvert {

    OpenProject toOpenProject(OpenProjectDTO dto);
}
