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

package com.sourceallies.beanoh.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.sourceallies.beanoh.BeanohTestCase;
import com.sourceallies.beanoh.exception.MissingComponentException;

public class MissingComponentTest extends BeanohTestCase {

	@Test
	public void testScanning() {
		try {
			assertComponentsInContext("com.sourceallies");
			fail();
		} catch (MissingComponentException e) {
			assertEquals(
					"There are beans marked with '@Component' in the classpath that are not configured by Spring. "
							+ "Either configure these beans or ignore them with the 'ignoreClassNames' or 'ignorePackages' method.\n"
							+ "Components not in Spring:\n"
							+ "com.sourceallies.b.B\n"
							+ "com.sourceallies.c.C\n"
							+ "com.sourceallies.test.Person", e.getMessage());
		}
	}

	@Test
	public void testIgnoreClassNames() {
		ignoreClassNames("com.sourceallies.b.B", "com.sourceallies.c.C",
				"com.sourceallies.test.Person");

		assertComponentsInContext("com.sourceallies");
	}

	@Test
	public void testIgnorePackages() {
		ignorePackages("com.sourceallies.b", "com.sourceallies.c",
				"com.sourceallies.test");

		assertComponentsInContext("com.sourceallies");
	}
}
