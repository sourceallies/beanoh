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

package com.sourceallies.beanoh;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestScope;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.SessionScope;

import com.sourceallies.beanoh.exception.MessageUtil;
import com.sourceallies.beanoh.exception.MissingComponentException;
import com.sourceallies.beanoh.exception.MissingConfigurationException;
import com.sourceallies.beanoh.spring.wrapper.BeanohApplicationContext;
import com.sourceallies.beanoh.util.DefaultContextLocationBuilder;

/**
 * Beanoh is a simple open source way to verify you Spring context. Teams that
 * leverage Beanoh spend less time focusing on configuring Spring and more time
 * adding business value.
 * 
 * @author David Kessler
 */
public class BeanohTestCase {

	private BeanohApplicationContext context;
	private Set<String> ignoredClassNames;
	private Set<String> ignoredPackages;
	private Set<String> ignoredDuplicateBeanNames;
	private MessageUtil messageUtil = new MessageUtil();
	private DefaultContextLocationBuilder defaultContextLocationBuilder = new DefaultContextLocationBuilder();

	/**
	 * Clears the ignored class name, package, and duplicate bean names lists
	 * before every test.
	 */
	@Before
	public void setUp() {
		ignoredClassNames = new HashSet<String>();
		ignoredPackages = new HashSet<String>();
		ignoredDuplicateBeanNames = new HashSet<String>();
	}

	/**
	 * Loads every bean in the Spring context. Import Spring context files in
	 * the bootstrap context. BeanohTestCase looks for a Spring context in the
	 * classpath with the same name as the test plus "-BeanohContext.xml". For
	 * eaxmple 'com.sourceallies.anything.SomethingTest' will use
	 * 'com.sourceallies.anything.SomethingTest-BeanohContext.xml' to bootstrap
	 * the Spring context.
	 */
	public void assertContextLoading() {
		assertContextLoading(false);
	}

	/**
	 * Loads every bean in the Spring context. This will fail if there are
	 * duplicate beans in the Spring context. Beans that are configured in the
	 * bootstrap context will not be considered duplicate beans.
	 * 
	 * Ignore duplicate bean names with the method ignoredDuplicateBeanNames.
	 */
	public void assertUniqueBeanContextLoading() {
		assertContextLoading(true);
	}

	/**
	 * Reconcile the beans marked with org.springframework.stereotype.Component
	 * in the classpath with the beans loaded in the Spring context.
	 * 
	 * Ignore classes with the method ignoreClassNames and ignore packages with
	 * the method ignorePackages.
	 * 
	 * @param basePackage
	 *            the base package in which classes annotated with
	 *            org.springframework.stereotype.Component will be located
	 */
	public void assertComponentsInContext(String basePackage) {
		loadContext();
		final Set<String> scannedComponents = new HashSet<String>();
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
				true);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Component.class));

		collectComponentsInClasspath(basePackage, scannedComponents, scanner);
		removeComponentsInPackages(scannedComponents);
		removeIgnoredClasses(scannedComponents);

		iterateBeanDefinitions(new BeanDefinitionAction() {
			@Override
			public void execute(String name, BeanDefinition definition) {
				scannedComponents.remove(definition.getBeanClassName());
			}
		});

		if (scannedComponents.size() > 0) {
			throw new MissingComponentException(
					"There are beans marked with '@Component' in the classpath that are not configured by Spring. "
							+ "Either configure these beans or ignore them with the 'ignoreClassNames' or 'ignorePackages' method.\n"
							+ "Components not in Spring:"
							+ missingList(scannedComponents));
		}
	}

	/**
	 * Registers class names that should be ignored when using
	 * assertComponentsInContext.
	 * 
	 * @param classNames
	 *            an array of class names that should be ignored when using
	 *            assertComponentsInContext.
	 */
	public void ignoreClassNames(String... classNames) {
		for (String className : classNames) {
			ignoredClassNames.add(className);
		}
	}

	/**
	 * Registers base packages that should be ignored when using
	 * assertComponentsInContext.
	 * 
	 * @param packages
	 *            an array of base packages that should be ignored when using
	 *            assertComponentsInContext.
	 */
	public void ignorePackages(String... packages) {
		for (String pakage : packages) {
			ignoredPackages.add(pakage);
		}
	}

	/**
	 * Registers bean names that should be ignored when using
	 * assertUniqueBeanContextLoading.
	 * 
	 * @param beanNames
	 *            an array of bean names that should be ignored when using
	 *            assertUniqueBeanContextLoading.
	 */
	public void ignoreDuplicateBeanNames(String... beanNames) {
		for (String beanName : beanNames) {
			ignoredDuplicateBeanNames.add(beanName);
		}
	}

	private void assertContextLoading(boolean assertUniqueBeans) {
		loadContext();
		iterateBeanDefinitions(new BeanDefinitionAction() {
			@Override
			public void execute(String name, BeanDefinition definition) {
				context.getBean(name);
			}
		});
		if (assertUniqueBeans)
			context.assertUniqueBeans(ignoredDuplicateBeanNames);
	}

	private String missingList(Set<String> missingComponents) {
		return messageUtil.list(new ArrayList<String>(missingComponents));
	}

	private void iterateBeanDefinitions(BeanDefinitionAction action) {
		String[] names = context.getBeanDefinitionNames();
		for (String name : names) {
			BeanDefinition beanDefinition = context.getBeanFactory()
					.getBeanDefinition(name);
			if (!beanDefinition.isAbstract()) {
				action.execute(name, beanDefinition);
			}
		}
	}

	private void removeComponentsInPackages(final Set<String> scannedComponents) {
		for (String scannedComponent : scannedComponents) {
			for (String ignoredPackage : ignoredPackages) {
				if (scannedComponent.startsWith(ignoredPackage)) {
					ignoredClassNames.add(scannedComponent);
				}
			}
		}
	}

	private void removeIgnoredClasses(final Set<String> scannedComponents) {
		for (String ignoredClassName : ignoredClassNames) {
			scannedComponents.remove(ignoredClassName);
		}
	}

	private void collectComponentsInClasspath(String basePackage,
			final Set<String> scannedComponents,
			ClassPathScanningCandidateComponentProvider scanner) {
		for (BeanDefinition beanDefinition : scanner
				.findCandidateComponents(basePackage)) {
			scannedComponents.add(beanDefinition.getBeanClassName());
		}
	}

	private void loadContext() {
		if (context == null) {
			String contextLocation = defaultContextLocationBuilder
					.build(getClass());
			context = new BeanohApplicationContext(contextLocation);
			try {
				context.refresh();
			} catch (BeanDefinitionParsingException e) {
				throw e;
			} catch (BeanDefinitionStoreException e) {
				throw new MissingConfigurationException("Unable to locate "
						+ contextLocation + ".", e);
			}

			context.getBeanFactory().registerScope("session",
					new SessionScope());
			context.getBeanFactory().registerScope("request",
					new RequestScope());
			MockHttpServletRequest request = new MockHttpServletRequest();
			ServletRequestAttributes attributes = new ServletRequestAttributes(
					request);
			RequestContextHolder.setRequestAttributes(attributes);
		}
	}
}
