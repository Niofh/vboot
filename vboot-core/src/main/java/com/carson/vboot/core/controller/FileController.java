package com.carson.vboot.core.controller;

import cn.hutool.core.util.StrUtil;
import com.carson.vboot.core.common.utils.FileUtil;
import com.carson.vboot.core.common.utils.MinioUtils;
import com.carson.vboot.core.common.utils.ResultUtil;
import com.carson.vboot.core.vo.FileVo;
import com.carson.vboot.core.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * created by Nicofh on 2020-06-16
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {
    @Value("${minio.url}")
    private String url;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucket}")
    private String bucket;


    //上传文件到minio服务器
    @PostMapping("/upload")
    public Result<Object> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = FileUtil.fileRename(file.getOriginalFilename());
        String fileUrl = MinioUtils.uploadFile(file, fileName, url, accessKey, secretKey, bucket);
        if (StrUtil.isNotBlank(fileUrl)) {
            FileVo fileVo = new FileVo();
            fileVo.setUrl(fileUrl);
            return ResultUtil.data(fileVo);
        } else {
            return ResultUtil.error("上传失败");
        }

    }

    /**
     * 上传多个文件
     *
     * @param files
     * @return
     */
    @PostMapping("/uploadFiles")
    public Result<List<FileVo>> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        List<FileVo> list = new ArrayList<>();//存储生成的访问路径
        for (MultipartFile file : files) {
            log.info(file.getOriginalFilename());
            String fileName = FileUtil.fileRename(file.getOriginalFilename());
            String fileUrl = MinioUtils.uploadFile(file, fileName, url, accessKey, secretKey, bucket);

            if (StrUtil.isNotBlank(fileUrl)) {
                // 添加文件地址
                FileVo fileVo = new FileVo();
                fileVo.setUrl(fileUrl);
                list.add(fileVo);
            }
        }

        return ResultUtil.data(list);
    }

}
