package com.blog.modules.openproject.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.blog.common.constant.CommonConstants;
import com.blog.common.domain.vo.PageVO;
import com.blog.common.exception.BusinessException;
import com.blog.modules.openproject.domain.dto.OpenProjectDTO;
import com.blog.modules.openproject.domain.entity.OpenProject;
import com.blog.modules.openproject.mapper.OpenProjectMapper;
import com.blog.modules.openproject.service.OpenProjectConvert;
import com.blog.modules.openproject.service.OpenProjectService;
import com.blog.utils.PageUtils;
import com.blog.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 开源项目业务实现层
 *
 * @author xuesong.lei
 * @since 2026-04-12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpenProjectServiceImpl implements OpenProjectService {

    private final OpenProjectMapper openProjectMapper;

    private final OpenProjectConvert openProjectConvert;

    private static final Pattern GITHUB_PATTERN = Pattern.compile("github\\.com/([^/]+)/([^/]+)");
    private static final Pattern GITEE_PATTERN = Pattern.compile("gitee\\.com/([^/]+)/([^/]+)");

    @Override
    public PageVO<OpenProject> pageList(OpenProjectDTO dto) {
        LambdaQueryWrapper<OpenProject> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(dto.getName()), OpenProject::getName, dto.getName());
        queryWrapper.orderByDesc(OpenProject::getSort, OpenProject::getCreateTime);
        return PageUtils.of(dto).paging(openProjectMapper, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(OpenProjectDTO dto) {
        OpenProject openProject = openProjectConvert.toOpenProject(dto);

        // 检查项目名称是否重复
        checkDuplicateName(openProject);

        // 自动获取 star 数量
        fetchStars(openProject);

        openProject.setCreateBy(SecurityUtils.getUserId());
        openProjectMapper.insert(openProject);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String update(OpenProjectDTO dto) {
        OpenProject openProject = openProjectConvert.toOpenProject(dto);

        // 检查项目名称是否重复（排除自身）
        checkDuplicateName(openProject);

        // 自动获取 star 数量
        fetchStars(openProject);

        openProject.setUpdateBy(SecurityUtils.getUserId());
        openProjectMapper.updateById(openProject);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(Long id) {
        openProjectMapper.deleteById(id);
        return CommonConstants.SUCCESS_MESSAGE;
    }

    private void checkDuplicateName(OpenProject openProject) {
        LambdaQueryWrapper<OpenProject> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OpenProject::getName, openProject.getName())
                .ne(ObjectUtils.isNotNull(openProject.getId()), OpenProject::getId, openProject.getId());

        if (openProjectMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("项目名称已存在");
        }
    }

    /**
     * 自动从 GitHub 或 Gitee 获取 Star 数量
     *
     * @param project 项目实体
     */
    private void fetchStars(OpenProject project) {
        String url = project.getUrl();
        if (StringUtils.isBlank(url)) {
            return;
        }

        try {
            Matcher githubMatcher = GITHUB_PATTERN.matcher(url);
            if (githubMatcher.find()) {
                String owner = githubMatcher.group(1);
                String repo = githubMatcher.group(2);
                if (repo.endsWith(".git")) {
                    repo = repo.substring(0, repo.length() - 4);
                }
                String apiUrl = String.format("https://api.github.com/repos/%s/%s", owner, repo);
                // GitHub API 建议设置 User-Agent
                String result = HttpUtil.createGet(apiUrl)
                        .header("User-Agent", "Mozilla/5.0")
                        .execute().body();
                JSONObject json = JSONUtil.parseObj(result);
                if (json.containsKey("stargazers_count")) {
                    project.setStars(json.getInt("stargazers_count"));
                }
            } else {
                Matcher giteeMatcher = GITEE_PATTERN.matcher(url);
                if (giteeMatcher.find()) {
                    String owner = giteeMatcher.group(1);
                    String repo = giteeMatcher.group(2);
                    if (repo.endsWith(".git")) {
                        repo = repo.substring(0, repo.length() - 4);
                    }
                    String apiUrl = String.format("https://gitee.com/api/v5/repos/%s/%s", owner, repo);
                    String result = HttpUtil.createGet(apiUrl)
                            .header("User-Agent", "Mozilla/5.0")
                            .execute().body();
                    JSONObject json = JSONUtil.parseObj(result);
                    if (json.containsKey("stargazers_count")) {
                        project.setStars(json.getInt("stargazers_count"));
                    }
                }
            }
        } catch (Exception e) {
            log.warn("自动获取 Star 数量失败，URL: {}, 原因: {}", url, e.getMessage());
        }
    }
}
