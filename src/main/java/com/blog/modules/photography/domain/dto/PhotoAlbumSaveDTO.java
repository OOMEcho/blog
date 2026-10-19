package com.blog.modules.photography.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 摄影相册草稿保存DTO
 *
 * @author xuesong.lei
 * @since 2026-10-19
 */
@Data
@ApiModel("摄影相册草稿保存DTO")
public class PhotoAlbumSaveDTO {

    @ApiModelProperty("相册ID")
    @NotNull(message = "相册ID不能为空")
    private Long id;

    @ApiModelProperty("当前修订版本")
    @NotNull(message = "相册修订版本不能为空")
    private Integer revision;

    @ApiModelProperty("相册草稿内容")
    @NotNull(message = "相册草稿内容不能为空")
    @Valid
    private PhotoAlbumContentDTO content;
}
