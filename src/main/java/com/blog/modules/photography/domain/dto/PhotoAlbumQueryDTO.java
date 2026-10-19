package com.blog.modules.photography.domain.dto;

import com.blog.common.domain.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 摄影相册分页查询DTO
 *
 * @author xuesong.lei
 * @since 2026-10-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("摄影相册分页查询DTO")
public class PhotoAlbumQueryDTO extends PageDTO {

    public PhotoAlbumQueryDTO() {
        setPageSize(12);
    }

    @Override
    public Integer getPageSize() {
        return Math.min(super.getPageSize(), 60);
    }
}
