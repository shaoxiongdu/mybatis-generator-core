package org.mybatis.generator.codegen.mybatis3.service.elements.impl;

import org.mybatis.generator.api.dom.java.*;

import java.util.Set;
import java.util.TreeSet;

/**
 * service层,根据条件进行匹配查询
 * @author laiyanlin
 */
public class GetListMethodOfServiceImplGenerator extends AbstractJavaServiceImplMethodGenerator {
	@Override
	public void addServiceElements(TopLevelClass topLevelClass) {
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(introspectedTable.getGetList());

        // 定义返回类型为List
        FullyQualifiedJavaType returnListType = new FullyQualifiedJavaType("java.util.List");
        FullyQualifiedJavaType returnType = introspectedTable.getRules()
                .calculateAllFieldsClass();
        returnListType.addTypeArgument(returnType);

        method.setReturnType(returnListType);

        importedTypes.add(returnType);
        importedTypes.add(returnListType);

        method.addParameter(new Parameter(returnType, "req")); //$NON-NLS-1$

        // 方法内容
		FullyQualifiedJavaType mapper = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
		StringBuffer bodyLineSB = new StringBuffer();
		bodyLineSB.append("return ");
		bodyLineSB.append(mapper.getInjectName() + "." + introspectedTable.getSelectByExampleStatementId() + "(buildCriteria(req));");
		method.addBodyLine(bodyLineSB.toString());


        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable,"查询list");


        if (context.getPlugins().clientInsertSelectiveMethodGenerated(
                method, topLevelClass, introspectedTable)) {
        	topLevelClass.addImportedTypes(importedTypes);
        	topLevelClass.addMethod(method);
        }

	}
}
