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

package com.sourceallies.beanoh.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A utility class that formats a list of messages for an error message.
 * 
 * @author David Kessler
 * 
 */
public class MessageUtil {

	/**
	 * Separates messages on different lines for error messages.
	 * 
	 * @param messages
	 *            a list of messages to be formatted
	 * @return a String containing a each message on a seperate line
	 */
	public String list(List<String> messages) {
		List<String> sortedComponents = new ArrayList<String>(messages);
		Collections.sort(sortedComponents);
		String output = "";
		for (String component : sortedComponents) {
			output += "\n" + component;
		}
		return output;
	}

}
