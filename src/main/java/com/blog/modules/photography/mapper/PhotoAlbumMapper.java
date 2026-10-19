package com.blog.modules.photography.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.modules.photography.domain.entity.PhotoAlbum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 针对表【t_photo_album(摄影相册表)】的数据库操作Mapper
 *
 * @author xuesong.lei
 * @since 2026-10-19
 * @Entity: com.blog.modules.photography.domain.entity.PhotoAlbum
 */
@Mapper
public interface PhotoAlbumMapper extends BaseMapper<PhotoAlbum> {

    /**
     * 锁定相册记录，串行处理草稿和发布快照的修改
     */
    PhotoAlbum lock(@Param("id") Long id);
}
