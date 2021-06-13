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

import org.mybatis.generator.api.dom.java.*;

import java.util.Set;
import java.util.TreeSet;


/**
 * service实现 insert方法内容构造
 *
 * @author laiyanlin
 *
 */
public class InsertMethodOfServiceGenerator extends AbstractJavaServiceMethodGenerator {

    boolean isSimple;

    public InsertMethodOfServiceGenerator(boolean isSimple) {
        super();
        this.isSimple = isSimple;
    }


    /**
     * 方法内容
     *
     * @author laiyanlin
     *
     */
    @Override
    public void addServiceElements(Interface interfaze){
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
        method.setAbstract(true);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(introspectedTable.getInsert());
        method.addParameter(new Parameter(parameterType, "record")); //$NON-NLS-1$


        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable,"新增");


        if (context.getPlugins().clientInsertMethodGenerated(method, interfaze,
                introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }


    public void addMapperAnnotations(Interface interfaze, Method method) {
        return;
    }
}
