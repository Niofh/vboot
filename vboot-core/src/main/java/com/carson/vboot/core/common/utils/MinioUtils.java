package com.carson.vboot.core.common.utils;

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * created by Nicofh on 2020-06-16
 * minio工具类 版本6.11
 * https://blog.csdn.net/CloudsYy/article/details/105235890
 */
@Slf4j
public class MinioUtils {


    /**
     * 单个文件上传
     *
     * @param file
     * @return 返回文件地址
     */
    public static String uploadFile(MultipartFile file, String fileName, String url, String accessKey, String secretKey, String bucket) {
        try {
            MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
            minioClient.putObject(bucket, fileName, file.getInputStream(), file.getSize(), null, null, file.getContentType());
            return bucket + "/" + fileName;
        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException | XmlPullParserException e) {
            log.error("上传失败 {}", e);
            return "";
        }
    }
}
