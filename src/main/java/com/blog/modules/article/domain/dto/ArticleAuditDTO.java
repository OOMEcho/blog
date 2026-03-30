package com.blog.modules.article.domain.dto;

import com.blog.common.validator.EnumString;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 文章审核DTO
 *
 * @author xuesong.lei
 * @since 2026-03-16
 */
@Data
@ApiModel("文章审核DTO")
public class ArticleAuditDTO {

    @ApiModelProperty("审核状态(1=通过,3=拒绝)")
    @NotBlank(message = "审核状态不能为空")
    @EnumString(value = {"1", "3"}, message = "审核状态不合法，只能为通过(1)或拒绝(3)")
    private String status;

    @ApiModelProperty("拒绝原因(status=3时必填)")
    private String rejectReason;
}
