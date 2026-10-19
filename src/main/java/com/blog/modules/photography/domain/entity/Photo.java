package com.blog.modules.photography.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 摄影照片表
 *
 * @author xuesong.lei
 * @since 2026-10-19
 * @TableName t_photo
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel("摄影照片表")
@TableName(value = "t_photo")
public class Photo implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 相册ID
     */
    @ApiModelProperty("相册ID")
    @TableField(value = "album_id")
    private Long albumId;

    /**
     * 原始文件名
     */
    @ApiModelProperty("原始文件名")
    @TableField(value = "original_name")
    private String originalName;

    /**
     * 原始照片SHA-256摘要
     */
    @ApiModelProperty("原始照片SHA-256摘要")
    @TableField(value = "sha256")
    private String sha256;

    /**
     * 原始文件大小，单位字节
     */
    @ApiModelProperty("原始文件大小，单位字节")
    @TableField(value = "file_size")
    private Long fileSize;

    /**
     * 校正方向后的照片宽度
     */
    @ApiModelProperty("校正方向后的照片宽度")
    @TableField(value = "width")
    private Integer width;

    /**
     * 校正方向后的照片高度
     */
    @ApiModelProperty("校正方向后的照片高度")
    @TableField(value = "height")
    private Integer height;

    /**
     * 拍摄时间
     */
    @ApiModelProperty("拍摄时间")
    @TableField(value = "taken_at")
    private String takenAt;

    /**
     * 相机型号
     */
    @ApiModelProperty("相机型号")
    @TableField(value = "camera")
    private String camera;

    /**
     * 镜头型号
     */
    @ApiModelProperty("镜头型号")
    @TableField(value = "lens")
    private String lens;

    /**
     * 光圈
     */
    @ApiModelProperty("光圈")
    @TableField(value = "aperture")
    private String aperture;

    /**
     * 快门速度
     */
    @ApiModelProperty("快门速度")
    @TableField(value = "shutter")
    private String shutter;

    /**
     * ISO感光度
     */
    @ApiModelProperty("ISO感光度")
    @TableField(value = "iso")
    private String iso;

    /**
     * 原图文件ID
     */
    @ApiModelProperty("原图文件ID")
    @TableField(value = "original_file_id")
    @JsonIgnore
    private Long originalFileId;

    /**
     * 缩略图文件ID
     */
    @ApiModelProperty("缩略图文件ID")
    @TableField(value = "thumbnail_file_id")
    @JsonIgnore
    private Long thumbnailFileId;

    /**
     * 浏览图文件ID
     */
    @ApiModelProperty("浏览图文件ID")
    @TableField(value = "display_file_id")
    @JsonIgnore
    private Long displayFileId;

}
