package com.blog.modules.photography.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 摄影照片VO
 *
 * @author xuesong.lei
 * @since 2026-10-19
 */
@Data
@ApiModel("摄影照片VO")
public class PhotoVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("相册ID")
    private Long albumId;

    @ApiModelProperty("原始文件名")
    private String originalName;

    @ApiModelProperty("原始照片SHA-256摘要")
    private String sha256;

    @ApiModelProperty("原始文件大小，单位字节")
    private Long fileSize;

    @ApiModelProperty("校正方向后的照片宽度")
    private Integer width;

    @ApiModelProperty("校正方向后的照片高度")
    private Integer height;

    @ApiModelProperty("拍摄时间")
    private String takenAt;

    @ApiModelProperty("相机型号")
    private String camera;

    @ApiModelProperty("镜头型号")
    private String lens;

    @ApiModelProperty("光圈")
    private String aperture;

    @ApiModelProperty("快门速度")
    private String shutter;

    @ApiModelProperty("ISO感光度")
    private String iso;

    @ApiModelProperty("照片说明")
    private String caption;
}
