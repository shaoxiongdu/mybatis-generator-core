package org.mybatis.generator.codegen.mybatis3.service.elements.impl;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.mybatis.generator.internal.util.StringUtility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * 构建查询条件
 * @author laiyanlin
 */
public class VueGenerator extends AbstractJavaServiceImplMethodGenerator {
	@Override
	public void addServiceElements(TopLevelClass topLevelClass) {
	    //基础类
        FullyQualifiedJavaType modelType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        Map<String, Object> map = new HashMap<>(16);
        map.put("modelName", StringUtility.getFirstLowerCaseWord(modelType.getShortName()));
        map.put("apiFile", modelType.getShortName());
        map.put("comment", introspectedTable.getFullyQualifiedTable().getRemark());
        map.put("pk", introspectedTable.getPrimaryKeyColumns().get(0).getJavaProperty());
        map.put("baseColumns", introspectedTable.getBaseColumns());
        VelocityContext context = new VelocityContext(map);
        Properties properties=new Properties();
        //设置velocity资源加载方式为class
        properties.setProperty("resource.loader", "class");
        //设置velocity资源加载方式为file时的处理类
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        VelocityEngine ve = new VelocityEngine(properties);
        //设置参数
        //  ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, templatepath);
        //处理中文问题
        ve.setProperty(Velocity.INPUT_ENCODING,"utf-8");
        ve.setProperty(Velocity.OUTPUT_ENCODING,"utf-8");
        //初始化
        ve.init();
        // 获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates)
        {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = ve.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            try
            {
                // 添加到zip
                String basePath = "src/generate/resources/js/";
                String path = basePath + "components/" + StringUtility.getFirstLowerCaseWord(modelType.getShortName())+"/";
                String fileName = "list.vue";
                if (template.contains("api")) {
                    path = basePath + "api/";
                    fileName = "api"+modelType.getShortName()+".js";
                } else if (template.contains("addForm")){
                    fileName = "addForm.vue";
                    path = path+"components/";
                }
                File file = new File(path);
                if (!file.exists())
                {
                    file.mkdirs();
                }
                FileOutputStream outFile = new FileOutputStream(path+fileName);
                IOUtils.write(sw.toString(), outFile, "UTF-8");
                IOUtils.closeQuietly(sw);
                outFile.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
	}

    public static List<String> getTemplates()
    {
        List<String> templates = new ArrayList<String>();
        templates.add("templates/api.js.vm");
        templates.add("templates/list.vue.vm");
        templates.add("templates/addForm.vue.vm");
        return templates;
    }
}
