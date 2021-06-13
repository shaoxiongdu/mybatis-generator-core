package org.mybatis.generator.codegen.mybatis3.service.elements.impl;

import org.mybatis.generator.api.dom.java.*;

import java.util.Set;
import java.util.TreeSet;

/**
 * 单个查询
 * @author laiyanlin
 */
public class GetOneMethodOfServiceImplGenerator extends AbstractJavaServiceImplMethodGenerator {
	@Override
	public void addServiceElements(TopLevelClass topLevelClass) {
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.addAnnotation("@Override");
        // 定义返回类型为List
        FullyQualifiedJavaType returnListType = new FullyQualifiedJavaType("java.util.List");
        FullyQualifiedJavaType returnType = introspectedTable.getRules()
                .calculateAllFieldsClass();
        returnListType.addTypeArgument(returnType);

        method.setReturnType(returnType);

        importedTypes.add(returnType);
        importedTypes.add(returnListType);
        importedTypes.add(new FullyQualifiedJavaType("org.apache.commons.collections4.CollectionUtils"));


        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(introspectedTable.getGetOne());

        method.addParameter(new Parameter(returnType, "req")); //$NON-NLS-1$

        // 方法内容
		StringBuffer bodyLineSB = new StringBuffer();

        bodyLineSB.append(returnListType.getShortName() + " list = getList(req);");
        method.addBodyLine(bodyLineSB.toString());
        bodyLineSB = new StringBuffer();
        bodyLineSB.append("if(CollectionUtils.isNotEmpty(list)) {");
        bodyLineSB.append("return list.get(0);");
        bodyLineSB.append("}");
        method.addBodyLine(bodyLineSB.toString());

        bodyLineSB = new StringBuffer();
		bodyLineSB.append("return null;");
        method.addBodyLine(bodyLineSB.toString());

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable,"单个查询");


        if (context.getPlugins().clientInsertSelectiveMethodGenerated(
                method, topLevelClass, introspectedTable)) {
        	topLevelClass.addImportedTypes(importedTypes);
        	topLevelClass.addMethod(method);
        }

	}
}
