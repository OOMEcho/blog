package com.blog.modules.category.domain.dto;

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
 * 分类DTO
 *
 * @author xuesong.lei
 * @since 2026-02-24 10:00:00
 */
@Data
@ApiModel("分类DTO")
@EqualsAndHashCode(callSuper = true)
public class CategoryDTO extends PageDTO {

    @ApiModelProperty("主键ID")
    @Null(groups = ValidGroup.Create.class, message = "分类ID必须为空")
    @NotNull(groups = ValidGroup.Update.class, message = "分类ID不能为空")
    private Long id;

    @ApiModelProperty("分类名称")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "分类名称不能为空")
    private String name;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("描述")
    private String description;
}
