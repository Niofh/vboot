package com.carson.vboot.generator;

import cn.hutool.core.io.FileUtil;
import com.carson.vboot.core.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/temp/vue")
@Api(tags = "vue生成增删改查模板")
public class VueGenerator {

    @ApiOperation(value = "生成模板")
    @GetMapping("/render")
    public Result<Object> render() {
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("/");
        Configuration cfg = null;
        try {
            cfg = Configuration.defaultConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
        Template t = gt.getTemplate("/hello.txt");
        String str = t.render();
        System.out.println(str);
        return null;
    }

    public static void main(String[] args) {
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("/");
        Configuration cfg = null;
        try {
            cfg = Configuration.defaultConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);

        Template t = gt.getTemplate("/temp/vue/api.txt");

        String name = "code";

        t.binding("name", "code");
        t.binding("Name","Code");
        String str = t.render();

//        String property = System.getProperty("user.dir"); 获取项目路径
//        System.out.println(property);

       // spingboot文件下载 https://blog.csdn.net/zhangvalue/article/details/89387261

        // 获取当前class资源路径
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();


        System.out.println(path);
        File file = new File(path+ "/temp/vue/code.js");
        // 文件写入
        FileUtil.writeBytes(str.getBytes(), file.getPath());
        System.out.println(str);
    }
}
