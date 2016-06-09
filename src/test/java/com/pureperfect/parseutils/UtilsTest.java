/*
 * Copyright [2014] PurePerfect.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License.
 * 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pureperfect.parseutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * 
 * @author J. Chris Folsom
 * @version 0.1
 * @since 0.1
 */
public class UtilsTest
{
	@Test
	public void concat()
	{
		assertEquals("onetwothree",
				Strings.concat("", "one", "two", "three").toString());
	}

	@Test
	public void concatWithSeparator()
	{
		assertEquals("one, two, three",
				Strings.concat(", ", "one", "two", "three").toString());
	}

	@Test
	public void trimString()
	{
		assertEquals("asdf", Strings.trim(" asdf "));

		// Need this reference to make sure trim string method is called
		final String s = null;

		assertNull(Strings.trim(s));
	}

	@Test
	public void trimStringBuilder()
	{
		assertEquals("asdf", Strings.trim(new StringBuilder(" asdf "))
				.toString());

		// Need this reference to make sure correct trim method is called
		final StringBuilder s = null;

		assertNull(Strings.trim(s));
		
		assertEquals("", Strings.trim(new StringBuilder("")).toString());
		
		assertEquals("", Strings.trim(new StringBuilder("   ")).toString());
	}
}
