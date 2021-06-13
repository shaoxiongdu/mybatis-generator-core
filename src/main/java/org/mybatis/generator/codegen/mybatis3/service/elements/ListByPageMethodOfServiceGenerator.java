package org.mybatis.generator.codegen.mybatis3.service.elements;

import org.mybatis.generator.api.dom.java.*;

import java.util.Set;
import java.util.TreeSet;

/**
 * 分页查询方法生成类
 * @author laiyanlin
 */
public class ListByPageMethodOfServiceGenerator extends AbstractJavaServiceMethodGenerator {
	@Override
	public void addServiceElements(Interface interfaze) {
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
		/*
		 * 新增方法
		 */
        Method method = new Method();
        method.setAbstract(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(introspectedTable.getListByPage());
        /*
         * 设置返回类型
         */
        FullyQualifiedJavaType returnListType = new FullyQualifiedJavaType("com.github.pagehelper.PageInfo");
        FullyQualifiedJavaType returnType = introspectedTable.getRules()
                .calculateAllFieldsClass();
        returnListType.addTypeArgument(returnType);

        method.setReturnType(returnListType);
        /*
         * 设置入参
         */
        method.addParameter(new Parameter(returnType, "req"));
        /*
         * 添加导入的包路径
         */
        importedTypes.add(returnType);
        importedTypes.add(returnListType);
        /*
         * 设置方法注解
         */
        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable,"分页查询");

        if (context.getPlugins().clientInsertSelectiveMethodGenerated(
                method, interfaze, introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
	}
}
