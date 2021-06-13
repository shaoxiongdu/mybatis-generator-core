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
package org.mybatis.generator.codegen.mybatis3.service.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * service实现 删除方法构造
 *
 * @author laiyanlin
 */
public class DeleteByPrimaryKeyMethodOfServiceGenerator extends AbstractJavaServiceMethodGenerator {

	private boolean isSimple;

	public DeleteByPrimaryKeyMethodOfServiceGenerator(boolean isSimple) {
		super();
		this.isSimple = isSimple;
	}

	@Override
	public void addServiceElements(Interface interfaze) {
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setAbstract(true);
		method.setName(introspectedTable.getDelete());
		method.setReturnType(FullyQualifiedJavaType.getIntInstance());


		List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();
		for (IntrospectedColumn introspectedColumn : introspectedColumns) {
			FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
			importedTypes.add(type);
			Parameter parameter = new Parameter(type, introspectedColumn.getJavaProperty());
			method.addParameter(parameter);
		}

		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable, "根据主键删除该记录");

		if (context.getPlugins().clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
			interfaze.addImportedTypes(importedTypes);
			interfaze.addMethod(method);
		}
	}

}
