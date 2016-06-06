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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

/**
 * A stream of characters with a lot of useful methods. <b>WARNING:</b> THIS
 * CLASS IS NOT THREAD SAFE!! If you need thread safety use a synchronized
 * wrapper.
 */
public class CharacterStream
{
	/**
	 * The end of file character.
	 */
	public static final char EOF = (char) -1;

	/**
	 * The null character.
	 */
	public static final char NULL = 0;

	private Position currentPosition;

	private final Reader in;

	private Position markedPosition;

	/**
	 * Create a new stream from the given reader.
	 * 
	 * @param in
	 *            the reader
	 */
	public CharacterStream(final Reader in)
	{
		if (!in.markSupported())
		{
			this.in = new BufferedReader(in);
		}
		else
		{
			this.in = in;
		}

		this.currentPosition = new Position();
		this.markedPosition = new Position();
	}

	/**
	 * Close the stream.
	 * 
	 * @throws IOException if there is an error closing the stream.
	 */
	public void close() throws IOException
	{
		this.in.close();
	}

	/**
	 * Get the current position.
	 * 
	 * @return the current positinon.
	 */
	public Position getCurrentPosition()
	{
		return this.currentPosition.copy();
	}

	/**
	 * Get the marked position.
	 * 
	 * @return the marked position.
	 */
	public Position getMarkedPosition()
	{
		return this.markedPosition.copy();
	}

	/**
	 * Determine whether or not we have hit the end of the stream as designated
	 * by the end of file char.
	 * 
	 * @return whether or not we are at the end of the stream
	 * @throws IOException if there is an error reading.
	 */
	public boolean isAtEOF() throws IOException
	{
		return this.peek() == CharacterStream.EOF;
	}

	/**
	 * Determine whether or not we are at a new line.
	 * 
	 * @return whether or not we are at a new line.
	 * @throws IOException if there is an error reading.
	 */
	public boolean isAtNewLine() throws IOException
	{
		final char[] next = this.peek(2);

		return next.length > 1 && next[0] == '\n' || next.length == 2
				&& next[0] == '\r' && next[1] == '\n';
	}

	/**
	 * Mark the current position in the stream.
	 * 
	 * @throws IOException will most likely occur if the underlying i/o resource is not mark supported.
	 */
	public void mark() throws IOException
	{
		this.markedPosition = this.currentPosition.copy();

		this.in.mark(Integer.MAX_VALUE);
	}

	/**
	 * Peek ahead at the next character in the stream.
	 * 
	 * @return the next character in the stream.
	 * @throws IOException if there is an error reading.
	 */
	public char peek() throws IOException
	{
		final char[] peek = this.peek(1);

		return peek.length == 0 ? CharacterStream.EOF : peek[0];
	}

	/**
	 * Peek ahead at a specified number of characters. This method does not read
	 * beyond the end of the stream, so the size of the array returned will not
	 * match the length specified in the parameter if the end of the stream is
	 * reaced.
	 * 
	 * @param count
	 *            the number of characters to peek at
	 * @return an array of characters
	 * @throws IOException if there is an error reading.
	 */
	public char[] peek(final int count) throws IOException
	{
		/*
		 * We're going to mark so we can read ahead and reset.
		 */
		this.in.mark(count + 2);

		final char[] results = new char[count];

		this.in.read(results);

		this.in.reset();

		return this.trim(results);
	}

	/**
	 * Determine whether or not the given characters match the next characters
	 * in the stream.
	 * 
	 * @param chars
	 *            the characters to match
	 * @return whether or not they match
	 * @throws IOException if there is an error reading.
	 */
	public boolean peekAndMatch(final String chars) throws IOException
	{
		return String.valueOf(this.peek(chars.length())).equals(chars);
	}

	/**
	 * Read the next character in the stream.
	 * 
	 * @return the next character in the stream.
	 * 
	 * @throws IOException if there is an error reading.
	 */
	public char read() throws IOException
	{
		final char c = (char) this.in.read();

		if (c == CharacterStream.EOF)
		{
			// Don't update position if we are at end of file
			return c;
		}

		if (c == '\n')
		{
			this.currentPosition.incrementLine();
		}
		else
		{
			this.currentPosition.incrementPositionInLine();
		}

		return c;
	}

	/**
	 * Reset current position to the marked position.
	 * 
	 * @throws IOException if there is an error reading.
	 */
	public void reset() throws IOException
	{
		this.currentPosition = this.markedPosition.copy();

		this.in.reset();
	}

	/**
	 * Skip characters.
	 * 
	 * @param count
	 *            the number of characters to skip
	 * @throws IOException if there is an error reading.
	 */
	public void skip(final int count) throws IOException
	{
		for (int i = 0; i < count; ++i)
		{
			this.read();
		}
	}

	/**
	 * Read and skip as long as the given matcher matches the next character in
	 * the stream.
	 * 
	 * @param m
	 *            the matcher to use
	 * @return the number of characters skipped
	 * @throws IOException if there is an error reading.
	 */
	public int skip(final Match m) throws IOException
	{
		int count = 0;

		for (char c = this.peek(); m.matches(c); c = this.peek())
		{
			this.read();
			count++;
		}

		return count;
	}

	/*
	 * Eliminate trailing null chars caused by reading past end of reader.
	 */
	private char[] trim(final char[] actual)
	{
		int stop = actual.length;

		while (stop > 0)
		{
			final char c = actual[stop - 1];

			if (c != CharacterStream.NULL)
			{
				break;
			}

			stop--;
		}

		return Arrays.copyOf(actual, stop);
	}
}