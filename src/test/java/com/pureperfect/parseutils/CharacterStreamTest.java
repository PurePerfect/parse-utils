/*
 * Copyright [2008] PurePerfect.com Licensed under the Apache License, Version
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

import java.io.IOException;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author J. Chris Folsom
 * @version 0.1
 * @since 0.1
 */
public class CharacterStreamTest
{
	public void assertPosition(final Position p, final int positionInStream,
			final int lineNumber, final int positionInLine)
	{
		Assert.assertEquals(positionInStream, p.getPositionInStream());
		Assert.assertEquals(lineNumber, p.getLine());
		Assert.assertEquals(positionInLine, p.getPositionInLine());
	}

	@Test
	public void getCurrentPosition() throws IOException
	{
		final StringReader testData = new StringReader(
				"this is a test\n string it is awesome because I say so");

		final CharacterStream in = new CharacterStream(testData);

		Position p = in.getCurrentPosition();

		Assert.assertEquals(0, p.getLine());
		Assert.assertEquals(0, p.getPositionInStream());
		Assert.assertEquals(0, p.getPositionInLine());

		in.read();
		in.read();
		in.read();
		in.read();

		p = in.getCurrentPosition();

		Assert.assertEquals(0, p.getLine());
		Assert.assertEquals(4, p.getPositionInLine());
		Assert.assertEquals(4, p.getPositionInStream());
		Assert.assertEquals(' ', in.read());
		Assert.assertEquals(5, in.getCurrentPosition().getPositionInStream());

		// Twelve more chars
		in.read();
		in.read();
		in.read();
		in.read();
		in.read();
		in.read();
		in.read();
		in.read();
		in.read();
		in.read();
		in.read();
		in.read();

		// The 't' in string
		Assert.assertEquals('t', in.read());
		final Position p2 = in.getCurrentPosition();

		Assert.assertEquals(1, p2.getLine());
		Assert.assertEquals(3, p2.getPositionInLine());
		Assert.assertEquals(18, p2.getPositionInStream());
	}

	@Test
	public void getCurrentPositionIsCopy()
	{
		final StringReader testData = new StringReader("this is a test");

		final CharacterStream in = new CharacterStream(testData);

		Assert.assertEquals(in.getCurrentPosition(), in.getCurrentPosition());
		Assert.assertFalse(in.getCurrentPosition() == in.getCurrentPosition());

		Assert.assertEquals(in.getCurrentPosition(), in.getCurrentPosition());
		Assert.assertFalse(in.getCurrentPosition() == in.getCurrentPosition());
	}

	@Test
	public void getMarkedPositionIsCopy()
	{
		final StringReader testData = new StringReader("this is a test");

		final CharacterStream in = new CharacterStream(testData);

		Assert.assertEquals(in.getMarkedPosition(), in.getMarkedPosition());
		Assert.assertFalse(in.getMarkedPosition() == in.getMarkedPosition());

		Assert.assertEquals(in.getMarkedPosition(), in.getMarkedPosition());
		Assert.assertFalse(in.getMarkedPosition() == in.getMarkedPosition());
	}

	@Test
	public void isAtNewLineOnCarriageReturnNewline() throws IOException
	{
		final StringReader testData = new StringReader("\r\nmorestuff");

		final CharacterStream in = new CharacterStream(testData);

		Assert.assertTrue(in.isAtNewLine());
	}

	@Test
	public void isAtNewLineOnNewlineOnly() throws IOException
	{
		final StringReader testData = new StringReader("\nasdfs");

		final CharacterStream in = new CharacterStream(testData);

		Assert.assertTrue(in.isAtNewLine());
	}

	@Test
	public void isAtNewLineOnNonNewline() throws IOException
	{
		final StringReader testData = new StringReader("\rmorestuff");

		final CharacterStream in = new CharacterStream(testData);

		Assert.assertFalse(in.isAtNewLine());
	}

