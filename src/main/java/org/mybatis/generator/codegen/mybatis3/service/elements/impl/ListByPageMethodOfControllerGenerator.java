package org.mybatis.generator.codegen.mybatis3.service.elements.impl;

import org.mybatis.generator.api.dom.java.*;

import java.util.Set;
import java.util.TreeSet;

/**
 * 分页查询
 * @author laiyanlin
 */
public class ListByPageMethodOfControllerGenerator extends AbstractJavaServiceImplMethodGenerator {
	@Override
	public void addServiceElements(TopLevelClass topLevelClass) {

        /*
         * 1. 获取基础类
         */
        FullyQualifiedJavaType modelType = new FullyQualifiedJavaType(
                introspectedTable.getBaseRecordType());

		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.addAnnotation("@PostMapping(\"/"+introspectedTable.getListByPage()+"\")");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(introspectedTable.getListByPage());
        // 定义返回类型为R
        FullyQualifiedJavaType returnListType = new FullyQualifiedJavaType("com.inc.admin.utils.R");
        FullyQualifiedJavaType returnType = introspectedTable.getRules()
                .calculateAllFieldsClass();
        method.setReturnType(returnListType);
        importedTypes.add(returnType);
        importedTypes.add(returnListType);
        Parameter req = new Parameter(returnType, "req");
        req.addAnnotation("@RequestBody");
        method.addParameter(req);

        // 方法内容
        FullyQualifiedJavaType service = new FullyQualifiedJavaType(context.getJavaServiceGeneratorConfiguration().getTargetPackage()+"."+modelType.getShortName()+"Service");
		StringBuffer bodyLineSB = new StringBuffer();
		bodyLineSB.append("return R.ok().put(\"page\", ");
		bodyLineSB.append(service.getInjectName() + "." + introspectedTable.getListByPage() + "(req));");
		method.addBodyLine(bodyLineSB.toString());


        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable,"分页查询");

        if (context.getPlugins().clientInsertSelectiveMethodGenerated(
                method, topLevelClass, introspectedTable)) {
        	topLevelClass.addImportedTypes(importedTypes);
        	topLevelClass.addMethod(method);
        }

	}
}
