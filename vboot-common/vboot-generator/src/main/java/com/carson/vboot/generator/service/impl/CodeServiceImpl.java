package com.carson.vboot.generator.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.common.enums.ExceptionEnums;
import com.carson.vboot.core.exception.VbootException;
import com.carson.vboot.generator.common.Constant;
import com.carson.vboot.generator.common.enums.FormEnum;
import com.carson.vboot.generator.common.enums.SqlEnum;
import com.carson.vboot.generator.common.utils.StringTool;
import com.carson.vboot.generator.dao.mapper.CodeDao;
import com.carson.vboot.generator.dao.mapper.CodeDetailDao;
import com.carson.vboot.generator.entity.Code;
import com.carson.vboot.generator.entity.CodeDetail;
import com.carson.vboot.generator.service.CodeService;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CodeServiceImpl implements CodeService {
    @Autowired
    private CodeDao codeDao;

    @Autowired
    private CodeDetailDao codeDetailDao;

    @Value("${code.winPath}")
    private String winPath;

    @Value("${code.linuxPath}")
    private String linuxPath;

    @Override
    public VbootBaseDao<Code> getBaseDao() {
        return codeDao;
    }

    @Override
    public IPage<Code> getCodeByPage(PageBo pageBo, Code code) {

        Page<Code> page = new Page<>(pageBo.getPageIndex(), pageBo.getPageSize());
        QueryWrapper<Code> codeQueryWrapper = new QueryWrapper<>();

        if (StrUtil.isNotBlank(code.getTableName())) {

            codeQueryWrapper.like("table_name", code.getTableName());
        }

        // 根据时间倒序
        codeQueryWrapper.orderByDesc(true, "create_time");
        Page<Code> codePage = codeDao.selectPage(page, codeQueryWrapper);

        return codePage;
    }

    @Override
    public String fileDownLoad(String id) {
        // 获取code信息
        Code code = codeDao.selectById(id);
        if (code == null) {
            throw new VbootException(ExceptionEnums.DATA_NO_EXIST);
        }
        // code
        QueryWrapper<CodeDetail> codeDetailQueryWrapper = new QueryWrapper<>();
        codeDetailQueryWrapper.eq("code_id", id);
        List<CodeDetail> codeDetailList = codeDetailDao.selectList(codeDetailQueryWrapper);

        if (CollUtil.isEmpty(codeDetailList)) {

            throw new VbootException(ExceptionEnums.CODE_DETAIL_NO_EXIST);
        }

        HashMap<String, Object> stringObjectHashMap = renderFiled(code, codeDetailList, true);
        log.info("【文件地址】：{}", (String) stringObjectHashMap.get("path"));
        return (String) stringObjectHashMap.get("path");
    }

    /**
     * 生成代码返回前端
     *
     * @param id
     * @return
     */
    @Override
    public HashMap<String, Object> showCode(String id) {
        // 获取code信息
        Code code = codeDao.selectById(id);
        if (code == null) {
            throw new VbootException(ExceptionEnums.DATA_NO_EXIST);
        }
        // code
        QueryWrapper<CodeDetail> codeDetailQueryWrapper = new QueryWrapper<>();
        codeDetailQueryWrapper.eq("code_id", id);
        List<CodeDetail> codeDetailList = codeDetailDao.selectList(codeDetailQueryWrapper);

        if (CollUtil.isEmpty(codeDetailList)) {

            throw new VbootException(ExceptionEnums.CODE_DETAIL_NO_EXIST);
        }

        HashMap<String, Object> map = this.renderFiled(code, codeDetailList, false);
        return map;
    }

    /**
     * 生成文件
     *
     * @param code
     * @param codeDetailList
     * @param createFile
     * @return
     */
    private HashMap<String, Object> renderFiled(Code code, List<CodeDetail> codeDetailList, Boolean createFile) {

        StringTool stringTool = new StringTool();
        String name = stringTool.lineToHump(code.getName()); // 下划线转驼峰
        String Name = name.substring(0, 1).toUpperCase() + name.substring(1);

        String FROM = Constant.FROM_PATH;
        String TARGET = Constant.TARGET_PATH + "/" + name;

        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("/");
        Configuration cfg = null;
        try {
            cfg = Configuration.defaultConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);


        // 设置共享变量
        Map<String, Object> shared = new HashMap<String, Object>();
        shared.put("name", name);
        shared.put("Name", Name);
        shared.put("code", code);
        shared.put("codeDetailList", codeDetailList);
        shared.put("formEnum", FormEnum.getObject());
        shared.put("sqlEnum", SqlEnum.getObject());
        gt.setSharedVars(shared);

        HashMap<String, Object> result = new HashMap<>();

        // api接口生成
        String api = this.commonFile(gt, FROM + "/vue/api.txt", TARGET + "/vue/" + name + ".js", createFile);
        String table = this.commonFile(gt, FROM + "/vue/table.txt", TARGET + "/vue/" + name + ".vue", createFile);
        String vuexDict = this.commonFile(gt, FROM + "/vue/dict.txt", TARGET + "/vue/" + name + "Dict.js", createFile);


        // 固远特殊版本
        String apiGuYuan = this.commonFile(gt, FROM + "/vue/apiGuYuan.txt", TARGET + "/vue/guyuan" + name + ".js", createFile);
        String tableGuYuan = this.commonFile(gt, FROM + "/vue/tableGuYuan.txt", TARGET + "/vue/guyuan" + name + ".vue", createFile);


        // mysql生成
        String mysql = this.commonFile(gt, FROM + "/mysql/sql.txt", TARGET + "/mysql/" + code.getTableName() + ".sql", createFile);

        // entity生成
        String entity = this.commonFile(gt, FROM + "/java/entity/entity.txt", TARGET + "/java/entity/" + Name + ".java", createFile);
        // mapper生成
        String mapper = this.commonFile(gt, FROM + "/java/dao/mapper/mapper.txt", TARGET + "/java/dao/mapper/" + Name + "Dao.java", createFile);

        // service
        String service = this.commonFile(gt, FROM + "/java/service/service.txt", TARGET + "/java/service/" + Name + "Service.java", createFile);
        String serviceImpl = this.commonFile(gt, FROM + "/java/service/impl/serviceImpl.txt", TARGET + "/java/service/impl/" + Name + "ServiceImpl.java", createFile);

        // controller
        String controller = this.commonFile(gt, FROM + "/java/controller/controller.txt", TARGET + "/java/controller/" + Name + "Controller.java", createFile);


        // 判断环境
        String osName = System.getProperties().getProperty("os.name");
        String path = "";
        if ("Linux".equals(osName)) {
            path = new File(linuxPath, TARGET).getAbsolutePath();
        } else {
            path = new File(winPath, TARGET).getAbsolutePath();
        }
        result.put("path", path); // 文件路径
        result.put("api", api);
        result.put("vuexDict", vuexDict);
        result.put("table", table);
        result.put("mysql", mysql);
        result.put("entity", entity);
        result.put("mapper", mapper);
        result.put("service", service);
        result.put("serviceImpl", serviceImpl);
        result.put("controller", controller);
        result.put("apiGuYuan", apiGuYuan);
        result.put("tableGuYuan", tableGuYuan);
        return result;
    }


    /**
     * @param gt         GroupTemplate
     * @param from       ；来源路径
     * @param target     目标路径
     * @param createFile 是否创建文件
     */
    private String commonFile(GroupTemplate gt, String from, String target, Boolean createFile) {
        Template t = gt.getTemplate(from);
        // 模板渲染
        String data = t.render();
        String path = "";
        if (createFile) {
            String osName = System.getProperties().getProperty("os.name");

            if ("Linux".equals(osName)) {
                path = new File(linuxPath, target).getAbsolutePath();
            } else {
                path = new File(winPath, target).getAbsolutePath().replaceAll("\\\\","/");
            }
            File file = new File(path);
            // 先创建目录,不然linux没有目录直接会失败
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                try {
                    // 创建文件名称
                    file.createNewFile();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 文件写入
            FileUtil.writeBytes(data.getBytes(), file.getPath());
        }

        return data;
    }

}
