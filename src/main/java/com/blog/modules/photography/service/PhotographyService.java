package com.blog.modules.photography.service;

import com.blog.common.domain.vo.PageVO;
import com.blog.modules.photography.domain.dto.PhotoAlbumContentDTO;
import com.blog.modules.photography.domain.dto.PhotoAlbumQueryDTO;
import com.blog.modules.photography.domain.dto.PhotoAlbumSaveDTO;
import com.blog.modules.photography.domain.dto.PhotoQueryDTO;
import com.blog.modules.photography.domain.vo.PhotoAlbumVO;
import com.blog.modules.photography.domain.vo.PhotoVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 摄影相册业务接口
 *
 * @author xuesong.lei
 * @since 2026-10-19
 */
public interface PhotographyService {

    /**
     * 分页查询后台相册
     *
     * @param dto 查询参数
     * @return 相册分页列表
     */
    PageVO<PhotoAlbumVO> pageList(PhotoAlbumQueryDTO dto);

    /**
     * 查询相册草稿及照片列表
     *
     * @param id 相册ID
     * @return 相册草稿详情
     */
    PhotoAlbumVO detail(Long id);

    /**
     * 新增相册
     *
     * @param dto 相册内容
     * @return 创建后的相册草稿
     */
    PhotoAlbumVO add(PhotoAlbumContentDTO dto);

    /**
     * 保存相册草稿
     *
     * @param dto 保存参数
     * @return 最新相册草稿
     */
    PhotoAlbumVO update(PhotoAlbumSaveDTO dto);

    /**
     * 上传照片，完成处理后加入相册草稿
     *
     * @param id 相册ID
     * @param file JPEG照片
     * @return 最新相册草稿
     */
    PhotoAlbumVO upload(Long id, MultipartFile file);

    /**
     * 发布当前草稿为新的公开快照
     *
     * @param id 相册ID
     * @param revision 当前修订版本
     * @return 发布后的相册信息
     */
    PhotoAlbumVO publish(Long id, Integer revision);

    /**
     * 下架相册
     *
     * @param id 相册ID
     * @param revision 当前修订版本
     * @return 下架后的相册信息
     */
    PhotoAlbumVO unpublish(Long id, Integer revision);

    /**
     * 分页查询已发布相册
     *
     * @param dto 查询参数
     * @return 已发布相册分页列表
     */
    PageVO<PhotoAlbumVO> publishedPageList(PhotoAlbumQueryDTO dto);

    /**
     * 查询已发布的相册详情
     *
     * @param id 相册ID
     * @return 已发布的相册信息
     */
    PhotoAlbumVO publishedDetail(Long id);

    /**
     * 按发布顺序分页查询照片
     *
     * @param id 相册ID
     * @param dto 查询参数
     * @return 已发布照片分页列表
     */
    PageVO<PhotoVO> publishedPhotos(Long id, PhotoQueryDTO dto);

    /**
     * 校验照片访问权限并输出图片，原图以附件形式下载
     *
     * @param id 相册ID
     * @param photoId 照片ID
     * @param variant 图片版本：thumbnail、display、original
     * @param published 是否读取已发布快照
     * @param response HTTP响应
     */
    void preview(Long id, Long photoId, String variant, boolean published, HttpServletResponse response);
}
