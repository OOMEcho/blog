package com.blog.modules.resource.service;

import com.blog.modules.resource.domain.dto.ResourceDTO;
import com.blog.modules.resource.domain.entity.Resource;
import com.blog.modules.resource.domain.vo.ResourceVO;
import org.mapstruct.Mapper;

/**
 * 资源类型转换类
 *
 * @author xuesong.lei
 * @since 2026-01-12
 */
@Mapper(componentModel = "spring")
public interface ResourceConvert {

    Resource toResource(ResourceDTO dto);

    ResourceVO toResourceVo(Resource resource);
}
