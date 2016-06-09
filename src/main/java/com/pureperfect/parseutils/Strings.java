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

/**
 * Some random helper methods.
 * 
 * @author J. Chris Folsom
 * @version 0.1
 * @since 0.1
 */
public class Strings
{
	/**
	 * Concatenate a group of objects using the given separator.
	 * 
	 * @param separator
	 *            the separator to use
	 * 
	 * @param args
	 *            the objects to concatenate
	 * @return the concatenated sequence.
	 */
	public static StringBuilder concat(final String separator, final Object... args)
	{
		final StringBuilder results = new StringBuilder(args.length * 16);

		for (int i = 0; i < args.length; ++i)
		{
			results.append(args[i]);

			if (i < args.length - 1)
			{
				results.append(separator);
			}
		}

		return results;
	}

	/**
	 * Trims leading and trailing whitespace.
	 * 
	 * @param value
	 *            the value to trim.
	 * @return the trimmed value or null if value was null
	 */
	public static String trim(final String value)
	{
		return value == null ? value : value.trim();
	}

	/**
	 * Trims leading and trailing whitespace.
	 * 
	 * @param value
	 *            the value to trim.
	 * @return the trimmed value or null if value was null
	 */
	public static StringBuilder trim(final StringBuilder value)
	{
		if (value == null)
		{
			return null;
		}

		while (value.length() > 0 && Character.isWhitespace(value.charAt(0)))
		{
			value.replace(0, 1, "");
		}

		for (int length = value.length(); length > 0 && Character.isWhitespace(value.charAt(length - 1)); length = value
				.length())
		{
			value.replace(length - 1, length, "");
		}

		return value;
	}

	private Strings()
	{
		// Hide constructor only static methods in this class
	}
}