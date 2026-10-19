package com.blog.modules.photography.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.modules.photography.domain.entity.Photo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 针对表【t_photo(摄影照片表)】的数据库操作Mapper
 *
 * @author xuesong.lei
 * @since 2026-10-19
 * @Entity: com.blog.modules.photography.domain.entity.Photo
 */
@Mapper
public interface PhotoMapper extends BaseMapper<Photo> {
}
