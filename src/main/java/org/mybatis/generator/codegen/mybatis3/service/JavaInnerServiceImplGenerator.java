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
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.service.elements.impl.*;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * service实现构造类
 *
 * @author laiyanlin
 *
 */
public class JavaInnerServiceImplGenerator extends AbstractJavaClientGenerator {

    /**
     *
     */
    public JavaInnerServiceImplGenerator(String project) {
        super(project, true);
    }

    public JavaInnerServiceImplGenerator(String project, boolean requiresMatchedXMLGenerator) {
        super(project, requiresMatchedXMLGenerator);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        progressCallback.startTask(getString("Progress.17", //$NON-NLS-1$
                introspectedTable.getFullyQualifiedTable().toString()));

        /*
         * 1. 获取基础类
         */
        FullyQualifiedJavaType modelType = new FullyQualifiedJavaType(
                introspectedTable.getBaseRecordType());
        /*
         * 2. 设置类名
         */
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(context.getJavaServiceGeneratorConfiguration().getTargetPackage()+".impl."+modelType.getShortName()+"ServiceImpl");
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);

        /*
         * 3. 设置继承的service，并导包
         */
        FullyQualifiedJavaType impl = new FullyQualifiedJavaType(context.getJavaServiceGeneratorConfiguration().getTargetPackage()+"."+
                modelType.getShortName()+"Service");
        topLevelClass.addSuperInterface(impl);
        topLevelClass.addImportedType(impl);

        /*
         * 4. 添加service注解，并导包
         */
        topLevelClass.addAnnotation("@Service(\""+ StringUtility.getFirstLowerCaseWord(impl.getShortName()) +"\")");
        topLevelClass.addImportedType("org.springframework.stereotype.Service");

        /*
         * 5. 添加dao，并导包
         */
        FullyQualifiedJavaType mapper = new FullyQualifiedJavaType(
                introspectedTable.getMyBatis3JavaMapperType());
        Field field = new Field(mapper.getInjectName(), mapper);
        field.setVisibility(JavaVisibility.PRIVATE);
        field.addAnnotation("@Resource");
        topLevelClass.addField(field);
        topLevelClass.addImportedType("javax.annotation.Resource");
        topLevelClass.addImportedType(mapper);

        /*
         * 6. 类加注释
         */
        CommentGenerator commentGenerator = context.getCommentGenerator();
        commentGenerator.addClassComment(topLevelClass, introspectedTable,"serviceImpl");

        // 7. 方法添加
        addListByPageMethod(topLevelClass);
        addGetListMethod(topLevelClass);
        addGetOneMethod(topLevelClass);
        addInsertSelectiveMethod(topLevelClass);
        addUpdateByPrimaryKeySelectiveMethod(topLevelClass);
        addDeleteByPrimaryKeyMethod(topLevelClass);
        addBuildCriteriaMethod(topLevelClass);

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        answer.add(topLevelClass);

        return answer;
    }


    protected void addDeleteByPrimaryKeyMethod(TopLevelClass topLevelClass) {
        if (introspectedTable.getRules().generateDeleteByPrimaryKey()) {
        	AbstractJavaServiceImplMethodGenerator methodGenerator = new DeleteByPrimaryKeyMethodOfServiceImplGenerator(false);
            initializeAndExecuteGenerator(methodGenerator, topLevelClass);
        }
    }

    protected void addInsertMethod(TopLevelClass topLevelClass) {
        if (introspectedTable.getRules().generateInsert()) {
            AbstractJavaServiceImplMethodGenerator methodGenerator = new InsertMethodOfServiceImplGenerator(false);
            initializeAndExecuteGenerator(methodGenerator, topLevelClass);
        }
    }

    protected void addInsertSelectiveMethod(TopLevelClass topLevelClass) {
        if (introspectedTable.getRules().generateInsertSelective()) {
            AbstractJavaServiceImplMethodGenerator methodGenerator = new InsertMethodOfServiceImplGenerator(false);
            initializeAndExecuteGenerator(methodGenerator, topLevelClass);
        }
    }

    /**
     * 新增：根据条件查询
     *
     * @author laiyanlin
     * 2015年5月15日
     */
    protected void addListByPageMethod(TopLevelClass topLevelClass){
        AbstractJavaServiceImplMethodGenerator methodGenerator = new ListByPageMethodOfServiceImplGenerator();
        initializeAndExecuteGenerator(methodGenerator, topLevelClass);
    }

    /**
     * 新增：根据条件查询
     *
     * @author laiyanlin
     * 2015年5月15日
     */
    protected void addGetListMethod(TopLevelClass topLevelClass){
        AbstractJavaServiceImplMethodGenerator methodGenerator = new GetListMethodOfServiceImplGenerator();
        initializeAndExecuteGenerator(methodGenerator, topLevelClass);
    }

    /**
     * 新增：根据条件查询
     *
     * @author laiyanlin
     * 2015年5月15日
     */
    protected void addGetOneMethod(TopLevelClass topLevelClass){
        AbstractJavaServiceImplMethodGenerator methodGenerator = new GetOneMethodOfServiceImplGenerator();
        initializeAndExecuteGenerator(methodGenerator, topLevelClass);
    }

    protected void addUpdateByPrimaryKeySelectiveMethod(TopLevelClass topLevelClass) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
        	AbstractJavaServiceImplMethodGenerator methodGenerator = new UpdateByPrimaryKeySelectiveMethodOfServiceImplGenerator(false);
            initializeAndExecuteGenerator(methodGenerator, topLevelClass);
        }
    }

    protected void addBuildCriteriaMethod(TopLevelClass topLevelClass) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            AbstractJavaServiceImplMethodGenerator methodGenerator = new BuildCriteriaMethodOfServiceImplGenerator();
            initializeAndExecuteGenerator(methodGenerator, topLevelClass);
        }
    }


    protected void initializeAndExecuteGenerator(
            AbstractJavaServiceImplMethodGenerator methodGenerator,
            TopLevelClass topLevelClass) {
        methodGenerator.setContext(context);
        methodGenerator.setIntrospectedTable(introspectedTable);
        methodGenerator.setProgressCallback(progressCallback);
        methodGenerator.setWarnings(warnings);
        methodGenerator.addServiceElements(topLevelClass);
    }

    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator() {
        return new XMLMapperGenerator();
    }
}
