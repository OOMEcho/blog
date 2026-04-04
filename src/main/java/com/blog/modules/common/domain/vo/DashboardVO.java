package com.blog.modules.common.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Dashboard统计VO
 *
 * @author xuesong.lei
 * @since 2026-02-02
 */
@Data
@ApiModel("Dashboard统计VO")
public class DashboardVO {

    @ApiModelProperty("文章总数")
    private Long articleCount;

    @ApiModelProperty("文章近7日增长百分比")
    private String articleGrowthRate;

    @ApiModelProperty("用户总数")
    private Long userCount;

    @ApiModelProperty("用户近7日增长百分比")
    private String userGrowthRate;

    @ApiModelProperty("总浏览量")
    private Long viewCount;

    @ApiModelProperty("浏览量近7日增长百分比")
    private String viewGrowthRate;

}
