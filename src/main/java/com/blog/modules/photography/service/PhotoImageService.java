package com.blog.modules.photography.service;

import com.blog.modules.photography.domain.dto.PhotoImageResultDTO;

/**
 * 摄影照片处理接口
 *
 * @author xuesong.lei
 * @since 2026-10-19
 */
public interface PhotoImageService {

    /**
     * 校验JPEG、提取拍摄信息并生成校正方向后的展示图片
     *
     * @param bytes 原始照片数据
     * @return 照片元数据、缩略图及浏览图
     */
    PhotoImageResultDTO process(byte[] bytes);
}
