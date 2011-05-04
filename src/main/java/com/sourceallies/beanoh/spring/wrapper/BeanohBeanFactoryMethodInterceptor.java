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

package com.sourceallies.beanoh.spring.wrapper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * A proxy that delegates to a real Spring bean factory. This collects the
 * arguments that are passed to the registerBeanDefinition method on the bean
 * factory. These bean definitions are inspected later by the
 * BeanohApplicationContext to determine if there are duplicate bean
 * definitions.
 * 
 * @author David Kessler
 * 
 */
public class BeanohBeanFactoryMethodInterceptor implements MethodInterceptor {

	private DefaultListableBeanFactory delegate;
	private Class<? extends DefaultListableBeanFactory> delegateClass;
	private Map<String, List<BeanDefinition>> beanDefinitionMap;

	/**
	 * Constructs a new proxy.
	 * 
	 * @param delegate
	 *            the delegate that will be called when the proxy methods are
	 *            invoked
	 */
	public BeanohBeanFactoryMethodInterceptor(
			DefaultListableBeanFactory delegate) {
		this.delegate = delegate;
		this.delegateClass = delegate.getClass();
		beanDefinitionMap = new HashMap<String, List<BeanDefinition>>();
	}

	/**
	 * Intercepts method calls to the proxy and calls the corresponding method
	 * on the delegate.
	 * 
	 * Collects bean definitions that are registered for later inspection.
	 */
	@Override
	public Object intercept(Object object, Method method, Object[] args,
			MethodProxy methodProxy) throws Throwable {
		Method delegateMethod = delegateClass.getMethod(method.getName(),
				method.getParameterTypes());
		if ("registerBeanDefinition".equals(method.getName())) {
			if (beanDefinitionMap.containsKey(args[0])) {
				List<BeanDefinition> definitions = beanDefinitionMap
						.get(args[0]);
				definitions.add((BeanDefinition) args[1]);
			} else {
				List<BeanDefinition> beanDefinitions = new ArrayList<BeanDefinition>();
				beanDefinitions.add((BeanDefinition) args[1]);
				beanDefinitionMap.put((String) args[0], beanDefinitions);
			}
		}
		return delegateMethod.invoke(delegate, args);
	}

	/**
	 * Provides access to the bean definitions that this proxy collects.
	 * 
	 * @return map of bean definition names and a list of definitions with that
	 *         name
	 */
	public Map<String, List<BeanDefinition>> getBeanDefinitionMap() {
		return beanDefinitionMap;
	}
}