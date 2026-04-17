package com.blog.modules.log.mapper;

import com.blog.modules.common.domain.vo.AccessTrendVO;
import com.blog.modules.log.domain.entity.ArticleViewLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 针对表【t_article_view_log(文章访问日志表)】的数据库操作Mapper
 *
 * @author xuesong.lei
 * @since 2026-04-17
 * @Entity: com.blog.modules.log.domain.entity.ArticleViewLog
 */
public interface ArticleViewLogMapper extends BaseMapper<ArticleViewLog> {

    /**
     * 按天统计文章访问量
     *
     * @param days 天数
     * @return 访问趋势列表
     */
    List<AccessTrendVO> selectAccessTrend(@Param("days") Integer days);
}
