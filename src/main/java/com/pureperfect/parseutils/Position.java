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
 * Marks the current position in the stream.
 * 
 * @author J. Chris Folsom
 * @version 0.1
 * @since 0.1
 */
public class Position
{
	private int line = 0;

	private int positionInLine = 0;

	private int positionInStream = 0;

	/**
	 * Create a copy of this object.
	 * 
	 * @return a copy of this object.
	 */
	public Position copy()
	{
		final Position copy = new Position();

		copy.line = this.line;
		copy.positionInLine = this.positionInLine;
		copy.positionInStream = this.positionInStream;

		return copy;
	}

	/**
	 * If the {@link Position#getPositionInStream positionInStream} is the same.
	 * 
	 * @return If the {@link Position#getPositionInStream positionInStream} is
	 *         the same.
	 */
	@Override
	public boolean equals(final Object obj)
	{
		return this.hashCode() == obj.hashCode();
	}

	/**
	 * The current line number.
	 * 
	 * @return The current line number.
	 */
	public int getLine()
	{
		return this.line;
	}

	/**
	 * The current position in the current line.
	 * 
	 * @return The current position in the current line.
	 */
	public int getPositionInLine()
	{
		return this.positionInLine;
	}

	/**
	 * The current position in the stream.
	 * 
	 * @return The current position in the stream.
	 */
	public int getPositionInStream()
	{
		return this.positionInStream;
	}

	/**
	 * Same as {@link Position#getPositionInStream positionInStream}.
	 * 
	 * @return Same as {@link Position#getPositionInStream positionInStream}.
	 */
	@Override
	public int hashCode()
	{
		return this.positionInStream;
	}

	void incrementLine()
	{
		++this.line;
		this.positionInLine = 0;
		++this.positionInStream;
	}

	void incrementPositionInLine()
	{
		++this.positionInLine;
		++this.positionInStream;
	}

	@Override
	public String toString()
	{
		final StringBuilder b = new StringBuilder(128);

		b.append("[");
		b.append(this.positionInStream);
		b.append(":");
		b.append(this.line);
		b.append(":");
		b.append(this.positionInLine);
		b.append("]");

		return b.toString();
	}
}