package com.blog.modules.photography.controller;

import com.blog.common.domain.vo.PageVO;
import com.blog.common.duplicate.PreventDuplicateSubmit;
import com.blog.common.log.BusinessType;
import com.blog.common.log.OperationLog;
import com.blog.modules.photography.domain.dto.PhotoAlbumContentDTO;
import com.blog.modules.photography.domain.dto.PhotoAlbumQueryDTO;
import com.blog.modules.photography.domain.dto.PhotoAlbumRevisionDTO;
import com.blog.modules.photography.domain.dto.PhotoAlbumSaveDTO;
import com.blog.modules.photography.domain.vo.PhotoAlbumVO;
import com.blog.modules.photography.service.PhotographyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 摄影管理接口
 *
 * @author xuesong.lei
 * @since 2026-10-19
 */
@RestController
@Api(tags = "摄影管理接口")
@RequestMapping("/photography")
@RequiredArgsConstructor
public class PhotographyController {

    private final PhotographyService photographyService;

    @ApiOperation("分页查询摄影相册")
    @GetMapping("/pageList")
    public PageVO<PhotoAlbumVO> pageList(PhotoAlbumQueryDTO dto) {
        return photographyService.pageList(dto);
    }

    @ApiOperation("新增摄影相册")
    @PostMapping("/add")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "新增摄影相册", businessType = BusinessType.INSERT)
    public PhotoAlbumVO add(@Validated @RequestBody PhotoAlbumContentDTO dto) {
        return photographyService.add(dto);
    }

    @ApiOperation("摄影相册草稿详情")
    @GetMapping("/detail/{id}")
    public PhotoAlbumVO detail(@PathVariable("id") Long id) {
        return photographyService.detail(id);
    }

    @ApiOperation("保存摄影相册草稿")
    @PutMapping("/update")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改摄影相册", businessType = BusinessType.UPDATE)
    public PhotoAlbumVO update(@Validated @RequestBody PhotoAlbumSaveDTO dto) {
        return photographyService.update(dto);
    }

    @ApiOperation("上传摄影照片")
    @PostMapping("/upload/{id}")
    @OperationLog(moduleTitle = "上传摄影照片", businessType = BusinessType.IMPORT)
    public PhotoAlbumVO upload(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) {
        return photographyService.upload(id, file);
    }

    @ApiOperation("发布摄影相册")
    @PostMapping("/publish/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "发布摄影相册", businessType = BusinessType.UPDATE)
    public PhotoAlbumVO publish(@PathVariable("id") Long id, @Validated @RequestBody PhotoAlbumRevisionDTO dto) {
        return photographyService.publish(id, dto.getRevision());
    }

    @ApiOperation("下架摄影相册")
    @PostMapping("/unpublish/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "下架摄影相册", businessType = BusinessType.UPDATE)
    public PhotoAlbumVO unpublish(@PathVariable("id") Long id, @Validated @RequestBody PhotoAlbumRevisionDTO dto) {
        return photographyService.unpublish(id, dto.getRevision());
    }

    @ApiOperation("摄影相册草稿图片预览及原图下载")
    @GetMapping("/preview/{id}/{photoId}/{variant}")
    public void preview(@PathVariable("id") Long id, @PathVariable("photoId") Long photoId,
                        @PathVariable("variant") String variant, HttpServletResponse response) {
        photographyService.preview(id, photoId, variant, false, response);
    }
}
