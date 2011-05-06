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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.cglib.proxy.Enhancer;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sourceallies.beanoh.exception.DuplicateBeanDefinitionException;
import com.sourceallies.beanoh.exception.MessageUtil;

/**
 * Wraps XML context loading in order to track loaded bean definitions. This
 * information is used to determine if duplicate bean definitions have been
 * loaded
 * 
 * @author David Kessler
 * 
 */
public class BeanohApplicationContext extends ClassPathXmlApplicationContext {

	List<BeanohBeanFactoryMethodInterceptor> callbacks;
	MessageUtil messageUtil = new MessageUtil();

	/**
	 * Constructs a new Spring application context based on the bootstrap
	 * context location.
	 * 
	 * @param configLocation
	 * @throws BeansException
	 */
	public BeanohApplicationContext(String configLocation)
			throws BeansException {
		super(new String[] { configLocation , "com/sourceallies/beanoh/spring/Base-BeanohContext.xml"}, false);
		callbacks = new ArrayList<BeanohBeanFactoryMethodInterceptor>();
	}

	@Override
	protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
			throws BeansException, IOException {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(DefaultListableBeanFactory.class);
		BeanohBeanFactoryMethodInterceptor callback = new BeanohBeanFactoryMethodInterceptor(
				beanFactory);
		callbacks.add(callback);
		enhancer.setCallback(callback);
		DefaultListableBeanFactory proxy = (DefaultListableBeanFactory) enhancer
				.create();

		super.loadBeanDefinitions(proxy);
	}

	/**
	 * This will fail if there are duplicate beans in the Spring context. Beans
	 * that are configured in the bootstrap context will not be considered
	 * duplicate beans.
	 */
	public void assertUniqueBeans(Set<String> ignoredDuplicateBeanNames) {
		for (BeanohBeanFactoryMethodInterceptor callback : callbacks) {
			Map<String, List<BeanDefinition>> beanDefinitionMap = callback
					.getBeanDefinitionMap();
			for (String key : beanDefinitionMap.keySet()) {
				if (!ignoredDuplicateBeanNames.contains(key)) {
					List<BeanDefinition> definitions = beanDefinitionMap
							.get(key);
					List<String> resourceDescriptions = new ArrayList<String>();
					for (BeanDefinition definition : definitions) {
						String resourceDescription = definition
								.getResourceDescription();
						if (resourceDescription == null) {
							resourceDescriptions.add(definition.getBeanClassName());
						}else if (!resourceDescription
								.endsWith("-BeanohContext.xml]")) {
							if(!resourceDescriptions.contains(resourceDescription)){
								resourceDescriptions.add(resourceDescription);
							}
						}
					}
					if (resourceDescriptions.size() > 1) {
						throw new DuplicateBeanDefinitionException("Bean '"
								+ key + "' was defined "
								+ resourceDescriptions.size() + " times.\n"
								+ "Either remove duplicate bean definitions or ignore them with the 'ignoredDuplicateBeanNames' method.\n"
								+ "Configuration locations:"
								+ messageUtil.list(resourceDescriptions));
					}
				}
			}
		}
	}
}
