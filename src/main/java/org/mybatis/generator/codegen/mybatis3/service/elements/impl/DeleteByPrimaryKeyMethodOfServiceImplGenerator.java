/*
 *  Copyright 2009 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mybatis.generator.codegen.mybatis3.service.elements.impl;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.service.elements.AbstractJavaServiceMethodGenerator;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * service实现 删除方法构造
 *
 * @author laiyanlin
 */
public class DeleteByPrimaryKeyMethodOfServiceImplGenerator extends AbstractJavaServiceImplMethodGenerator {

	private boolean isSimple;

	public DeleteByPrimaryKeyMethodOfServiceImplGenerator(boolean isSimple) {
		super();
		this.isSimple = isSimple;
	}

	@Override
	public void addServiceElements(TopLevelClass topLevelClass) {
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
		Method method = new Method();
		method.addAnnotation("@Override");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		method.setName(introspectedTable.getDelete());


		List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();
		for (IntrospectedColumn introspectedColumn : introspectedColumns) {
			FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
			importedTypes.add(type);
			Parameter parameter = new Parameter(type, introspectedColumn.getJavaProperty());
			method.addParameter(parameter);
			// 方法内容
			FullyQualifiedJavaType mapper = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
			StringBuffer bodyLineSB = new StringBuffer();
			bodyLineSB.append("return ");
			bodyLineSB.append(mapper.getInjectName() + "." + introspectedTable.getDeleteByPrimaryKeyStatementId() + "("+introspectedColumn.getJavaProperty()+");");
			method.addBodyLine(bodyLineSB.toString());
		}

		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable, "根据主键删除该记录");

		if (context.getPlugins().clientDeleteByPrimaryKeyMethodGenerated(method, topLevelClass, introspectedTable)) {
			topLevelClass.addImportedTypes(importedTypes);
			topLevelClass.addMethod(method);
		}
	}

}
