package com.blog.modules.role.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 角色VO
 *
 * @author xuesong.lei
 * @since 2026-02-27
 */
@Data
@ApiModel("角色VO")
public class RoleVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色编码")
    private String roleCode;

    @ApiModelProperty("显示顺序")
    private Integer orderNum;

    @ApiModelProperty("角色状态(0-正常,1-停用)")
    private String status;
}
