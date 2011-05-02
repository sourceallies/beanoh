package com.sourceallies.beanoh.util;

public class DefaultContextLocationBuilder {

	public String build(Class<?> clazz) {
		String packageName = clazz.getPackage().getName();
		String formattedPackageName = packageName.replace(".", "/");
		return formattedPackageName + "/" + clazz.getSimpleName() + "-context.xml";
	}

}
