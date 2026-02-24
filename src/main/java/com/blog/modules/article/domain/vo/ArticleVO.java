package com.blog.modules.article.domain.vo;

import com.blog.modules.tag.domain.entity.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 文章VO
 */
@Data
@ApiModel("文章VO")
public class ArticleVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章摘要")
    private String summary;

    @ApiModelProperty("文章内容")
    private String content;

    @ApiModelProperty("封面图片")
    private String coverImage;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("状态(0=草稿,1=已发布,2=待审核,3=审核拒绝)")
    private String status;

    @ApiModelProperty("审核拒绝原因")
    private String rejectReason;

    @ApiModelProperty("是否置顶")
    private Integer isTop;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("浏览量")
    private Long viewCount;

    @ApiModelProperty("点赞量")
    private Long likeCount;

    @ApiModelProperty("作者ID")
    private Long authorId;

    @ApiModelProperty("作者名称")
    private String authorName;

    @ApiModelProperty("作者头像")
    private String authorAvatar;

    @ApiModelProperty("标签列表")
    private List<Tag> tagList;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
