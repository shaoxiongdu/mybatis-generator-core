package org.mybatis.generator.codegen.mybatis3.service.elements.impl;

import org.mybatis.generator.api.dom.java.*;

import java.util.Set;
import java.util.TreeSet;

/**
 * 分页查询
 * @author laiyanlin
 */
public class ListByPageMethodOfServiceImplGenerator extends AbstractJavaServiceImplMethodGenerator {
	@Override
	public void addServiceElements(TopLevelClass topLevelClass) {
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(introspectedTable.getListByPage());
        // 定义返回类型为List
        FullyQualifiedJavaType returnListType = new FullyQualifiedJavaType("com.github.pagehelper.PageInfo");
        FullyQualifiedJavaType returnType = introspectedTable.getRules()
                .calculateAllFieldsClass();
        returnListType.addTypeArgument(returnType);

        method.setReturnType(returnListType);

        importedTypes.add(returnType);
        importedTypes.add(returnListType);
        importedTypes.add(new FullyQualifiedJavaType("com.github.pagehelper.PageHelper"));

        method.addParameter(new Parameter(returnType, "req"));

        // 方法内容
		FullyQualifiedJavaType mapper = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
		StringBuffer bodyLineSB = new StringBuffer();

        bodyLineSB.append("PageHelper.startPage(req.getPageNo(), req.getPageSize());");
        method.addBodyLine(bodyLineSB.toString());

        bodyLineSB = new StringBuffer();
		bodyLineSB.append("return ");
		bodyLineSB.append("new PageInfo<>(" + mapper.getInjectName() + "." + introspectedTable.getSelectByExampleStatementId() + "(buildCriteria(req)));");
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
