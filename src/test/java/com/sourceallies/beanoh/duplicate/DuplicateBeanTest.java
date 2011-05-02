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

package com.sourceallies.beanoh.duplicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;

import com.sourceallies.beanoh.UniqueBeanSpringContextTestCase;

public class DuplicateBeanTest extends UniqueBeanSpringContextTestCase {
	
	@Test
	public void testDups() {
		try{
			assertContextLoading();
			fail();
		}catch(BeanDefinitionParsingException e){
			assertEquals("Configuration problem: Failed to register bean definition with name 'person'\n" +
					"Offending resource: class path resource " +
					"[com/sourceallies/beanoh/duplicate/DuplicateBeanTest-context.xml]; " +
					"nested exception is org.springframework.beans.factory.BeanDefinitionStoreException: " +
					"Invalid bean definition with name 'person' defined in class path resource " +
					"[com/sourceallies/beanoh/duplicate/DuplicateBeanTest-context.xml]: " +
					"Cannot register bean definition [Generic bean: class [com.sourceallies.test.Person]; " +
					"scope=; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; " +
					"autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; " +
					"initMethodName=null; destroyMethodName=null; defined in class path resource " +
					"[com/sourceallies/beanoh/duplicate/DuplicateBeanTest-context.xml]] for " +
					"bean 'person': There is already [Generic bean: class [com.sourceallies.test.Person]; " +
					"scope=; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; " +
					"autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; " +
					"initMethodName=null; destroyMethodName=null; defined in class path resource " +
					"[com/sourceallies/beanoh/duplicate/Duplicate-context.xml]] bound.", e.getMessage());
		}
	}
}