	@Test
	public void markAgainOverwritesPreviousMark() throws IOException
	{
		final StringReader testData = new StringReader(
				"this is a test\n string it is awesome because I say so");

		final CharacterStream in = new CharacterStream(testData);

		in.read();
		in.read();
		in.read();
		in.read();
		in.read();

		in.mark();

		Assert.assertEquals(5, in.getMarkedPosition().getPositionInStream());
		Assert.assertEquals(5, in.getCurrentPosition().getPositionInStream());
		Assert.assertNotSame(in.getCurrentPosition(), in.getMarkedPosition());

		in.read();
		in.read();
		in.read();

		Assert.assertEquals(5, in.getMarkedPosition().getPositionInStream());
		Assert.assertEquals(8, in.getCurrentPosition().getPositionInStream());
		Assert.assertNotSame(in.getCurrentPosition(), in.getMarkedPosition());

		in.mark();

		Assert.assertEquals(8, in.getMarkedPosition().getPositionInStream());
		Assert.assertEquals(8, in.getCurrentPosition().getPositionInStream());
		Assert.assertNotSame(in.getCurrentPosition(), in.getMarkedPosition());

		in.read();
		in.read();
		in.read();
		in.read();

		in.reset();

		Assert.assertEquals(8, in.getMarkedPosition().getPositionInStream());
		Assert.assertEquals(8, in.getCurrentPosition().getPositionInStream());
		Assert.assertNotSame(in.getCurrentPosition(), in.getMarkedPosition());

		Assert.assertTrue(in.peekAndMatch("a test"));
	}

	@Test
	public void markResetsToZero() throws IOException
	{
		final StringReader testData = new StringReader(
				"this is a test\n string it is awesome because I say so");

		final CharacterStream in = new CharacterStream(testData);

		/*
		 * Start of the stream mark and current positions should be equal.
		 */
		Assert.assertEquals(in.getCurrentPosition(), in.getMarkedPosition());

		/*
		 * They should not be equal anymore.
		 */
		for (int i = 0; i < 5; ++i)
		{
			in.read();
		}

		Assert.assertNotSame(in.getCurrentPosition(), in.getMarkedPosition());

		// It should still be at zero
		Position mark = in.getMarkedPosition();

		Assert.assertEquals(0, mark.getLine());
		Assert.assertEquals(0, mark.getPositionInLine());
		Assert.assertEquals(0, mark.getPositionInStream());

		/*
		 * the first five characters should be "this ".
		 */
		in.reset();

		// Marked position should still be zero after reset.
		mark = in.getMarkedPosition();

		Assert.assertEquals(0, mark.getLine());
		Assert.assertEquals(0, mark.getPositionInLine());
		Assert.assertEquals(0, mark.getPositionInStream());

		// Current position should be zero as well
		Position current = in.getCurrentPosition();

		Assert.assertEquals(0, current.getLine());
		Assert.assertEquals(0, current.getPositionInLine());
		Assert.assertEquals(0, current.getPositionInStream());

		// The two should be equal
		Assert.assertEquals(current, mark);

		// The characters should match
		Assert.assertEquals('t', in.read());
		Assert.assertEquals('h', in.read());
		Assert.assertEquals('i', in.read());
		Assert.assertEquals('s', in.read());
		Assert.assertEquals(' ', in.read());

		/*
		 * Make sure successive calls work as expected.
		 */
		in.reset();

		mark = in.getMarkedPosition();

		Assert.assertEquals(0, mark.getLine());
		Assert.assertEquals(0, mark.getPositionInLine());
		Assert.assertEquals(0, mark.getPositionInStream());

		// Current position should be zero as well
		current = in.getCurrentPosition();

		Assert.assertEquals(0, current.getLine());
		Assert.assertEquals(0, current.getPositionInLine());
		Assert.assertEquals(0, current.getPositionInStream());

		Assert.assertEquals('t', in.read());
		Assert.assertEquals('h', in.read());
		Assert.assertEquals('i', in.read());
		Assert.assertEquals('s', in.read());
		Assert.assertEquals(' ', in.read());

		// Try it a third time just to make sure
		in.reset();

		mark = in.getMarkedPosition();

		Assert.assertEquals(0, mark.getLine());
		Assert.assertEquals(0, mark.getPositionInLine());
		Assert.assertEquals(0, mark.getPositionInStream());

		// Current position should be zero as well
		current = in.getCurrentPosition();

		Assert.assertEquals(0, current.getLine());
		Assert.assertEquals(0, current.getPositionInLine());
		Assert.assertEquals(0, current.getPositionInStream());

		Assert.assertEquals('t', in.read());
		Assert.assertEquals('h', in.read());
		Assert.assertEquals('i', in.read());
		Assert.assertEquals('s', in.read());
		Assert.assertEquals(' ', in.read());
	}

	@Test
	public void markStartsAtZero() throws IOException
	{
		final StringReader testData = new StringReader(
				"this is a test\n string it is awesome because I say so");

		final CharacterStream in = new CharacterStream(testData);

		/*
		 * Start of the stream mark and current positions should be equal.
		 */
		Assert.assertEquals(in.getCurrentPosition(), in.getMarkedPosition());

		/*
		 * They should not be equal anymore.
		 */
		for (int i = 0; i < 5; ++i)
		{
			in.read();
		}

		Assert.assertNotSame(in.getCurrentPosition(), in.getMarkedPosition());

		// It should still be at zero
		final Position mark = in.getMarkedPosition();

		Assert.assertEquals(0, mark.getLine());
		Assert.assertEquals(0, mark.getPositionInLine());
		Assert.assertEquals(0, mark.getPositionInStream());
	}

