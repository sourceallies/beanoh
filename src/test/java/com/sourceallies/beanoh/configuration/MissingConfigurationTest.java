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

package com.sourceallies.beanoh.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.sourceallies.beanoh.BeanohTestCase;
import com.sourceallies.beanoh.util.DefaultContextLocationBuilder;

public class MissingConfigurationTest extends BeanohTestCase {

	@Test
	public void testMissingConfiguration() {
		try {
			assertContextLoading();
			fail();
		} catch (Exception e) {
			String contextName = new DefaultContextLocationBuilder()
					.build(getClass());
			assertEquals("Unable to locate " + contextName + ".",
					e.getMessage());
		}
	}
}
