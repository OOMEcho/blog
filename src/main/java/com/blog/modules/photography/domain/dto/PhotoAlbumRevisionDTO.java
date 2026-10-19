package com.blog.modules.photography.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 摄影相册版本校验DTO
 *
 * @author xuesong.lei
 * @since 2026-10-19
 */
@Data
@ApiModel("摄影相册版本校验DTO")
public class PhotoAlbumRevisionDTO {

    @ApiModelProperty("当前修订版本")
    @NotNull(message = "相册修订版本不能为空")
    private Integer revision;
}