	@Test
	public void midStreamMarkAndReset() throws IOException
	{
		final StringReader testData = new StringReader(
				"this is a test\n string it is awesome because I say so");

		final CharacterStream in = new CharacterStream(testData);

		in.read();
		in.read();
		in.read();
		in.read();
		in.read();

		in.mark();

		Assert.assertEquals(5, in.getMarkedPosition().getPositionInStream());
		Assert.assertEquals(5, in.getCurrentPosition().getPositionInStream());
		Assert.assertNotSame(in.getCurrentPosition(), in.getMarkedPosition());

		in.read();
		in.read();
		in.read();

		Assert.assertEquals(5, in.getMarkedPosition().getPositionInStream());
		Assert.assertEquals(8, in.getCurrentPosition().getPositionInStream());
		Assert.assertNotSame(in.getCurrentPosition(), in.getMarkedPosition());

		in.reset();

		Assert.assertEquals(5, in.getMarkedPosition().getPositionInStream());
		Assert.assertEquals(5, in.getCurrentPosition().getPositionInStream());
		Assert.assertNotSame(in.getCurrentPosition(), in.getMarkedPosition());

		Assert.assertTrue(in.peekAndMatch("is a test"));
	}

	@Test
	public void peek() throws IOException
	{
		final StringReader testData = new StringReader("this is a test");

		final CharacterStream in = new CharacterStream(testData);

		in.read();
		in.read();
		in.read();

		/*
		 * Make sure peek does not affect current or marked positions
		 */
		final Position current = in.getCurrentPosition();
		final Position mark = in.getMarkedPosition();

		final char[] peek = in.peek(11);

		Assert.assertEquals('s', peek[0]);
		Assert.assertEquals(' ', peek[1]);
		Assert.assertEquals('i', peek[2]);
		Assert.assertEquals('s', peek[3]);
		Assert.assertEquals(' ', peek[4]);
		Assert.assertEquals('a', peek[5]);
		Assert.assertEquals(' ', peek[6]);
		Assert.assertEquals('t', peek[7]);
		Assert.assertEquals('e', peek[8]);
		Assert.assertEquals('s', peek[9]);
		Assert.assertEquals('t', peek[10]);

		Assert.assertEquals(current, in.getCurrentPosition());
		Assert.assertEquals(mark, in.getMarkedPosition());

		// We should still be able to read those chars
		Assert.assertEquals('s', in.read());
		Assert.assertEquals(' ', in.read());
		Assert.assertEquals('i', in.read());
		Assert.assertEquals('s', in.read());
		Assert.assertEquals(' ', in.read());
		Assert.assertEquals('a', in.read());
		Assert.assertEquals(' ', in.read());
		Assert.assertEquals('t', in.read());
		Assert.assertEquals('e', in.read());
		Assert.assertEquals('s', in.read());
		Assert.assertEquals('t', in.read());
	}

	@Test
	public void peekAndMatch() throws IOException
	{
		final StringReader testData = new StringReader("this is a test");

		final CharacterStream in = new CharacterStream(testData);

		Assert.assertFalse(in.peekAndMatch("badtext"));
		Assert.assertTrue(in.peekAndMatch("this is a test"));

		Assert.assertTrue(in.peekAndMatch("this is"));
		Assert.assertTrue(in.peekAndMatch("this"));

		this.assertPosition(in.getCurrentPosition(), 0, 0, 0);

		in.read();

		Assert.assertFalse(in.peekAndMatch("this is a test"));
		Assert.assertTrue(in.peekAndMatch("his is a test"));

		this.assertPosition(in.getCurrentPosition(), 1, 0, 1);
	}

	@Test
	public void peekAndMatchBeyondEndOfStream() throws IOException
	{
		final StringReader testData = new StringReader("this is a test");

		final CharacterStream in = new CharacterStream(testData);

		for (int i = 0; i < 30; i++)
		{
			in.read();
		}

		Assert.assertFalse(in.peekAndMatch("badtext"));
		Assert.assertFalse(in.peekAndMatch("this is a test"));

		Assert.assertEquals(CharacterStream.EOF, in.read());
		Assert.assertEquals(CharacterStream.EOF, in.read());
		Assert.assertEquals(CharacterStream.EOF, in.read());
	}

