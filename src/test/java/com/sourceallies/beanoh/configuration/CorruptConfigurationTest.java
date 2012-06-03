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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.BeanDefinitionStoreException;

import com.sourceallies.beanoh.BeanohTestCase;
import com.sourceallies.beanoh.util.DefaultContextLocationBuilder;

public class CorruptConfigurationTest extends BeanohTestCase {

	@Test
	public void testCorruptConfiguration_preserveStackTrace() {
		try {
			assertContextLoading();
			fail();
		} catch (Exception e) {
			String contextName = new DefaultContextLocationBuilder()
					.build(getClass());
			assertEquals("Unable to locate " + contextName + ".",
					e.getMessage());
			
			assertTrue(e.getCause() instanceof BeanDefinitionStoreException);
			assertEquals("Line 36 in XML document from class path resource [com/sourceallies/beanoh/configuration/CorruptConfigurationTest-BeanohContext.xml] is invalid;"
					+" nested exception is org.xml.sax.SAXParseException: The end-tag for element type \"bean\" must end with a '>' delimiter.", 
					e.getCause().getMessage());
			
			assertTrue(e.getCause().getCause() instanceof org.xml.sax.SAXParseException);
			assertEquals("The end-tag for element type \"bean\" must end with a '>' delimiter.", 
					e.getCause().getCause().getMessage());
			
		}
	}
}
