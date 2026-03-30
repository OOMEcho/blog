package com.blog.modules.dict.service;

import com.blog.modules.dict.domain.dto.DictionaryDTO;
import com.blog.modules.dict.domain.entity.Dictionary;
import com.blog.modules.dict.domain.vo.DictionaryVO;
import org.mapstruct.Mapper;

/**
 * 字典实体类转换
 *
 * @author xuesong.lei
 * @since 2025/09/08 22:23
 */
@Mapper(componentModel = "spring")
public interface DictionaryConvert {

    Dictionary toDictionary(DictionaryDTO dto);

    DictionaryVO toDictionaryVo(Dictionary dictionary);
}
