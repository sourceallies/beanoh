== Overview
SpringContextTestCase is a simple way to verify you Spring context.
Teams that leverage this test case spend less time focusing on 
configuring Spring and more time adding business value.  

== Features
1. Verify that all of your beans are wired correctly
2. Reconcile the beans marked with @Component in the classpath 
   with the beans loaded in the Spring context
3. Prevent duplicate bean definition overwriting

== Building
mvn clean install

== Using
1. Verify Spring wiring
////////
public class SomeTest extends SpringContextTestCase {

	@Test
	public void testSomething() {
		assertContextLoading();
	}
}
////////

2. Reconcile @Components
////////
public class SomeTest extends SpringContextTestCase {

	@Test
	public void testSomething() {
		assertComponentsInContext("com.sourceallies");
	}
}
////////

3. Ensure Unique Bean Definitions
////////
public class SomeTest extends UniqueBeanSpringContextTestCase {

	@Test
	public void testSomething() {
		assertContextLoading();
	}
}
////////

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