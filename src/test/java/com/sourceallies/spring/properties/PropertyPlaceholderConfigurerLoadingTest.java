package com.sourceallies.spring.properties;

import org.junit.Test;

import com.sourceallies.spring.SpringContextTestCase;

public class PropertyPlaceholderConfigurerLoadingTest  extends SpringContextTestCase {

	@Test
	public void testScanning() {
		assertContextLoading();
	}
}
