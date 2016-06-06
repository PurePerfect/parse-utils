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
 * Interface for determining whether a character matches a given criteria.
 * 
 * @author J. Chris Folsom
 * @version 0.1
 * @since 0.1
 */
public interface Match
{
	/**
	 * Matches whitespace.
	 */
	public static final Match WHITESPACE = new Match()
	{
		/**
		 * Matches whitespace.
		 * 
		 * @return if the character is whitespace.
		 */
		@Override
		public boolean matches(final char c)
		{
			return Character.isWhitespace(c);
		}
	};

	/**
	 * Matches whitespace except for '\n'.
	 */
	// FIXME change the name of this constant
	public static final Match WHITESPACE_NOTNL = new Match()
	{
		/**
		 * Matches whitespace (except '\n').
		 * 
		 * @return true if the character is whitespace but not a new line.
		 */
		@Override
		public boolean matches(final char c)
		{
			return Character.isWhitespace(c) && c != '\n';
		}
	};

	/**
	 * See if the character matches the given criteria.
	 * 
	 * @param c the
	 *            character to test against the matcher criteria.
	 * 
	 * @return whether or not the character matches the criteria.
	 */
	public boolean matches(char c);
}
