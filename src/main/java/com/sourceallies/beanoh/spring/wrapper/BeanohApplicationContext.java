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

public class BeanohApplicationContext extends ClassPathXmlApplicationContext {
	
	List<BeanohBeanFactoryMethodInterceptor> callbacks;
	MessageUtil messageUtil = new MessageUtil();
	
	public BeanohApplicationContext(String configLocation) throws BeansException {
		super(new String[]{configLocation}, false);
		callbacks = new ArrayList<BeanohBeanFactoryMethodInterceptor>();
	}

	@Override
	protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
		Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(DefaultListableBeanFactory.class);
        BeanohBeanFactoryMethodInterceptor callback = new BeanohBeanFactoryMethodInterceptor(beanFactory);
        callbacks.add(callback);
		enhancer.setCallback(callback);
        DefaultListableBeanFactory proxy = (DefaultListableBeanFactory)enhancer.create();

		super.loadBeanDefinitions(proxy);
	}
	
	public void assertUniqueBeans(){
		for(BeanohBeanFactoryMethodInterceptor callback : callbacks){
			Map<String, List<BeanDefinition>> beanDefinitionMap = callback.getBeanDefinitionMap();
			for(String key : beanDefinitionMap.keySet()){
				List<BeanDefinition> definitions = beanDefinitionMap.get(key);
				List<String> resourceDescriptions = new ArrayList<String>();
				for(BeanDefinition definition : definitions){
					String resourceDescription = definition.getResourceDescription();
					if(!resourceDescription.endsWith("-BeanohContext.xml]")){
						resourceDescriptions.add(resourceDescription);
					}
				}
				if(resourceDescriptions.size() > 1){
					throw new DuplicateBeanDefinitionException("Bean 'person' was defined " + 
							resourceDescriptions.size() + " times:\n" + 
							messageUtil.list(resourceDescriptions));
				}
			}
		}
	}
}
