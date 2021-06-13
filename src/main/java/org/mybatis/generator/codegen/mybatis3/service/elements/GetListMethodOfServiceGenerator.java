package org.mybatis.generator.codegen.mybatis3.service.elements;

import org.mybatis.generator.api.dom.java.*;

import java.util.Set;
import java.util.TreeSet;

/**
 * 获取list
 * @author laiyanlin
 */
public class GetListMethodOfServiceGenerator extends AbstractJavaServiceMethodGenerator {
	@Override
	public void addServiceElements(Interface interfaze) {
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.setAbstract(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(introspectedTable.getGetList());

        // 定义返回类型为List
        FullyQualifiedJavaType returnListType = new FullyQualifiedJavaType("java.util.List");
        FullyQualifiedJavaType returnType = introspectedTable.getRules()
                .calculateAllFieldsClass();
        returnListType.addTypeArgument(returnType);

        method.setReturnType(returnListType);
        method.addParameter(new Parameter(returnType, "req"));

        importedTypes.add(returnType);
        importedTypes.add(returnListType);

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable,"获取list");

        if (context.getPlugins().clientInsertSelectiveMethodGenerated(
                method, interfaze, introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
	}
}
