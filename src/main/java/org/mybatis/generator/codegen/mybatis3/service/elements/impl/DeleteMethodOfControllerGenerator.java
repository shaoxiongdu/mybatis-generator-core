package org.mybatis.generator.codegen.mybatis3.service.elements.impl;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 修改
 * @author laiyanlin
 */
public class DeleteMethodOfControllerGenerator extends AbstractJavaServiceImplMethodGenerator {
	@Override
	public void addServiceElements(TopLevelClass topLevelClass) {

        /*
         * 1. 获取基础类
         */
        FullyQualifiedJavaType modelType = new FullyQualifiedJavaType(
                introspectedTable.getBaseRecordType());

		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.addAnnotation("@PostMapping(\"/"+introspectedTable.getDelete()+"\")");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(introspectedTable.getDelete());
        // 定义返回类型为R
        FullyQualifiedJavaType returnListType = new FullyQualifiedJavaType("com.inc.admin.utils.R");
        FullyQualifiedJavaType returnType = introspectedTable.getRules()
                .calculateAllFieldsClass();
        method.setReturnType(returnListType);
        importedTypes.add(returnType);
        importedTypes.add(returnListType);
        importedTypes.add(new FullyQualifiedJavaType("javax.validation.constraints.NotNull"));
        importedTypes.add(new FullyQualifiedJavaType("org.springframework.validation.annotation.Validated"));
        //List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
        Parameter req = new Parameter(new FullyQualifiedJavaType("java.lang.Integer"), "id");
        req.addAnnotation("@Validated");
        req.addAnnotation("@NotNull(message = \"编号不能为空\")");
        req.addAnnotation("@RequestParam(\"id\")");
        req.addAnnotation("@RequestBody");
        method.addParameter(req);

        // 方法内容
        FullyQualifiedJavaType service = new FullyQualifiedJavaType(context.getJavaServiceGeneratorConfiguration().getTargetPackage()+"."+modelType.getShortName()+"Service");
		StringBuffer bodyLineSB = new StringBuffer();
		bodyLineSB.append("return R.operate(");
		bodyLineSB.append(service.getInjectName() + "." + introspectedTable.getDelete() + "(id)>0);");
		method.addBodyLine(bodyLineSB.toString());


        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable,"删除");

        if (context.getPlugins().clientInsertSelectiveMethodGenerated(
                method, topLevelClass, introspectedTable)) {
        	topLevelClass.addImportedTypes(importedTypes);
        	topLevelClass.addMethod(method);
        }

	}
}
