package com.blog.modules.photography.domain.dto;

import com.blog.common.domain.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 摄影照片分页查询DTO
 *
 * @author xuesong.lei
 * @since 2026-10-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("摄影照片分页查询DTO")
public class PhotoQueryDTO extends PageDTO {

    public PhotoQueryDTO() {
        setPageSize(24);
    }

    @Override
    public Integer getPageSize() {
        return Math.min(super.getPageSize(), 60);
    }
}
