/*
Copyright (c) 2011  Source Allies

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation version 3.0.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, please visit 
http://www.gnu.org/licenses/lgpl-3.0.txt.
 */

package com.sourceallies.beanoh.util;

/**
 * Builds the context location of the bootstrap context.
 * 
 * @author David Kessler
 * 
 */
public class DefaultContextLocationBuilder {

	/**
	 * Builds a bootstrap name with the same name as the test plus
	 * "-BeanohContext.xml".
	 * 
	 * @param clazz
	 *            the test class which is used to determine the location and
	 *            name of the bootstrap context
	 * @return the name that is built based on the test class.
	 */
	public String build(Class<?> clazz) {
		String packageName = clazz.getPackage().getName();
		String formattedPackageName = packageName.replace(".", "/");
		return formattedPackageName + "/" + clazz.getSimpleName()
				+ "-BeanohContext.xml";
	}

}
