package com.blog.modules.comment.domain.dto;

import com.blog.common.domain.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 评论DTO
 */
@Data
@ApiModel("评论DTO")
@EqualsAndHashCode(callSuper = true)
public class CommentDTO extends PageDTO {

    @ApiModelProperty("文章ID")
    @NotNull(message = "文章ID不能为空")
    private Long articleId;

    @ApiModelProperty("评论用户ID")
    private Long userId;

    @ApiModelProperty("父评论ID")
    private Long parentId;

    @ApiModelProperty("回复目标评论ID")
    private Long replyToId;

    @ApiModelProperty("评论内容")
    @NotBlank(message = "评论内容不能为空")
    private String content;

    @ApiModelProperty("状态(0=待审核,1=已通过,2=已拒绝)")
    private String status;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
