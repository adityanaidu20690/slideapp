package com.github.ants280.slidegame.logic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GridTest
{
	private static final int LENGTH = 4;
	private Grid grid;

	private static final Tile TILE_2 = Tile.TWO;
	private static final Tile TILE_4 = TILE_2.getNext();
	private static final Tile TILE_8 = TILE_4.getNext();
	private static final Tile TILE_16 = TILE_8.getNext();
	private static final Tile TILE_32 = TILE_16.getNext();
	private static final Tile TILE_64 = TILE_32.getNext();
	private static final Tile TILE_128 = TILE_64.getNext();
	private static final Tile TILE_256 = TILE_128.getNext();
	private static final Tile TILE_512 = TILE_256.getNext();
	private static final Tile TILE_1024 = TILE_512.getNext();
	private static final Tile TILE_2048 = TILE_1024.getNext();

	@Before
	public void setUp()
	{
		grid = new Grid(LENGTH);
	}

	@Test
	public void testConstructor_length()
	{
		Grid grid1 = new Grid();

		int length = grid1.getLength();

		Assert.assertEquals(4, length);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_bad_length()
	{
		Grid grid1 = new Grid(1);

		Assert.fail("Should not be constructable: " + grid1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_badGoalTileValue()
	{
		Grid grid2 = new Grid(4, 17);

		Assert.fail("Should not be constructable: " + grid2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_badGoalTileValue2()
	{
		Grid grid3 = new Grid(4, Integer.MIN_VALUE);

		Assert.fail("Should not be constructable: " + grid3);
	}

	@Test
	public void testInitialGridEmpty()
	{
		for (int c = 0; c < LENGTH; c++)
		{
			for (int r = 0; r < LENGTH; r++)
			{
				Assert.assertNull(
						String.format("Non-null tile found at [%d,%d]", r, c),
						grid.getTile(c, r));
			}
		}
	}

	@Test
	public void testAddRandomTileOnEmptyBoard()
	{
		// it should not cause an infinite loop
		grid.addRandomTile();

		boolean tileAdded = false;
		for (int r = 0; r < LENGTH && !tileAdded; r++)
		{
			for (int c = 0; c < LENGTH && !tileAdded; c++)
			{
				final Tile tile = grid.getTile(c, r);
				if (tile != null)
				{
					tileAdded = true;
					Assert.assertTrue(
							"Expected new tile to be 2 or 4 : " + tile,
							tile == TILE_2 || tile == TILE_4);
				}
			}
		}
		Assert.assertTrue(tileAdded);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddRandomTileOnUnSlideableBoard()
	{
		for (int r = 0; r < LENGTH; r++)
		{
			for (int c = 0; c < LENGTH; c++)
			{
				grid.setTile(c, r, TILE_2);
			}
		}

		// it should not cause an infinite loop
		grid.addRandomTile();

		Assert.fail("It should not be possible to add a tile to the grid");
	}

	@Test
	public void testSlideTilesLeft()
	{
		grid.setTile(1, 1, TILE_2);

		grid.slideTiles(MoveDirection.LEFT);

		Assert.assertEquals(TILE_2, grid.getTile(0, 1));
		Assert.assertNull(grid.getTile(1, 1));
	}

	@Test
	public void testSlideTilesRight()
	{
		grid.setTile(1, 1, TILE_2);

		grid.slideTiles(MoveDirection.RIGHT);

		Assert.assertEquals(TILE_2, grid.getTile(3, 1));
		Assert.assertNull(grid.getTile(1, 1));
	}

	@Test
	public void testSlideTilesUp()
	{
		grid.setTile(1, 1, TILE_2);

		grid.slideTiles(MoveDirection.UP);

		Assert.assertEquals(TILE_2, grid.getTile(1, 0));
		Assert.assertNull(grid.getTile(1, 1));
	}

	@Test
	public void testSlideTilesDown()
	{
		grid.setTile(1, 1, TILE_2);

		grid.slideTiles(MoveDirection.DOWN);

		Assert.assertEquals(TILE_2, grid.getTile(1, 3));
		Assert.assertNull(grid.getTile(1, 1));
	}

	@Test
	public void testSlideTiles_0202()
	{
		grid.setTile(1, 0, TILE_2);
		grid.setTile(3, 0, TILE_2);

		int moveScore = grid.slideTiles(MoveDirection.LEFT);

		Assert.assertEquals(4, moveScore);
		Assert.assertEquals(TILE_4, grid.getTile(0, 0));
		Assert.assertNull(grid.getTile(0, 1));
		Assert.assertNull(grid.getTile(0, 3));
	}

	@Test
	public void testSlideTiles_2244()
	{
		grid.setTile(0, 0, TILE_2);
		grid.setTile(1, 0, TILE_2);
		grid.setTile(2, 0, TILE_4);
		grid.setTile(3, 0, TILE_4);

		int moveScore = grid.slideTiles(MoveDirection.LEFT);

		Assert.assertEquals(12, moveScore);
		Assert.assertEquals(TILE_4, grid.getTile(0, 0));
		Assert.assertEquals(TILE_8, grid.getTile(1, 0));
		Assert.assertNull(grid.getTile(0, 2));
		Assert.assertNull(grid.getTile(0, 3));
	}

	@Test
	public void testSlideTiles_22_allRows()
	{
		grid.setTile(0, 0, TILE_2);
		grid.setTile(1, 0, TILE_2);
		grid.setTile(0, 1, TILE_2);
		grid.setTile(1, 1, TILE_2);
		grid.setTile(0, 2, TILE_2);
		grid.setTile(1, 2, TILE_2);
		grid.setTile(0, 3, TILE_2);
		grid.setTile(1, 3, TILE_2);

		int moveScore = grid.slideTiles(MoveDirection.LEFT);

		Assert.assertEquals(16, moveScore);
	}

	@Test
	public void testSlideTilesDown_16s4s()
	{
		grid.setTile(0, 0, TILE_16);
		grid.setTile(0, 1, TILE_16);
		grid.setTile(0, 2, TILE_4);
		grid.setTile(0, 3, TILE_4);

		int moveScore = grid.slideTiles(MoveDirection.DOWN);

		Assert.assertEquals(40, moveScore);
	}

	@Test
	public void testHas2048TILE_empty()
	{
		Assert.assertFalse(grid.goalTileCreated());
	}

	@Test
	public void testHas2048TILE_smallTiles()
	{
		grid.setTile(0, 0, TILE_2);
		grid.setTile(1, 0, TILE_2);

		grid.slideTiles(MoveDirection.LEFT);

		Assert.assertFalse(grid.goalTileCreated());
	}

	@Test
	public void testHas2048TILE_slide1024Tiles()
	{
		grid.setTile(0, 0, TILE_1024);
		grid.setTile(1, 0, TILE_1024);

		grid.slideTiles(MoveDirection.LEFT);

		Assert.assertTrue(grid.goalTileCreated());
	}

	@Test
	public void testCanSlideTilesInAnyDirection_empty()
	{
		Assert.assertFalse(grid.canSlideInAnyDirection());
	}

	@Test
	public void testCanSlideTilesInAnyDirection_oneTile()
	{
		grid.setTile(1, 1, TILE_2);

		Assert.assertTrue(grid.canSlideInAnyDirection());
	}

	@Test
	public void testCanSlideTiles_middle()
	{
		grid.setTile(1, 1, TILE_2);

		Assert.assertTrue(grid.canSlideTiles(MoveDirection.LEFT));
		Assert.assertTrue(grid.canSlideTiles(MoveDirection.RIGHT));
		Assert.assertTrue(grid.canSlideTiles(MoveDirection.UP));
		Assert.assertTrue(grid.canSlideTiles(MoveDirection.DOWN));
	}

	@Test
	public void testCanSlideTiles_topLeft()
	{
		grid.setTile(0, 0, TILE_2);

		Assert.assertFalse(grid.canSlideTiles(MoveDirection.LEFT));
		Assert.assertTrue(grid.canSlideTiles(MoveDirection.RIGHT));
		Assert.assertFalse(grid.canSlideTiles(MoveDirection.UP));
		Assert.assertTrue(grid.canSlideTiles(MoveDirection.DOWN));
	}

	@Test
	public void testCanSlideTiles_combine()
	{
		grid.setTile(0, 0, TILE_2);
		grid.setTile(1, 0, TILE_4);
		grid.setTile(2, 0, TILE_4);
		grid.setTile(3, 0, TILE_8);

		Assert.assertTrue(grid.canSlideTiles(MoveDirection.LEFT));
	}

	@Test
	public void testCanSlideTiles_staggered()
	{
		grid = new Grid(2, 8);
		grid.setTile(0, 0, TILE_2);
		grid.setTile(1, 0, TILE_4);
		grid.setTile(0, 1, TILE_4);
		grid.setTile(1, 1, TILE_2);

		Assert.assertFalse(grid.canSlideInAnyDirection());
	}

	@Test
	public void testCanSlideTiles_staggered_oddLength()
	{
		grid = new Grid(3, 512);
		grid.setTile(0, 0, TILE_2);
		grid.setTile(1, 0, TILE_4);
		grid.setTile(2, 0, TILE_8);

		Assert.assertFalse(grid.canSlideTiles(MoveDirection.LEFT));
		Assert.assertFalse(grid.canSlideTiles(MoveDirection.RIGHT));
	}

	@Test
	public void testClear()
	{
		grid.setTile(1, 1, TILE_2);

		grid.clear();

		Assert.assertFalse(grid.canSlideInAnyDirection());
	}

	@Test
	public void testClearOnWin()
	{
		grid.setTile(0, 0, TILE_1024);
		grid.setTile(1, 0, TILE_1024);

		grid.slideTiles(MoveDirection.LEFT);
		grid.clear();

		Assert.assertFalse(grid.goalTileCreated());
	}

	@Test
	public void testCombine2048Tiles()
	{
		grid.setTile(0, 0, TILE_2048);
		grid.setTile(1, 0, TILE_2048);

		grid.slideTiles(MoveDirection.LEFT);

		Assert.assertEquals(
				4096,
				grid.getTile(0, 0).getValue());
	}

	@Test
	public void testStall()
	{
		grid.setTile(0, 0, TILE_2);
		grid.setTile(1, 0, TILE_2);
		grid.setTile(2, 0, TILE_4);
		grid.setTile(3, 0, TILE_2);
		grid.setTile(0, 1, TILE_8);
		grid.setTile(1, 1, TILE_64);
		grid.setTile(2, 1, TILE_8);
		grid.setTile(3, 1, TILE_16);
		grid.setTile(0, 2, TILE_4);
		grid.setTile(1, 2, TILE_2);
		grid.setTile(2, 2, TILE_32);
		grid.setTile(3, 2, TILE_8);
		grid.setTile(0, 3, TILE_2);
		grid.setTile(1, 3, TILE_2);
		grid.setTile(2, 3, TILE_4);
		grid.setTile(3, 3, TILE_2);

		Assert.assertTrue(grid.isFilled());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetLength_tooSmallForGoalTileValue()
	{
		grid.setLength(2);

		Assert.fail("It should not be possible to set a grid length "
				+ "less than sqrt(log_2(goalTileValue)-1)");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetGoalTileValue_tooLargeForGridLength()
	{
		grid.setGoalTileValue(131072); // 2^17 = 131072

		Assert.fail("It should not be possible to set a grid goal tile value "
				+ "over 2^((gridLength^2)+1");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_invalidArgCombination()
	{
		// stuck state: [[512,256,128],[64,32,16],[8,4,2]]
		// Actually, it is possible to win if the last tile generated
		// is a 4 (not a 2), but that depends on the random tile frequencies.
		Grid gridX = new Grid(3, 1024);

		Assert.fail("It should not be possible to create a grid "
				+ "with length = 3 and goalTileValue = 1024 : " + gridX);
	}

	@Test
	public void testSetLength()
	{
		grid.setTile(0, 0, TILE_1024);
		grid.setTile(1, 0, TILE_1024);
		grid.slideTiles(MoveDirection.LEFT);
		int longerLength = LENGTH + 1;

		grid.setLength(longerLength);
		int length = grid.getLength();
		boolean goalTileCreated = grid.goalTileCreated();

		Assert.assertEquals(longerLength, length);
		Assert.assertFalse(goalTileCreated);
	}

	@Test
	public void testGetGoalTileValue_default()
	{
		int expectedGoalTileValue = 2048;

		int goalTileValue = grid.getGoalTileValue();

		Assert.assertEquals(expectedGoalTileValue, goalTileValue);
	}

	@Test
	public void testSetGoalTileValue()
	{
		grid.setTile(0, 0, TILE_1024);
		grid.setTile(1, 0, TILE_1024);
		grid.slideTiles(MoveDirection.LEFT);
		int largerGoalTileValue = 2 * grid.getGoalTileValue();

		grid.setGoalTileValue(largerGoalTileValue);
		int goalTileValue = grid.getGoalTileValue();
		boolean goalTileCreated = grid.goalTileCreated();

		Assert.assertEquals(largerGoalTileValue, goalTileValue);
		Assert.assertFalse(goalTileCreated);
	}
}
