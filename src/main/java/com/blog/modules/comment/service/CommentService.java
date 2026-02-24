package com.blog.modules.comment.service;

import com.blog.common.domain.vo.PageVO;
import com.blog.modules.comment.domain.dto.CommentDTO;
import com.blog.modules.comment.domain.vo.CommentVO;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 评论业务层
 */
public interface CommentService {

    /**
     * 分页列表(管理端)
     *
     * @param dto 查询参数
     * @return 评论分页列表
     */
    PageVO<CommentVO> pageList(CommentDTO dto);

    /**
     * 审核评论(通过/拒绝)
     *
     * @param id     评论ID
     * @param status 状态(1=通过,2=拒绝)
     * @return 响应消息
     */
    String updateStatus(Long id, String status);

    /**
     * 删除评论
     *
     * @param id 评论ID
     * @return 响应消息
     */
    String delete(Long id);
}
