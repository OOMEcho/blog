package com.blog.modules.photography.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 相册内照片内容DTO
 *
 * @author xuesong.lei
 * @since 2026-10-19
 */
@Data
@ApiModel("相册内照片内容DTO")
public class PhotoAlbumPhotoDTO {

    @ApiModelProperty("照片ID")
    @NotNull(message = "照片ID不能为空")
    private Long photoId;

    @ApiModelProperty("照片说明")
    @Size(max = 1000, message = "照片说明不能超过1000个字符")
    private String caption;

    @ApiModelProperty("拍摄时间")
    @Size(max = 32, message = "拍摄时间不能超过32个字符")
    private String takenAt;
}
