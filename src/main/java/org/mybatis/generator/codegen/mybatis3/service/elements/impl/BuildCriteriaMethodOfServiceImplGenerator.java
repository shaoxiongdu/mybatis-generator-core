package org.mybatis.generator.codegen.mybatis3.service.elements.impl;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Set;
import java.util.TreeSet;

/**
 * 构建查询条件
 * @author laiyanlin
 */
public class BuildCriteriaMethodOfServiceImplGenerator extends AbstractJavaServiceImplMethodGenerator {
	@Override
	public void addServiceElements(TopLevelClass topLevelClass) {
	    /*
	     * 1. 导入包
	     */
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        /*
         * 1.1 条件包
         */
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(
                introspectedTable.getExampleType());
        importedTypes.add(type);
        /*
         * 1.2 实体包
         */
        FullyQualifiedJavaType paramType = introspectedTable.getRules()
                .calculateAllFieldsClass();
        importedTypes.add(paramType);


        Method method = new Method(introspectedTable.getDeleteByExampleStatementId());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(type);
        method.addParameter(new Parameter(paramType, "req")); //$NON-NLS-1$


        method.setVisibility(JavaVisibility.PRIVATE);
        method.setName(introspectedTable.getBuildCriteria());



        // 方法内容
        String returnParam = StringUtility.getFirstLowerCaseWord(type.getShortName());
		StringBuffer bodyLineSB = new StringBuffer();
        /*
         * 第一行
         */
        bodyLineSB.append(type.getShortName() + " " + returnParam +
                "= new " + type.getShortName() + "();");
        method.addBodyLine(bodyLineSB.toString());
        /*
         * 第二行
         */
        bodyLineSB = new StringBuffer();
        bodyLineSB.append(type.getShortName()+".Criteria criteria = " + returnParam + ".createCriteria();");
        method.addBodyLine(bodyLineSB.toString());

        /*
         * 第三行
         */
        bodyLineSB = new StringBuffer();
        bodyLineSB.append("if (req != null) {");
        method.addBodyLine(bodyLineSB.toString());

        //主键查询条件
        IntrospectedColumn pk = introspectedTable.getPrimaryKeyColumns().get(0);
        String getterName = JavaBeansUtil.getGetterMethodName(pk.getJavaProperty(), pk.getFullyQualifiedJavaType());
        if ("String".equals(pk.getFullyQualifiedJavaType().getShortName())) {
            importedTypes.add(new FullyQualifiedJavaType("org.apache.commons.lang3.StringUtils"));
            bodyLineSB = new StringBuffer();
            bodyLineSB.append("if (StringUtils.isNotBlank(req."+ getterName +"())) {");
            StringBuilder sb = new StringBuilder();
            sb.append(pk.getJavaProperty());
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            bodyLineSB.append("criteria.and"+sb+"EqualTo(req."+getterName+"());");
            bodyLineSB.append("}");
            method.addBodyLine(bodyLineSB.toString());
        } else {
            bodyLineSB = new StringBuffer();
            bodyLineSB.append("if (req."+ getterName +"() != null) {");
            StringBuilder sb = new StringBuilder();
            sb.append(pk.getJavaProperty());
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            bodyLineSB.append("criteria.and"+sb+"EqualTo(req."+getterName+"());");
            bodyLineSB.append("}");
            method.addBodyLine(bodyLineSB.toString());
        }
        //生成查询条件
        for (IntrospectedColumn baseColumn : introspectedTable.getBaseColumns()) {
            getterName = JavaBeansUtil.getGetterMethodName(baseColumn.getJavaProperty(), baseColumn.getFullyQualifiedJavaType());
            if ("String".equals(baseColumn.getFullyQualifiedJavaType().getShortName())) {
                importedTypes.add(new FullyQualifiedJavaType("org.apache.commons.lang3.StringUtils"));
                bodyLineSB = new StringBuffer();
                bodyLineSB.append("if (StringUtils.isNotBlank(req."+ getterName +"())) {");
                StringBuilder sb = new StringBuilder();
                sb.append(baseColumn.getJavaProperty());
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
                bodyLineSB.append("criteria.and"+sb+"EqualTo(req."+getterName+"());");
                bodyLineSB.append("}");
                method.addBodyLine(bodyLineSB.toString());
            } else {
                bodyLineSB = new StringBuffer();
                bodyLineSB.append("if (req."+ getterName +"() != null) {");
                StringBuilder sb = new StringBuilder();
                sb.append(baseColumn.getJavaProperty());
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
                bodyLineSB.append("criteria.and"+sb+"EqualTo(req."+getterName+"());");
                bodyLineSB.append("}");
                method.addBodyLine(bodyLineSB.toString());
            }
        }
        /*
         * 第四行
         */
        bodyLineSB = new StringBuffer();
        bodyLineSB.append("");
        method.addBodyLine(bodyLineSB.toString());

        /*
         * 第五行
         */
        bodyLineSB = new StringBuffer();
        bodyLineSB.append("}");
        method.addBodyLine(bodyLineSB.toString());

        bodyLineSB = new StringBuffer();
		bodyLineSB.append("return " + returnParam + ";");
		method.addBodyLine(bodyLineSB.toString());


        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable,"构建查询条件");


        if (context.getPlugins().clientInsertSelectiveMethodGenerated(
                method, topLevelClass, introspectedTable)) {
        	topLevelClass.addImportedTypes(importedTypes);
        	topLevelClass.addMethod(method);
        }

	}
}
