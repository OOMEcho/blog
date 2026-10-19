package com.blog.modules.photography.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * 摄影相册内容DTO
 *
 * @author xuesong.lei
 * @since 2026-10-19
 */
@Data
@ApiModel("摄影相册内容DTO")
public class PhotoAlbumContentDTO {

    @ApiModelProperty("相册标题")
    @NotBlank(message = "相册标题不能为空")
    @Size(max = 120, message = "相册标题不能超过120个字符")
    private String title;

    @ApiModelProperty("相册介绍")
    @Size(max = 4000, message = "相册介绍不能超过4000个字符")
    private String description;

    @ApiModelProperty("拍摄日期")
    @Pattern(regexp = "^$|\\d{4}-\\d{2}-\\d{2}", message = "拍摄日期格式应为yyyy-MM-dd")
    private String date;

    @ApiModelProperty("封面照片ID")
    private Long coverPhotoId;

    @ApiModelProperty("按展示顺序排列的照片")
    @NotNull(message = "照片列表不能为空")
    @Size(max = 500, message = "每个相册最多支持500张照片")
    @Valid
    private List<@NotNull PhotoAlbumPhotoDTO> photos = new ArrayList<>();
}
