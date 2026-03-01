package com.blog.modules.whitelist.service;

import com.blog.modules.whitelist.domain.dto.WhitelistDTO;
import com.blog.modules.whitelist.domain.entity.Whitelist;
import com.blog.modules.whitelist.domain.vo.WhitelistVO;
import org.mapstruct.Mapper;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/8 22:27
 * @Description: 白名单实体类转换
 */
@Mapper(componentModel = "spring")
public interface WhitelistConvert {

    Whitelist toWhitelist(WhitelistDTO dto);

    WhitelistVO toWhitelistVo(Whitelist whitelist);
}
