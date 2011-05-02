package com.sourceallies.beanoh.properties;

import org.junit.Test;

import com.sourceallies.beanoh.SpringContextTestCase;

public class PropertyPlaceholderConfigurerLoadingTest  extends SpringContextTestCase {

	@Test
	public void testScanning() {
		assertContextLoading();
	}
}
