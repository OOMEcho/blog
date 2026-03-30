package com.blog.modules.tag.domain.dto;

import com.blog.common.domain.dto.PageDTO;
import com.blog.common.validator.ValidGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 标签DTO
 *
 * @author xuesong.lei
 * @since 2026-02-24 10:00:00
 */
@Data
@ApiModel("标签DTO")
@EqualsAndHashCode(callSuper = true)
public class TagDTO extends PageDTO {

    @ApiModelProperty("主键ID")
    @Null(groups = ValidGroup.Create.class, message = "标签ID必须为空")
    @NotNull(groups = ValidGroup.Update.class, message = "标签ID不能为空")
    private Long id;

    @ApiModelProperty("标签名称")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "标签名称不能为空")
    private String name;

    @ApiModelProperty("标签颜色")
    private String color;
}
