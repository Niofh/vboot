package com.carson.vboot.generator;

import com.carson.vboot.core.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/vue")
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
        Template t = gt.getTemplate("/hello.txt");
        String str = t.render();
        System.out.println(str);
    }
}
