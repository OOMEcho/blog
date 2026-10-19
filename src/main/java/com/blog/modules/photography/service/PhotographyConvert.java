package com.blog.modules.photography.service;

import com.blog.modules.photography.domain.dto.PhotoAlbumContentDTO;
import com.blog.modules.photography.domain.entity.Photo;
import com.blog.modules.photography.domain.vo.PhotoAlbumVO;
import com.blog.modules.photography.domain.vo.PhotoVO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 摄影相册及照片实体类转换
 *
 * @author xuesong.lei
 * @since 2026-10-19
 */
@Mapper(componentModel = "spring")
public interface PhotographyConvert {

    @Mapping(target = "caption", ignore = true)
    PhotoVO toPhotoVO(Photo photo);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "coverPhotoId", source = "coverPhotoId")
    @Mapping(target = "photoCount", expression = "java(content.getPhotos().size())")
    PhotoAlbumVO toPhotoAlbumVO(PhotoAlbumContentDTO content);
}