	@Test
	public void peekMultiplePastEndOfStream() throws IOException
	{
		final StringReader testData = new StringReader("this is a test");

		final CharacterStream in = new CharacterStream(testData);

		in.read();
		in.read();
		in.read();

		/*
		 * Make sure peek does not affect current or marked positions
		 */
		final Position current = in.getCurrentPosition();
		final Position mark = in.getMarkedPosition();

		final char[] peek = in.peek(15);

		// Should stop at end of stream and not return a 15 char array
		Assert.assertEquals(11, peek.length);

		Assert.assertEquals('s', peek[0]);
		Assert.assertEquals(' ', peek[1]);
		Assert.assertEquals('i', peek[2]);
		Assert.assertEquals('s', peek[3]);
		Assert.assertEquals(' ', peek[4]);
		Assert.assertEquals('a', peek[5]);
		Assert.assertEquals(' ', peek[6]);
		Assert.assertEquals('t', peek[7]);
		Assert.assertEquals('e', peek[8]);
		Assert.assertEquals('s', peek[9]);
		Assert.assertEquals('t', peek[10]);

		Assert.assertEquals(current, in.getCurrentPosition());
		Assert.assertEquals(mark, in.getMarkedPosition());

		// We should still be able to read those chars
		Assert.assertEquals('s', in.read());
		Assert.assertEquals(' ', in.read());
		Assert.assertEquals('i', in.read());
		Assert.assertEquals('s', in.read());
		Assert.assertEquals(' ', in.read());
		Assert.assertEquals('a', in.read());
		Assert.assertEquals(' ', in.read());
		Assert.assertEquals('t', in.read());
		Assert.assertEquals('e', in.read());
		Assert.assertEquals('s', in.read());
		Assert.assertEquals('t', in.read());
	}

	@Test
	public void peekSingleReturnsEOFWhenAtEndOfStream() throws IOException
	{
		final StringReader testData = new StringReader("");

		final CharacterStream in = new CharacterStream(testData);

		Assert.assertEquals(CharacterStream.EOF, in.peek());
	}

	@Test
	public void peekSingleReturnsProperCharWhenNotAtEndOfStream()
			throws IOException
	{
		final StringReader testData = new StringReader("u");

		final CharacterStream in = new CharacterStream(testData);

		Assert.assertEquals('u', in.peek());
	}

	@Test
	public void readPastEndOfStream() throws IOException
	{
		final StringReader testData = new StringReader("this is");

		final CharacterStream in = new CharacterStream(testData);

		Assert.assertEquals('t', in.read());
		Assert.assertEquals('h', in.read());
		Assert.assertEquals('i', in.read());
		Assert.assertEquals('s', in.read());
		Assert.assertEquals(' ', in.read());
		Assert.assertEquals('i', in.read());
		Assert.assertEquals('s', in.read());

		/*
		 * Subsequent reads should return EOF forever and should not increment
		 * the position in the line, the line number or the position in the
		 * stream.
		 */
		final Position current = in.getCurrentPosition();

		Assert.assertEquals(CharacterStream.EOF, in.read());
		Assert.assertEquals(CharacterStream.EOF, in.read());
		Assert.assertEquals(CharacterStream.EOF, in.read());
		Assert.assertEquals(CharacterStream.EOF, in.read());

		Assert.assertEquals(current, in.getCurrentPosition());

		Assert.assertEquals(CharacterStream.EOF, in.read());
		Assert.assertEquals(CharacterStream.EOF, in.read());
		Assert.assertEquals(CharacterStream.EOF, in.read());

		Assert.assertEquals(current, in.getCurrentPosition());

		Assert.assertEquals(CharacterStream.EOF, in.read());
		Assert.assertEquals(CharacterStream.EOF, in.read());

		Assert.assertEquals(current, in.getCurrentPosition());
	}

	@Test
	public void skip() throws IOException
	{
		final StringReader testData = new StringReader(
				"this is a test\n string it is awesome because I say so");

		final CharacterStream in = new CharacterStream(testData);

		in.read();
		in.read();
		in.read();

		in.skip(7);

		Assert.assertEquals('t', in.read());
		Assert.assertEquals('e', in.read());
		Assert.assertEquals('s', in.read());

		Position current = in.getCurrentPosition();

		Assert.assertEquals(0, current.getLine());
		Assert.assertEquals(13, current.getPositionInLine());
		Assert.assertEquals(13, current.getPositionInStream());

		in.skip(6);

		Assert.assertEquals('i', in.read());
		Assert.assertEquals('n', in.read());
		Assert.assertEquals('g', in.read());

		current = in.getCurrentPosition();

		Assert.assertEquals(1, current.getLine());
		Assert.assertEquals(7, current.getPositionInLine());
		Assert.assertEquals(22, current.getPositionInStream());
	}

