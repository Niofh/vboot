package com.carson.vboot.core.common.utils;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
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

    /**
     * Minio下载文件
     *
     * @param httpResponse
     * @param fileName
     * @param url
     * @param accessKey
     * @param secretKey
     * @param bucket
     * @return String
     */
    public static String downloadFile(HttpServletResponse httpResponse, String fileName, String url, String accessKey, String secretKey, String bucket) throws InvalidPortException, InvalidEndpointException {
        MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
        try (InputStream ism = new BufferedInputStream(minioClient.getObject(bucket, fileName))) {
            // 调用statObject()来判断对象是否存在。
            // 如果不存在, statObject()抛出异常,
            // 否则则代表对象存在
            minioClient.statObject(bucket, fileName);
            byte buf[] = new byte[1024];
            int length = 0;
            httpResponse.reset();
            //Content-disposition 是 MIME 协议的扩展，MIME 协议指示 MIME 用户代理如何显示附加的文件。
            // Content-disposition其实可以控制用户请求所得的内容存为一个文件的时候提供一个默认的文件名，
            // 文件直接在浏览器上显示或者在访问时弹出文件下载对话框。
            httpResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            httpResponse.setContentType("application/x-msdownload");
            httpResponse.setCharacterEncoding("utf-8");
            OutputStream osm = new BufferedOutputStream(httpResponse.getOutputStream());
            while ((length = ism.read(buf)) > 0) {
                osm.write(buf, 0, length);
            }
            osm.close();
            return "下载成功";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "下载失败";
        }
    }

}
