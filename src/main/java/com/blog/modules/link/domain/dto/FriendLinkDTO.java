package com.blog.modules.link.domain.dto;

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
 * 友情链接DTO
 *
 * @author xuesong.lei
 * @since 2026-02-24
 */
@Data
@ApiModel("友情链接DTO")
@EqualsAndHashCode(callSuper = true)
public class FriendLinkDTO extends PageDTO {

    @ApiModelProperty("主键ID")
    @Null(groups = ValidGroup.Create.class, message = "友情链接ID必须为空")
    @NotNull(groups = ValidGroup.Update.class, message = "友情链接ID不能为空")
    private Long id;

    @ApiModelProperty("链接名称")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "链接名称不能为空")
    private String name;

    @ApiModelProperty("链接地址")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "链接地址不能为空")
    private String url;

    @ApiModelProperty("链接Logo")
    private String logo;

    @ApiModelProperty("链接描述")
    private String description;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态(0=正常,1=停用)")
    private String status;
}