	@Test
	public void skipPastEnd() throws IOException
	{
		final StringReader testData = new StringReader("this is a tes");

		final CharacterStream in = new CharacterStream(testData);

		in.read();
		in.read();
		in.read();

		in.skip(70000);

		/*
		 * Skip should not update position past end of file.
		 */
		final Position current = in.getCurrentPosition();

		Assert.assertEquals(0, current.getLine());
		Assert.assertEquals(13, current.getPositionInLine());
		Assert.assertEquals(13, current.getPositionInStream());

		// Should be able to read EOF forever
		Assert.assertEquals(CharacterStream.EOF, in.read());
		Assert.assertEquals(CharacterStream.EOF, in.read());
		Assert.assertEquals(CharacterStream.EOF, in.read());
		Assert.assertEquals(CharacterStream.EOF, in.read());
		Assert.assertEquals(CharacterStream.EOF, in.read());
	}

	@Test
	public void skipWhiteSpacePastEndOfStream() throws IOException
	{
		final StringReader testData = new StringReader("this is a test");

		final CharacterStream in = new CharacterStream(testData);

		in.skip(13);

		Assert.assertEquals('t', in.read());

		in.skip(Match.WHITESPACE);

		Assert.assertEquals(CharacterStream.EOF, in.read());
		Assert.assertEquals(CharacterStream.EOF, in.read());
		Assert.assertEquals(CharacterStream.EOF, in.read());

		this.assertPosition(in.getCurrentPosition(), 14, 0, 14);

		in.skip(60);

		in.skip(Match.WHITESPACE);

		Assert.assertEquals(CharacterStream.EOF, in.read());
		Assert.assertEquals(CharacterStream.EOF, in.read());
		Assert.assertEquals(CharacterStream.EOF, in.read());

		this.assertPosition(in.getCurrentPosition(), 14, 0, 14);
	}

	@Test
	public void skipWithMatcher() throws IOException
	{
		final StringReader testData = new StringReader(
				"this is a test \n it handles \r \t white spaces");

		final CharacterStream in = new CharacterStream(testData);

		Assert.assertEquals(0, in.skip(Match.WHITESPACE));

		Assert.assertEquals('t', in.read());
		Assert.assertEquals('h', in.read());
		Assert.assertEquals('i', in.read());

		Assert.assertEquals(0, in.skip(Match.WHITESPACE));

		this.assertPosition(in.getCurrentPosition(), 3, 0, 3);

		Assert.assertEquals('s', in.read());

		Assert.assertEquals(1, in.skip(Match.WHITESPACE));

		Assert.assertEquals('i', in.read());

		Assert.assertEquals(0, in.skip(Match.WHITESPACE));

		this.assertPosition(in.getCurrentPosition(), 6, 0, 6);

		Assert.assertEquals('s', in.read());
		Assert.assertEquals(' ', in.read());
		Assert.assertEquals('a', in.read());

		Assert.assertEquals(1, in.skip(Match.WHITESPACE));

		this.assertPosition(in.getCurrentPosition(), 10, 0, 10);

		Assert.assertEquals('t', in.read());
		Assert.assertEquals('e', in.read());
		Assert.assertEquals('s', in.read());

		Assert.assertEquals(0, in.skip(Match.WHITESPACE));

		Assert.assertEquals('t', in.read());

		this.assertPosition(in.getCurrentPosition(), 14, 0, 14);

		Assert.assertEquals(3, in.skip(Match.WHITESPACE));

		Assert.assertEquals('i', in.read());

		this.assertPosition(in.getCurrentPosition(), 18, 1, 2);

		Assert.assertEquals('t', in.read());

		Assert.assertEquals(1, in.skip(Match.WHITESPACE));

		Assert.assertEquals('h', in.read());
		Assert.assertEquals('a', in.read());
		Assert.assertEquals('n', in.read());
		Assert.assertEquals('d', in.read());

		Assert.assertEquals(0, in.skip(Match.WHITESPACE));

		Assert.assertEquals('l', in.read());
		Assert.assertEquals('e', in.read());
		Assert.assertEquals('s', in.read());

		Assert.assertEquals(5, in.skip(Match.WHITESPACE));

		Assert.assertEquals('w', in.read());
	}
}
