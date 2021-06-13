package org.mybatis.generator.custom.field;
import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.PrimitiveTypeWrapper;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.VisitableElement;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * 自定义插件 - 自定义查询指定字段
 * @author laiyanlin
 */
public class FieldsPlugin extends PluginAdapter {
    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        // add field, getter, setter for limit clause
        addFields(topLevelClass, introspectedTable, "fields");
        return super.modelExampleClassGenerated(topLevelClass,
                introspectedTable);
    }

	@Override
	public boolean sqlMapBaseColumnListElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {


		// TODO Auto-generated method stub
		XmlElement isNullElement = new XmlElement("if"); //$NON-NLS-1$
		isNullElement.addAttribute(new Attribute("test", "fields == null")); //$NON-NLS-1$ //$NON-NLS-2$
		//isNotNullElement.addAttribute(new Attribute("compareValue", "0")); //$NON-NLS-1$ //$NON-NLS-2$
    	for(VisitableElement e : element.getElements()){
    		isNullElement.addElement(e);
    	}
     	element.getElements().clear();
    	element.addElement(isNullElement);

		XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
		isNotNullElement.addAttribute(new Attribute("test", "fields != null")); //$NON-NLS-1$ //$NON-NLS-2$
		//isNotNullElement.addAttribute(new Attribute("compareValue", "0")); //$NON-NLS-1$ //$NON-NLS-2$
		isNotNullElement.addElement(new TextElement(
		      "${fields}"));
		//isParameterPresenteElemen.addElement(isNotNullElement);
		element.addElement(isNotNullElement);

		return super.sqlMapBaseColumnListElementGenerated(element, introspectedTable);
	}

	private void addFields(TopLevelClass topLevelClass,
    		IntrospectedTable introspectedTable, String name) {
    	CommentGenerator commentGenerator = context.getCommentGenerator();
    	Field field = new Field(name, PrimitiveTypeWrapper.getStringInstance());
    	field.setVisibility(JavaVisibility.PROTECTED);
		//field.setType(FullyQualifiedJavaType.getIntInstance());
    	//field.setType(PrimitiveTypeWrapper.getStringInstance());
    	//field.setName(name);
		//field.setInitializationString("1");
    	commentGenerator.addFieldComment(field, introspectedTable);
    	topLevelClass.addField(field);
    	char c = name.charAt(0);
    	String camel = Character.toUpperCase(c) + name.substring(1);
    	Method method = new Method("set" + camel);
    	method.setVisibility(JavaVisibility.PUBLIC);
    	method.setName("set" + camel);
    	method.addParameter(new Parameter(PrimitiveTypeWrapper.getStringInstance(), name));
    	method.addBodyLine("this." + name + "=" + name + ";");
    	commentGenerator.addGeneralMethodComment(method, introspectedTable);
    	topLevelClass.addMethod(method);
    	method = new Method("get" + camel);
    	method.setVisibility(JavaVisibility.PUBLIC);
    	method.setReturnType(PrimitiveTypeWrapper.getStringInstance());
    	method.setName("get" + camel);
    	method.addBodyLine("return " + name + ";");
    	commentGenerator.addGeneralMethodComment(method, introspectedTable);
    	topLevelClass.addMethod(method);
    }



	@Override
	public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {

		List<VisitableElement> elements = element.getElements();

		StringBuilder columns = new StringBuilder();

		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();


		for (IntrospectedColumn introspectedColumn : allColumns) {

			columns.append(",").append(introspectedColumn.getActualColumnName());
		}
		columns.deleteCharAt(0);
		elements.set(1, new TextElement(columns.toString()));


		return super.sqlMapSelectByPrimaryKeyElementGenerated(element,
				introspectedTable);
	}

	/**
     * This plugin is always valid - no properties are required
     */
    @Override
	public boolean validate(List<String> warnings) {
        return true;
    }

}
