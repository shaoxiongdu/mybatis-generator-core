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
 * 更新
 *
 * @author laiyanlin
 *
 */
public class UpdateByPrimaryKeySelectiveMethodOfServiceImplGenerator extends AbstractJavaServiceImplMethodGenerator {

	private boolean isSimple;

	public UpdateByPrimaryKeySelectiveMethodOfServiceImplGenerator(boolean isSimple) {
		super();
		this.isSimple = isSimple;
	}

	@Override
	public void addServiceElements(TopLevelClass topLevelClass) {
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();

		FullyQualifiedJavaType mapper = new FullyQualifiedJavaType(
				introspectedTable.getMyBatis3JavaMapperType());
		FullyQualifiedJavaType parameterType;
		if (isSimple) {
			parameterType = new FullyQualifiedJavaType(
					introspectedTable.getBaseRecordType());
		} else {
			parameterType = introspectedTable.getRules()
					.calculateAllFieldsClass();
		}

		importedTypes.add(parameterType);

		Method method = new Method();
		method.addAnnotation("@Override");
		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName(introspectedTable.getUpdate());
		method.addParameter(new Parameter(parameterType, "record")); //$NON-NLS-1$
		StringBuffer sb = new StringBuffer();
		sb.append("return ");
		sb.append(mapper.getInjectName()+"."+introspectedTable.getUpdateByPrimaryKeySelectiveStatementId()+"(record);");
		method.addBodyLine(sb.toString());

		context.getCommentGenerator().addGeneralMethodComment(method,
				introspectedTable,"根据主键更新不为空的值");


		if (context.getPlugins().clientUpdateByPrimaryKeySelectiveMethodGenerated(method, topLevelClass,
				introspectedTable)) {
			topLevelClass.addImportedTypes(importedTypes);
			topLevelClass.addMethod(method);
		}
	}

}
