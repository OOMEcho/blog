package com.blog.modules.photography.domain.dto;

import com.blog.modules.photography.domain.entity.Photo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 摄影照片处理结果DTO
 *
 * @author xuesong.lei
 * @since 2026-10-19
 */
@Data
@ApiModel("摄影照片处理结果DTO")
public class PhotoImageResultDTO {

    @ApiModelProperty("照片元数据")
    private Photo photo = new Photo();

    @ApiModelProperty("缩略图数据")
    private byte[] thumbnail;

    @ApiModelProperty("浏览图数据")
    private byte[] display;
}
