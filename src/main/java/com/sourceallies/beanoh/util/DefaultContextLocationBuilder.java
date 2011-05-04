package com.sourceallies.beanoh.util;

/**
 * Builds the context location of the bootstrap context.
 * 
 * @author David Kessler
 *
 */
public class DefaultContextLocationBuilder {

	/**
	 * Builds a bootstrap name with the same name as the test plus "-BeanohContext.xml".
	 * 
	 * @param clazz the test class which is used to determine the location and name of the bootstrap context
	 * @return the name that is built based on the test class.
	 */
	public String build(Class<?> clazz) {
		String packageName = clazz.getPackage().getName();
		String formattedPackageName = packageName.replace(".", "/");
		return formattedPackageName + "/" + clazz.getSimpleName()
				+ "-BeanohContext.xml";
	}

}
