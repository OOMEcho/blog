package com.blog.modules.common.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: xuesong.lei
 * @Date: 2026/2/2
 * @Description: Dashboard统计VO
 */
@Data
@ApiModel("Dashboard统计VO")
public class DashboardVO {

    @ApiModelProperty("文章总数")
    private Long articleCount;

    @ApiModelProperty("文章近7日增长百分比")
    private String articleGrowthRate;

    @ApiModelProperty("评论总数")
    private Long commentCount;

    @ApiModelProperty("评论近7日增长百分比")
    private String commentGrowthRate;

    @ApiModelProperty("用户总数")
    private Long userCount;

    @ApiModelProperty("用户近7日增长百分比")
    private String userGrowthRate;

    @ApiModelProperty("总浏览量")
    private Long viewCount;

    @ApiModelProperty("浏览量近7日增长百分比")
    private String viewGrowthRate;

}
