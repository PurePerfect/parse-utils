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

import junit.framework.Assert;

import org.junit.Test;

/**
 * 
 * @author J. Chris Folsom
 * @version 0.1
 * @since 0.1
 */
public class PositionTest
{
	@Test
	public void testConstructor()
	{
		final Position p = new Position();

		Assert.assertEquals(0, p.getLine());
		Assert.assertEquals(0, p.getPositionInLine());
		Assert.assertEquals(0, p.getPositionInStream());
	}

	@Test
	public void testCopy()
	{
		final Position p = new Position();

		p.incrementPositionInLine();
		p.incrementLine();
		p.incrementPositionInLine();
		p.incrementPositionInLine();

		Assert.assertEquals(1, p.getLine());
		Assert.assertEquals(2, p.getPositionInLine());
		Assert.assertEquals(4, p.getPositionInStream());

		final Position copy = p.copy();

		Assert.assertEquals(1, copy.getLine());
		Assert.assertEquals(2, copy.getPositionInLine());
		Assert.assertEquals(4, copy.getPositionInStream());

		Assert.assertEquals(p.hashCode(), copy.hashCode());
		Assert.assertEquals(p, copy);
		Assert.assertFalse(p == copy);
	}

	@Test
	public void testEquals()
	{
		// Same position in stream
		final Position p = new Position();

		p.incrementPositionInLine();
		p.incrementLine();
		p.incrementPositionInLine();
		p.incrementPositionInLine();

		final Position p2 = new Position();

		p2.incrementPositionInLine();
		p2.incrementLine();
		p2.incrementPositionInLine();
		p2.incrementPositionInLine();

		Assert.assertEquals(p, p2);

		// Different position in stream
		p2.incrementLine();

		Assert.assertNotSame(p, p2);
	}

	@Test
	public void testHashCode()
	{
		final Position p = new Position();

		p.incrementPositionInLine();
		p.incrementLine();
		p.incrementPositionInLine();
		p.incrementPositionInLine();

		Assert.assertEquals(4, p.hashCode());
	}

	@Test
	public void testIncrementLine()
	{
		final Position p = new Position();

		p.incrementPositionInLine();
		p.incrementPositionInLine();
		p.incrementPositionInLine();

		Assert.assertEquals(3, p.getPositionInStream());

		p.incrementLine();

		Assert.assertEquals(4, p.getPositionInStream());
		Assert.assertEquals(1, p.getLine());
		Assert.assertEquals(0, p.getPositionInLine());
	}

	@Test
	public void testIncrementPositionInLine()
	{
		final Position p = new Position();

		p.incrementPositionInLine();
		p.incrementPositionInLine();

		Assert.assertEquals(0, p.getLine());
		Assert.assertEquals(2, p.getPositionInLine());
		Assert.assertEquals(2, p.getPositionInStream());
	}

	@Test
	public void testToString()
	{
		final Position p = new Position();

		p.incrementPositionInLine();
		p.incrementLine();
		p.incrementPositionInLine();
		p.incrementPositionInLine();

		Assert.assertEquals("[4:1:2]", p.toString());
	}
}
