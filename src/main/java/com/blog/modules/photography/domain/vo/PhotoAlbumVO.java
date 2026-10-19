package com.blog.modules.photography.domain.vo;

import com.blog.modules.photography.domain.dto.PhotoAlbumContentDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 摄影相册VO
 *
 * @author xuesong.lei
 * @since 2026-10-19
 */
@Data
@ApiModel("摄影相册VO")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhotoAlbumVO {

    @ApiModelProperty("相册ID")
    private Long id;

    @ApiModelProperty("相册标题")
    private String title;

    @ApiModelProperty("相册介绍")
    private String description;

    @ApiModelProperty("拍摄日期")
    private String date;

    @ApiModelProperty("封面照片ID")
    private Long coverPhotoId;

    @ApiModelProperty("照片数量")
    private Integer photoCount;

    @ApiModelProperty("相册状态")
    private String status;

    @ApiModelProperty("当前修订版本")
    private Integer revision;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedAt;

    @ApiModelProperty("是否存在未发布修改")
    private Boolean hasUnpublishedChanges;

    @ApiModelProperty("相册草稿内容")
    private PhotoAlbumContentDTO content;

    @ApiModelProperty("草稿照片列表")
    private List<PhotoVO> photos;
}
