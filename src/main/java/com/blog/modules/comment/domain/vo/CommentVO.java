package com.blog.modules.comment.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 评论VO
 */
@Data
@ApiModel("评论VO")
public class CommentVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("文章ID")
    private Long articleId;

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("评论用户ID")
    private Long userId;

    @ApiModelProperty("评论用户昵称")
    private String nickname;

    @ApiModelProperty("评论用户头像")
    private String avatar;

    @ApiModelProperty("父评论ID")
    private Long parentId;

    @ApiModelProperty("回复目标评论ID")
    private Long replyToId;

    @ApiModelProperty("回复目标用户昵称")
    private String replyToNickname;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("状态(0=待审核,1=已通过,2=已拒绝)")
    private String status;

    @ApiModelProperty("IP地址")
    private String ip;

    @ApiModelProperty("归属地")
    private String location;

    @ApiModelProperty("浏览器")
    private String browser;

    @ApiModelProperty("操作系统")
    private String os;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("子评论列表")
    private List<CommentVO> children;
}
