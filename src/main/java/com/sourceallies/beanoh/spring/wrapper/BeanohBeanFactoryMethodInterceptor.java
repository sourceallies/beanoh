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



public class BeanohBeanFactoryMethodInterceptor implements MethodInterceptor{
	
	private DefaultListableBeanFactory delegate;
	private Class<? extends DefaultListableBeanFactory> delegateClass;
	private Map<String, List<BeanDefinition>> beanDefinitionMap;

	public BeanohBeanFactoryMethodInterceptor(DefaultListableBeanFactory delegate){
		this.delegate = delegate;
		this.delegateClass = delegate.getClass();
		beanDefinitionMap = new HashMap<String, List<BeanDefinition>>();
	}

	@Override
	public Object intercept(Object object, Method method, Object[] args,
		    MethodProxy methodProxy) throws Throwable {
		Method delegateMethod = delegateClass.getMethod(method.getName(), method.getParameterTypes());
		if("registerBeanDefinition".equals(method.getName())){
			if(beanDefinitionMap.containsKey(args[0])){
				List<BeanDefinition> definitions = beanDefinitionMap.get(args[0]);
				definitions.add((BeanDefinition) args[1]);
			}else{
				List<BeanDefinition> beanDefinitions = new ArrayList<BeanDefinition>();
				beanDefinitions.add((BeanDefinition) args[1]);
				beanDefinitionMap.put((String) args[0], beanDefinitions);
			}
		}
		return delegateMethod.invoke(delegate, args);
	}

	public Map<String, List<BeanDefinition>> getBeanDefinitionMap() {
		return beanDefinitionMap;
	}
}