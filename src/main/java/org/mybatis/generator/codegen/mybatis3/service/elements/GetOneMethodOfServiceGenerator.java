package org.mybatis.generator.codegen.mybatis3.service.elements;

import org.mybatis.generator.api.dom.java.*;

import java.util.Set;
import java.util.TreeSet;

/**
 * 单个查询
 * @author laiyanlin
 */
public class GetOneMethodOfServiceGenerator extends AbstractJavaServiceMethodGenerator {
	@Override
	public void addServiceElements(Interface interfaze) {
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.setAbstract(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(introspectedTable.getGetOne());

        // 定义返回类型
        FullyQualifiedJavaType returnType = introspectedTable.getRules().calculateAllFieldsClass();

        method.setReturnType(returnType);
        method.addParameter(new Parameter(returnType, "req"));

        importedTypes.add(returnType);

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable,"单个查询");

        if (context.getPlugins().clientInsertSelectiveMethodGenerated(
                method, interfaze, introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
	}
}
