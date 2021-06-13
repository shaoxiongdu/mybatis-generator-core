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

package org.mybatis.generator.codegen.mybatis3.service;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.service.elements.*;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.messages.Messages.getString;


/**
 * service构造类
 *
 * @author laiyanlin
 *
 */
public class JavaInnerServiceGenerator extends AbstractJavaClientGenerator {

    /**
     *
     */
    public JavaInnerServiceGenerator(String project) {
        super(project, true);
    }

    public JavaInnerServiceGenerator(String project, boolean requiresMatchedXMLGenerator) {
        super(project, requiresMatchedXMLGenerator);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        progressCallback.startTask(getString("Progress.17", //$NON-NLS-1$
                introspectedTable.getFullyQualifiedTable().toString()));

        /*
         * 1.获取基础类
         */
        FullyQualifiedJavaType modelType = new FullyQualifiedJavaType(
                introspectedTable.getBaseRecordType());

        /*
         * 2. 定义接口及接口名称
         */
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(
                context.getJavaServiceGeneratorConfiguration().getTargetPackage()+"."+modelType.getShortName()+"Service");
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);

        /*
         * 3. 类加注释
         */
        CommentGenerator commentGenerator = context.getCommentGenerator();
        commentGenerator.addClassComment(interfaze, introspectedTable,"Service");
        /*
         * 4. 添加方法
         */
        addListByPageMethod(interfaze);
        addGetListMethod(interfaze);
        addGetOneMethod(interfaze);
        addInsertSelectiveMethod(interfaze);
        addUpdateByPrimaryKeySelectiveMethod(interfaze);
        addDeleteByPrimaryKeyMethod(interfaze);
        /*
         * 5. 返回结果
         */
        List<CompilationUnit> answer = new ArrayList<>();
        answer.add(interfaze);
        return answer;
    }

    /**
     * 分页查询
     * @param topLevelClass
     */
    protected void addListByPageMethod(Interface topLevelClass){
        AbstractJavaServiceMethodGenerator methodGenerator = new ListByPageMethodOfServiceGenerator();
        initializeAndExecuteGenerator(methodGenerator, topLevelClass);
    }

    /**
     * 获取list
     * @param topLevelClass
     */
    protected void addGetListMethod(Interface topLevelClass){
        AbstractJavaServiceMethodGenerator methodGenerator = new GetListMethodOfServiceGenerator();
        initializeAndExecuteGenerator(methodGenerator, topLevelClass);
    }

    /**
     * 获取单个
     * @param topLevelClass
     */
    protected void addGetOneMethod(Interface topLevelClass){
        AbstractJavaServiceMethodGenerator methodGenerator = new GetOneMethodOfServiceGenerator();
        initializeAndExecuteGenerator(methodGenerator, topLevelClass);
    }

    /**
     * 新增
     * @param topLevelClass
     */
    protected void addInsertSelectiveMethod(Interface topLevelClass) {
        if (introspectedTable.getRules().generateInsertSelective()) {
            AbstractJavaServiceMethodGenerator methodGenerator = new InsertMethodOfServiceGenerator(false);
            initializeAndExecuteGenerator(methodGenerator, topLevelClass);
        }
    }

    /**
     * 更新
     * @param interfaze
     */
    protected void addUpdateByPrimaryKeySelectiveMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            AbstractJavaServiceMethodGenerator methodGenerator = new UpdateByPrimaryKeySelectiveMethodOfServiceGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    /**
     * 删除
     * @param topLevelClass
     */
    protected void addDeleteByPrimaryKeyMethod(Interface topLevelClass) {
        if (introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            AbstractJavaServiceMethodGenerator methodGenerator = new DeleteByPrimaryKeyMethodOfServiceGenerator(false);
            initializeAndExecuteGenerator(methodGenerator, topLevelClass);
        }
    }

    protected void initializeAndExecuteGenerator(
    		AbstractJavaServiceMethodGenerator methodGenerator,
            Interface interfaze) {
        methodGenerator.setContext(context);
        methodGenerator.setIntrospectedTable(introspectedTable);
        methodGenerator.setProgressCallback(progressCallback);
        methodGenerator.setWarnings(warnings);
        methodGenerator.addServiceElements(interfaze);
    }

    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator() {
        return new XMLMapperGenerator();
    }
}
