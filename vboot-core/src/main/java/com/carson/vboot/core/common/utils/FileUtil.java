package com.carson.vboot.core.common.utils;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 文件操作类
 */
public class FileUtil {
    /**
     * 文件重命名
     *
     * @param fileName 文件名称
     * @return
     */
    public static String fileRename(String fileName) {
        if (StrUtil.isBlank(fileName)) {
            return IdUtil.objectId() + "";
        }
        int len = fileName.lastIndexOf(".");
        String suffixName = fileName.substring(len);  // 后缀名
        String preName = fileName.substring(0, len);  // 前缀名
        return IdUtil.objectId() + preName + suffixName; // 新文件名;
    }

    /**
     * 下载
     *
     * @param response
     * @param filename
     * @param filepath
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String downLoad(HttpServletResponse response, String filename, String filepath) throws UnsupportedEncodingException {
        // 如果文件名不为空，则进行下载
        if (filename != null) {
            File file = new File(filepath);
            // 如果文件存在，则进行下载
            if (file.exists()) {
                // 配置文件下载
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                // 下载文件能正常显示中文
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
                // 实现文件下载
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("Download  successfully!");
                    return "successfully";

                } catch (Exception e) {
                    System.out.println("Download  failed!");
                    return "failed";

                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return "";
    }
}
