package com.blog.modules.article.service;

import com.blog.common.domain.vo.PageVO;
import com.blog.modules.article.domain.dto.ArticleDTO;
import com.blog.modules.article.domain.vo.ArticleVO;

/**
 * @Author: xuesong.lei
 * @Date: 2026-02-24 10:00:00
 * @Description: 文章业务层
 */
public interface ArticleService {

    /**
     * 分页列表
     *
     * @param dto 查询参数
     * @return 文章分页列表
     */
    PageVO<ArticleVO> pageList(ArticleDTO dto);

    /**
     * 新增文章
     *
     * @param dto 文章DTO
     * @return 响应消息
     */
    String add(ArticleDTO dto);

    /**
     * 修改文章
     *
     * @param dto 文章DTO
     * @return 响应消息
     */
    String update(ArticleDTO dto);

    /**
     * 删除文章
     *
     * @param id 文章ID
     * @return 响应消息
     */
    String delete(Long id);

    /**
     * 审核文章
     *
     * @param id           文章ID
     * @param status       审核状态(1=通过,3=拒绝)
     * @param rejectReason 拒绝原因(status=3时填写)
     * @return 响应消息
     */
    String audit(Long id, String status, String rejectReason);
}
