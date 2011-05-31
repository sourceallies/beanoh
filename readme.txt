== Overview
Beanoh, pronounced \'beanˌō\, is a simple open source way to verify you Spring context.
Teams that leverage Beanoh spend less time focusing on 
configuring Spring and more time adding business value.  Visit our website at http://beanoh.org.

== Features
1. Verify that all of your beans are wired correctly
2. Reconcile the beans marked with @Component in the classpath 
   with the beans loaded in the Spring context
3. Prevent duplicate bean definition overwriting

=== Verify
Beanoh is a simple way to verify all of the beans in your Spring context.  This lightweight tool 
supplies quick feedback without firing up your container. Beanoh provides three simple 
verification strategies:
1. The first strategy retrieves all of the beans in the Spring context and verifies that they 
are configured correctly.  
2. The second strategy reconciles beans marked with org.springframework.stereotype.Component 
in the classpath with the beans available in the Spring context.  
3. The final strategy verifes that bean definitions within your Spring context are unique.

Beanoh allows you to combine these strategies in a single test.


=== Focus
Beanoh helps you focus on adding busines value.  Switching your focus between coding and 
configuring Spring is distracting.  While configuring Spring is absolutely necessary it 
should not be your primary focus.  Beanoh encourages you to focus on coding while it 
focuses on verifying your context.  Beanoh can help increase your productivity by 
encouraging you to focus on adding business value.

=== Control
Beanoh uncovers Spring configuration errors and supplies timely information.  This tool 
puts your team back in the driver's seat.  It identifies misconfigured beans, components 
that are not scanned by Spring, and duplicate bean definitions.  Beanoh takes the mystery 
out of Spring.  You don't have to be a Spring guru to take control of your Spring context.  
Beanoh does the heavy lifting and empowers you to take control of your Spring context.

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
for a Spring context in the classpath with the same name as the test
plus "-BeanohContext.xml".  For example 'com.sourceallies.anything.SomethingTest'
will use 'com.sourceallies.anything.SomethingTest-BeanohContext.xml' to bootstrap
the Spring context.  Add imports to this bootstrap context for each of the context 
files that you wish to load.  If you need to override beans they must be added to
this bootstrap context in order to not count as a duplicate bean definition.

== Best Practices
1. Ignore classes annotated with @Component explicitly rather than by package.
For example if you run 'assertComponentsInContext("com.sourceallies");' and it
fails on a bean "com.sourceallies.something.Anything" in a dependent jar that
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
<bean id="dataSource" parent="beanohDataSource"/>
////////////

3. Setup system properties in a @BeforeClass method.
//////////
@BeforeClass
public static void setupProperties(){
	System.setProperty("something", "someValue");
}
//////////

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