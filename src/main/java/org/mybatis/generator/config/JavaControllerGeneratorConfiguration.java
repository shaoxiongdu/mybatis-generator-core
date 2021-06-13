package org.mybatis.generator.config;

/**
 * 控制器层生成
 *
 * @author tanzibiao
 */
public class JavaControllerGeneratorConfiguration extends PropertyHolder {
	private String targetPackage;

    private String targetProject;

	public String getTargetPackage() {
		return targetPackage;
	}

	public void setTargetPackage(String targetPackage) {
		this.targetPackage = targetPackage;
	}

	public String getTargetProject() {
		return targetProject;
	}

	public void setTargetProject(String targetProject) {
		this.targetProject = targetProject;
	}
}
