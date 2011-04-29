package com.sourceallies.spring.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class DefaultContextLocationBuilderTest {
	
	@Test
	public void testBuild(){
		DefaultContextLocationBuilder builder = new DefaultContextLocationBuilder();
		assertEquals("java/lang/String-context.xml", builder.build(String.class));
	}

}
