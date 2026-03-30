package com.blog.modules.menu.service;

import com.blog.modules.menu.domain.dto.MenuDTO;
import com.blog.modules.menu.domain.entity.Menu;
import com.blog.modules.menu.domain.vo.MenuVO;
import org.mapstruct.Mapper;

/**
 * 菜单类型转换类
 *
 * @author xuesong.lei
 * @since 2025/9/13 16:20
 */
@Mapper(componentModel = "spring")
public interface MenuConvert {

    MenuVO toMenuVo(Menu menu);

    Menu toMenu(MenuDTO dto);
}
