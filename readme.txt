== Overview
Beanoh is a simple way to verify you Spring context.
Teams that leverage Beanoh spend less time focusing on 
configuring Spring and more time adding business value.  

== Features
1. Verify that all of your beans are wired correctly
2. Reconcile the beans marked with @Component in the classpath 
   with the beans loaded in the Spring context
3. Prevent duplicate bean definition overwriting

== Building
mvn clean install

== Dependencies
Refer to the dependencies listed in the pom.xml file

== Using
1. Verify Spring wiring
////////
public class SomeTest extends BeanohTestCase {

	@Test
	public void testSomething() {
		assertContextLoading();
	}
}
////////

2. Reconcile @Components
////////
public class SomeTest extends BeanohTestCase {

	@Test
	public void testSomething() {
		assertComponentsInContext("com.sourceallies");
	}
}
////////

3. Ensure Unique Bean Definitions
////////
public class SomeTest extends BeanohTestCase {

	@Test
	public void testSomething() {
		assertUniqueBeanContextLoading();
	}
}
////////

== Test Design
Beanoh requires a test bootstrap Spring context file.  BeanohTestCase looks
for a Spring context within the classpath with the same name as the test
plus "-BeanohContext.xml".  For eacmple 'com.sourceallies.anything.SomethingTest'
will use 'com.sourceallies.anything.SomethingTest-BeanohContext.xml' to bootstrap
the Spring context.  Add imports to this bootstrap context for each of the context 
files that you wish to load.  If you need to override beans they must be added to
this bootstrap context in order to not count as a duplicate bean definition.

== Best Practices
1. Ignore classes annotated with @Component explicitly rather than by package.
For example if you run 'assertComponentsInContext("com.sourceallies");' and it
fails on a bean "com.sourceallies.something.Anything" in a dependant jar that
you do not want to add to your context.  Add 
'ignoreClassName("com.sourceallies.something.Anything");' to your test method
instead of 'ignorePackages("com.sourceallies.something");'.  The method
'ignorePackages' may ignore classes that you did not intend to ignore.
2. Override environment specific beans in the bootstrap context.  For example
JNDI references are environment specific and need to be overridden.
////////
<jee:jndi-lookup id="testBean" jndi-name="jdbc/test"/>
/////////
A simple way to override this bean is to use Mockito to create mock beans.
//////////
<bean id="testBean" class="org.mockito.Mockito" factory-method="mock">
    <constructor-arg value="com.something.Foo" />
</bean>
//////////
While this worked for most beans, I received errors when I wired up mock DataSources. 
Instead I used embedded databases to replace real DataSources.
/////////////
<jdbc:embedded-database id="dataSource" type="H2"/>
////////////

== License
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