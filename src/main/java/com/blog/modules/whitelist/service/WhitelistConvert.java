package com.blog.modules.whitelist.service;

import com.blog.modules.whitelist.domain.dto.WhitelistDTO;
import com.blog.modules.whitelist.domain.entity.Whitelist;
import com.blog.modules.whitelist.domain.vo.WhitelistVO;
import org.mapstruct.Mapper;

/**
 * 白名单实体类转换
 *
 * @author xuesong.lei
 * @since 2025-09-08
 */
@Mapper(componentModel = "spring")
public interface WhitelistConvert {

    Whitelist toWhitelist(WhitelistDTO dto);

    WhitelistVO toWhitelistVo(Whitelist whitelist);
}
