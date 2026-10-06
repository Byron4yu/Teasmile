package com.teasmile.controller.admin;

import com.teasmile.constant.MessageConstant;
import com.teasmile.result.ApiResult;
import com.teasmile.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @version 1.0
 * @Author Imak
 * @Date 2024/8/7 15:05
 * @通用管理
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public ApiResult<String> upload(MultipartFile file){
        log.info("文件上传，{}",file);

        try {
            String originalFilename = file.getOriginalFilename();//原始文件名
            String extension=originalFilename.substring(originalFilename.lastIndexOf("."));//后缀，文件格式
            String objectName=UUID.randomUUID().toString()+extension;

            String filePath=aliOssUtil.upload(file.getBytes(),objectName);
            return ApiResult.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败：{}",e);
        }
        return ApiResult.error(MessageConstant.UPLOAD_FAILED);
    }
}
