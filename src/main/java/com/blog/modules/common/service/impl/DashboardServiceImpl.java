package com.blog.modules.common.service.impl;

import com.blog.modules.article.domain.entity.Article;
import com.blog.modules.article.mapper.ArticleMapper;
import com.blog.modules.common.domain.vo.AccessTrendVO;
import com.blog.modules.common.domain.vo.DashboardVO;
import com.blog.modules.common.service.DashboardService;
import com.blog.modules.log.mapper.SysLoginLogMapper;
import com.blog.modules.user.domain.entity.User;
import com.blog.modules.user.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 首页业务实现层
 *
 * @author xuesong.lei
 * @since 2026-02-02
 */
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserMapper userMapper;

    private final ArticleMapper articleMapper;

    private final SysLoginLogMapper sysLoginLogMapper;

    @Override
    public DashboardVO getStatistics() {
        DashboardVO vo = new DashboardVO();

        // 获取7天前的日期
        Date sevenDaysAgo = getDateBefore(7);

        // 文章统计（仅已发布）
        Long articleCount = articleMapper.selectCount(
                new LambdaQueryWrapper<Article>().eq(Article::getStatus, "1")
        );
        Long articleCountBefore = articleMapper.selectCount(
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getStatus, "1")
                        .lt(Article::getCreateTime, sevenDaysAgo)
        );
        vo.setArticleCount(articleCount);
        vo.setArticleGrowthRate(calculateGrowthRate(articleCount, articleCountBefore));

        // 用户统计
        Long userCount = userMapper.selectCount(null);
        Long userCountBefore = userMapper.selectCount(
                new LambdaQueryWrapper<User>().lt(User::getCreateTime, sevenDaysAgo)
        );
        vo.setUserCount(userCount);
        vo.setUserGrowthRate(calculateGrowthRate(userCount, userCountBefore));

        // 浏览量统计（已发布文章的总浏览量）
        LambdaQueryWrapper<Article> viewWrapper = new LambdaQueryWrapper<>();
        viewWrapper.eq(Article::getStatus, "1");
        List<Article> allArticles = articleMapper.selectList(viewWrapper);
        long totalViews = 0L;
        for (Article article : allArticles) {
            if (article.getViewCount() != null) {
                totalViews += article.getViewCount();
            }
        }
        vo.setViewCount(totalViews);

        // 浏览量增长：当前总浏览 vs 7天前已存在文章的浏览量（近似）
        LambdaQueryWrapper<Article> viewBeforeWrapper = new LambdaQueryWrapper<>();
        viewBeforeWrapper.eq(Article::getStatus, "1")
                .lt(Article::getCreateTime, sevenDaysAgo);
        List<Article> oldArticles = articleMapper.selectList(viewBeforeWrapper);
        long oldViews = 0L;
        for (Article article : oldArticles) {
            if (article.getViewCount() != null) {
                oldViews += article.getViewCount();
            }
        }
        vo.setViewGrowthRate(calculateGrowthRate(totalViews, oldViews));

        return vo;
    }

    @Override
    public List<AccessTrendVO> getAccessTrend(Integer days) {
        // 参数校验，只允许7或30，默认7
        if (days == null || (days != 7 && days != 30)) {
            days = 7;
        }
        return sysLoginLogMapper.selectAccessTrend(days);
    }

    /**
     * 获取指定天数之前的日期
     *
     * @param days 天数
     * @return 日期
     */
    private Date getDateBefore(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 计算增长率
     * 公式：(当前总数 - 7天前总数) / 7天前总数 × 100%
     *
     * @param currentCount 当前总数
     * @param beforeCount  7天前总数
     * @return 格式化后的增长率字符串，如 "+2.4%" 或 "-1.5%"
     */
    private String calculateGrowthRate(Long currentCount, Long beforeCount) {
        if (beforeCount == null || beforeCount == 0) {
            // 如果7天前没有数据，则新增数量即为增长
            if (currentCount != null && currentCount > 0) {
                return "+100.0%";
            }
            return "+0.0%";
        }

        long newCount = currentCount - beforeCount;
        BigDecimal rate = BigDecimal.valueOf(newCount)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(beforeCount), 1, RoundingMode.HALF_UP);

        String prefix = rate.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "";
        return prefix + rate.toPlainString() + "%";
    }
}
