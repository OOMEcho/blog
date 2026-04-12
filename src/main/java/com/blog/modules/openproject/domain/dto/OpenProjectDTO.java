package com.blog.modules.openproject.domain.dto;

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
 * 开源项目DTO
 *
 * @author xuesong.lei
 * @since 2026-04-12
 */
@Data
@ApiModel("开源项目DTO")
@EqualsAndHashCode(callSuper = true)
public class OpenProjectDTO extends PageDTO {

    @ApiModelProperty("主键ID")
    @Null(groups = ValidGroup.Create.class, message = "开源项目ID必须为空")
    @NotNull(groups = ValidGroup.Update.class, message = "开源项目ID不能为空")
    private Long id;

    @ApiModelProperty("项目名称")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "项目名称不能为空")
    private String name;

    @ApiModelProperty("项目地址")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "项目地址不能为空")
    private String url;

    @ApiModelProperty("项目Logo")
    private String logo;

    @ApiModelProperty("项目描述")
    private String description;

    @ApiModelProperty("技术栈")
    private String techStack;

    @ApiModelProperty("Star数量")
    private Integer stars;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态(0=正常,1=停用)")
    private String status;
}
