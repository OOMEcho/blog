package com.blog.modules.blogconfig.mapper;

import com.blog.modules.blogconfig.domain.entity.BlogConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 针对表【t_blog_config(博客配置表)】的数据库操作Mapper
 *
 * @author xuesong.lei
 * @since 2026-02-24
 * @Entity: com.blog.modules.blogconfig.domain.entity.BlogConfig
 */
@Mapper
public interface BlogConfigMapper extends BaseMapper<BlogConfig> {

}
