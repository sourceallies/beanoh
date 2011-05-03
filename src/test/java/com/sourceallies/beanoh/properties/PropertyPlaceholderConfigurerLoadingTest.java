package com.sourceallies.beanoh.properties;

import org.junit.Test;

import com.sourceallies.beanoh.BeanohTestCase;

public class PropertyPlaceholderConfigurerLoadingTest extends BeanohTestCase {

	@Test
	public void testScanning() {
		assertContextLoading();
	}
}
