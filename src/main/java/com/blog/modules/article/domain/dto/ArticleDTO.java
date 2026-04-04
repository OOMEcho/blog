package com.blog.modules.article.domain.dto;

import com.blog.common.domain.dto.PageDTO;
import com.blog.common.validator.EnumString;
import com.blog.common.validator.ValidGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;
import java.util.List;

/**
 * 文章DTO
 *
 * @author xuesong.lei
 * @since 2026-02-24
 */
@Data
@ApiModel("文章DTO")
@EqualsAndHashCode(callSuper = true)
public class ArticleDTO extends PageDTO {

    @ApiModelProperty("主键ID")
    @Null(groups = ValidGroup.Create.class, message = "文章ID必须为空")
    @NotNull(groups = ValidGroup.Update.class, message = "文章ID不能为空")
    private Long id;

    @ApiModelProperty("作者ID")
    @NotNull(groups = ValidGroup.Update.class, message = "作者ID不能为空")
    private Long authorId;

    @ApiModelProperty("文章标题")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "文章标题不能为空")
    private String title;

    @ApiModelProperty("文章摘要")
    private String summary;

    @ApiModelProperty("文章内容")
    @NotBlank(groups = ValidGroup.Create.class, message = "文章内容不能为空")
    private String content;

    @ApiModelProperty("封面图片")
    private String coverImage;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("状态(0=草稿,1=已发布,2=待审核,3=审核拒绝)")
    @EnumString(value = {"0", "1", "2", "3"}, groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "状态值不合法")
    private String status = "0";

    @ApiModelProperty("审核拒绝原因")
    private String rejectReason;

    @ApiModelProperty("是否置顶")
    private Integer isTop;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("标签ID列表")
    private List<Long> tagIds;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
