package com.blog.modules.article.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.blog.modules.tag.domain.entity.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 文章表
 * @TableName t_article
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel("文章表")
@TableName(value = "t_article")
public class Article implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    @TableField(value = "create_by")
    private Long createBy;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    @TableField(value = "update_by")
    private Long updateBy;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 逻辑删除标记(0=正常,1=删除)
     */
    @ApiModelProperty("逻辑删除标记(0=正常,1=删除)")
    @TableLogic
    @TableField(value = "deleted")
    private Integer deleted;

    /**
     * 版本号,用于乐观锁
     */
    @ApiModelProperty("版本号,用于乐观锁")
    @Version
    @TableField(value = "version")
    private Integer version;

    /**
     * 文章标题
     */
    @ApiModelProperty("文章标题")
    @TableField(value = "title")
    private String title;

    /**
     * 文章摘要
     */
    @ApiModelProperty("文章摘要")
    @TableField(value = "summary")
    private String summary;

    /**
     * 文章内容
     */
    @ApiModelProperty("文章内容")
    @TableField(value = "content")
    private String content;

    /**
     * 封面图片
     */
    @ApiModelProperty("封面图片")
    @TableField(value = "cover_image")
    private String coverImage;

    /**
     * 分类ID
     */
    @ApiModelProperty("分类ID")
    @TableField(value = "category_id")
    private Long categoryId;

    /**
     * 状态(0=草稿,1=已发布,2=待审核,3=审核拒绝)
     */
    @ApiModelProperty("状态(0=草稿,1=已发布,2=待审核,3=审核拒绝)")
    @TableField(value = "status")
    private String status;

    /**
     * 审核拒绝原因
     */
    @ApiModelProperty("审核拒绝原因")
    @TableField(value = "reject_reason")
    private String rejectReason;

    /**
     * 是否置顶(0=否,1=是)
     */
    @ApiModelProperty("是否置顶(0=否,1=是)")
    @TableField(value = "is_top")
    private Integer isTop;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    @TableField(value = "sort")
    private Integer sort;

    /**
     * 浏览量
     */
    @ApiModelProperty("浏览量")
    @TableField(value = "view_count")
    private Long viewCount;

    /**
     * 点赞量
     */
    @ApiModelProperty("点赞量")
    @TableField(value = "like_count")
    private Long likeCount;

    /**
     * 分类名称(非数据库字段)
     */
    @ApiModelProperty("分类名称")
    @TableField(exist = false)
    private String categoryName;

    /**
     * 作者名称(非数据库字段)
     */
    @ApiModelProperty("作者名称")
    @TableField(exist = false)
    private String authorName;

    /**
     * 标签列表(非数据库字段)
     */
    @ApiModelProperty("标签列表")
    @TableField(exist = false)
    private List<Tag> tagList;
}
