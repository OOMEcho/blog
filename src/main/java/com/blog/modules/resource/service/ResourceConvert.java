package com.blog.modules.resource.service;

import com.blog.modules.resource.domain.dto.ResourceDTO;
import com.blog.modules.resource.domain.entity.Resource;
import com.blog.modules.resource.domain.vo.ResourceVO;
import org.mapstruct.Mapper;

/**
 * @Author: xuesong.lei
 * @Date: 2026/1/12 23:20
 * @Description: 资源类型转换类
 */
@Mapper(componentModel = "spring")
public interface ResourceConvert {

    Resource toResource(ResourceDTO dto);

    ResourceVO toResourceVo(Resource resource);
}
