package com.sourceallies.beanoh.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DefaultContextLocationBuilderTest {

	@Test
	public void testBuild() {
		DefaultContextLocationBuilder builder = new DefaultContextLocationBuilder();
		assertEquals("java/lang/String-BeanohContext.xml",
				builder.build(String.class));
	}

}
