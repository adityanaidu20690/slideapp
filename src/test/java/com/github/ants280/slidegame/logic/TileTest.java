package com.github.ants280.slidegame.logic;

import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.Assert;
import org.junit.Test;

public class TileTest
{
	@Test
	public void testGetDisplayValue_distinct()
	{
		Tile[] tiles = Stream.iterate(Tile.TWO, Tile::getNext)
				.limit(11)
				.toArray(Tile[]::new);

		String[] distinctTileDisplayValues
				= Arrays.stream(tiles)
						.map(Tile::getDisplayValue)
						.distinct()
						.toArray(String[]::new);

		Assert.assertEquals(
				tiles.length,
				distinctTileDisplayValues.length);
		// [ensure the last tile is 2048] :
		Assert.assertEquals(
				2048,
				tiles[tiles.length - 1].getValue());
	}

	@Test
	public void testHashCode_same()
	{
		Tile tile1 = Tile.TWO;
		Tile tile2 = tile1;

		int hashCode1 = tile1.hashCode();
		int hashCode2 = tile2.hashCode();

		Assert.assertEquals(hashCode1, hashCode2);
	}

	@Test
	public void testHashCode_different()
	{
		Tile tile1 = Tile.TWO;
		Tile tile2 = tile1.getNext();

		int hashCode1 = tile1.hashCode();
		int hashCode2 = tile2.hashCode();

		Assert.assertNotEquals(hashCode1, hashCode2);
	}

	@Test
	public void testEquals_same()
	{
		Tile tile1 = Tile.TWO;
		Tile tile2 = tile1;

		boolean equals = tile1.equals(tile2);

		Assert.assertTrue(equals);
	}

	@Test
	public void testEquals_eq()
	{
		Tile tile1 = new Tile(2);
		Tile tile2 = new Tile(2);

		boolean equals = tile1.equals(tile2);

		Assert.assertTrue(equals);
	}

	@Test
	public void testEquals_different()
	{
		Tile tile1 = Tile.TWO;
		Tile tile2 = tile1.getNext();

		boolean equals = tile1.equals(tile2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_different_null()
	{
		Tile tile1 = Tile.TWO;
		Tile tile2 = null;

		boolean equals = tile1.equals(tile2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testEquals_different_otherObject()
	{
		Object tile1 = Tile.TWO;
		Object tile2 = 2;

		boolean equals = tile1.equals(tile2);

		Assert.assertFalse(equals);
	}

	@Test
	public void testCompareTo_eq()
	{
		Tile tile1 = Tile.TWO;
		Tile tile2 = tile1;

		int compareTo = tile1.compareTo(tile2);

		Assert.assertEquals(0, compareTo);
	}

	@Test
	public void testCompareTo_lt()
	{
		Tile tile1 = Tile.TWO;
		Tile tile2 = tile1.getNext();

		int compareTo = tile1.compareTo(tile2);

		Assert.assertTrue(compareTo < 0);
	}

	@Test
	public void testCompareTo_gt()
	{
		Tile tile1 = Tile.TWO;
		Tile tile2 = tile1.getNext();

		int compareTo = tile2.compareTo(tile1);

		Assert.assertTrue(compareTo > 0);
	}

	@Test
	public void testToString()
	{
		Tile tile1 = Tile.TWO;

		String toString = tile1.toString();

		Assert.assertNotNull(toString);
	}
}
