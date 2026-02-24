package com.blog.modules.blogconfig.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 博客配置DTO
 */
@Data
@ApiModel("博客配置DTO")
public class BlogConfigDTO {

    @ApiModelProperty("配置键")
    @NotBlank(message = "配置键不能为空")
    private String configKey;

    @ApiModelProperty("配置值")
    private String configValue;
}
