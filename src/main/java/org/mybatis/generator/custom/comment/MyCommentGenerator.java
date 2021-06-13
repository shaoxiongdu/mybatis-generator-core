package org.mybatis.generator.custom.comment;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.internal.util.StringUtility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;


/**
 * @author shensr
 * @version V1.0
 * @description mybatis generator自定义生成注释插件类
 * @create 2019/10/4
 **/
public class MyCommentGenerator implements CommentGenerator {

    private Properties properties = new Properties();
    /**
     * 抑制日期  默认false：不抑制
     */
    private boolean suppressDate = false;
    /**
     * 抑制注释 默认false：不抑制
     */
    private boolean suppressAllComments = false;

    /**
     * 显示数据库comments 默认false：不显示
     */
    private boolean addRemarkComments = false;
    /**
     * 日期格式
     */
    private SimpleDateFormat dateFormat;

    public MyCommentGenerator() {
        super();
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    }


    /**
     * 读取配置文件
     *
     * @param properties
     */
    @Override
    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);
        this.suppressDate = StringUtility.isTrue(properties.getProperty("suppressDate"));
        this.suppressAllComments = StringUtility.isTrue(properties.getProperty("suppressAllComments"));
        this.addRemarkComments = StringUtility.isTrue(properties.getProperty("addRemarkComments"));
        String dateFormatString = properties.getProperty("dateFormat");
        if (StringUtility.stringHasValue(dateFormatString)) {
            this.dateFormat = new SimpleDateFormat(dateFormatString);
        }

    }

    /**
     * 创建的数据表对应的类添加的注释
     *
     * @param topLevelClass
     * @param introspectedTable
     */
    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (!this.suppressAllComments) {
            topLevelClass.addJavaDocLine("/**");
            topLevelClass.addJavaDocLine(" * " + introspectedTable.getFullyQualifiedTable());
            topLevelClass.addJavaDocLine(" * ");
            topLevelClass.addJavaDocLine(" * @author "+introspectedTable.getContext().getAuthorConfiguration().getName());
            topLevelClass.addJavaDocLine(" * @date " + this.getDateString());
            topLevelClass.addJavaDocLine(" */");
        }
    }

    /**
     * <p>生成xx.java文件（model）属性的注释</p>
     *
     * @param field
     * @param introspectedTable
     * @param introspectedColumn
     */
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (!this.suppressAllComments) {
            // 注释开始的地方
            field.addJavaDocLine("/**");
            String remarks = introspectedColumn.getRemarks();
            // 开启注释，并且数据库中comment有值
            if (this.addRemarkComments && StringUtility.stringHasValue(remarks)) {
                // 通过换行符分割 System.getProperty("line.separator")：换行符 ，屏蔽了 Windows和Linux的区别
                String[] remarkLines = remarks.split(System.getProperty("line.separator"));
                int length = remarkLines.length;
                // 如果有多行，就换行显示
                for (int i = 0; i < length; i++) {
                    String remarkLine = remarkLines[i];
                    field.addJavaDocLine(" * " + remarkLine);
                }
            }
            // 注释结束
            field.addJavaDocLine(" */");
        }
    }

    /**
     * xxxMapper接口和xxxExample类方法注解
     *
     * @param method
     * @param introspectedTable
     */
    @Override
    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
        if (!this.suppressAllComments) {
            method.addJavaDocLine("/**");
            method.addJavaDocLine(" * " + method.getName());
            List<Parameter> parameters = method.getParameters();
            parameters.forEach(parameter -> method.addJavaDocLine(" * @param " + parameter.getName()));
            // 如果有返回类型，添加@return
            String returnType = "void";
            if (!returnType.equals(method.getReturnType())) {
                method.addJavaDocLine(" * @return ");
            }
            method.addJavaDocLine(" */");
        }

    }

    @Override
    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable, String comment) {
        if (!this.suppressAllComments) {
            method.addJavaDocLine("/**");
            method.addJavaDocLine(" * " + comment);
            List<Parameter> parameters = method.getParameters();
            parameters.forEach(parameter -> method.addJavaDocLine(" * @param " + parameter.getName()));
            // 如果有返回类型，添加@return
            String returnType = "void";
            if (!returnType.equals(method.getReturnType())) {
                method.addJavaDocLine(" * @return ");
            }
            method.addJavaDocLine(" */");
        }
    }

    /**
     * 数据库对应实体类的Getter方法注解
     *
     * @param method
     * @param introspectedTable
     * @param introspectedColumn
     */
    @Override
    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {

    }

    /**
     * 数据库对应实体类的Setter方法注解
     *
     * @param method
     * @param introspectedTable
     * @param introspectedColumn
     */
    @Override
    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {

    }

    /**
     * 生成xxMapper.XML文件的注释
     *
     * @param xmlElement
     */
    @Override
    public void addComment(XmlElement xmlElement) {
    }

    @Override
    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {

    }

    @Override
    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {

    }

    @Override
    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {

    }

    @Override
    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {
    }

    @Override
    public void addClassAnnotation(InnerClass innerClass, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {
    }

    @Override
    public void addJavaFileComment(CompilationUnit compilationUnit) {
    }

    @Override
    public void addRootComment(XmlElement rootElement) {
        if (suppressAllComments) {
            return;
        }

        rootElement.addElement(new TextElement("<!--")); //$NON-NLS-1$

        StringBuilder sb = new StringBuilder();
        sb.append("  WARNING - "); //$NON-NLS-1$
        sb.append(MergeConstants.NEW_ELEMENT_TAG);
        rootElement.addElement(new TextElement(sb.toString()));
        rootElement.addElement(
                new TextElement("  This element is automatically generated by MyBatis Generator," //$NON-NLS-1$
                        + " do not modify.")); //$NON-NLS-1$

        String s = getDateString();
        if (s != null) {
            sb.setLength(0);
            sb.append("  This element was generated on "); //$NON-NLS-1$
            sb.append(s);
            sb.append('.');
            rootElement.addElement(new TextElement(sb.toString()));
        }

        rootElement.addElement(new TextElement("-->")); //$NON-NLS-1$
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
    }

    @Override
    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {

    }

    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {

    }

    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {

    }

    @Override
    public void addClassComment(JavaElement innerClass, IntrospectedTable introspectedTable, String layer) {
        if (suppressAllComments) {
            return;
        }

        innerClass.addJavaDocLine("/**"); //$NON-NLS-1$
        // 获取数据库注释
        String comment = introspectedTable.getFullyQualifiedTable().getRemark();
        if("model".equalsIgnoreCase(layer)){
            innerClass.addJavaDocLine(" * " + comment+"实体类");
        }else if("dao".equalsIgnoreCase(layer)){
            innerClass.addJavaDocLine(" * " + comment+"Dao类");
        }else if("service".equalsIgnoreCase(layer)){
            innerClass.addJavaDocLine(" * " + comment+"Service类");
        }else if("serviceImpl".equalsIgnoreCase(layer)){
            innerClass.addJavaDocLine(" * " + comment+"Service实现类");
        }else if("controller".equalsIgnoreCase(layer)){
            innerClass.addJavaDocLine(" * " + comment+"Controller类");
        }

        innerClass.addJavaDocLine(" * ");
        innerClass.addJavaDocLine(" * @author "+introspectedTable.getContext().getAuthorConfiguration().getName());
        innerClass.addJavaDocLine(" * @date " + this.getDateString());
        addJavadocTag(innerClass, false);
        innerClass.addJavaDocLine(" */"); //$NON-NLS-1$
    }

    /**
     * This method adds the custom javadoc tag for. You may do nothing if you do not
     * wish to include the Javadoc tag - however, if you do not include the Javadoc
     * tag then the Java merge capability of the eclipse plugin will break.
     *
     * @param javaElement       the java element
     * @param markAsDoNotDelete the mark as do not delete
     */
    protected void addJavadocTag(JavaElement javaElement, boolean markAsDoNotDelete) {
        javaElement.addJavaDocLine(" *"); //$NON-NLS-1$
        StringBuilder sb = new StringBuilder();
        sb.append(" * "); //$NON-NLS-1$
        sb.append(MergeConstants.NEW_ELEMENT_TAG);
        if (markAsDoNotDelete) {
            sb.append(" do_not_delete_during_merge"); //$NON-NLS-1$
        }
        String s = getDateString();
        if (s != null) {
            sb.append(' ');
            sb.append(s);
        }
        javaElement.addJavaDocLine(sb.toString());
    }

    /**
     * 日期格式化
     *
     * @return 格式化后的日期
     */
    protected String getDateString() {
        if (this.suppressDate) {
            return null;
        } else {
            return this.dateFormat != null ? this.dateFormat.format(new Date()) : (new Date()).toString();
        }
    }
}
