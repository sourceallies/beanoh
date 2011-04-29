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

package com.sourceallies.spring.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.sourceallies.spring.UniqueBeanSpringContextTestCase;
import com.sourceallies.spring.exception.MissingConfigurationException;
import com.sourceallies.spring.util.DefaultContextLocationBuilder;

public class MissingConfigurationTest extends UniqueBeanSpringContextTestCase {
	
	@Test
	public void testMissingConfiguration() {
		try{
			assertContextLoading();
			fail();
		}catch(MissingConfigurationException e){
			String contextName = new DefaultContextLocationBuilder().build(getClass());
			
			assertEquals("Unable to locate one of the configuration location(s):\n" + contextName + 
					"\nUse the 'registerContextLocation' method to configure custom configuration location(s).", 
					e.getMessage());
		}
	}
}
