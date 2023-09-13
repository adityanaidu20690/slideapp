package com.github.ants280.slidegame.logic;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class MoveDirectionTest
{
	@Test
	public void testGetDisplayValue_distinct()
	{
		MoveDirection[] values = MoveDirection.values();

		String[] distinctMoveDirectionDisplayValues
				= Arrays.stream(values)
						.map(MoveDirection::getDisplayValue)
						.distinct()
						.toArray(String[]::new);

		Assert.assertEquals(
				values.length,
				distinctMoveDirectionDisplayValues.length);
	}

	@Test
	public void testGetDxDy_exactlyOneIsZero()
	{
		for (MoveDirection moveDirection : MoveDirection.values())
		{
			int dx = moveDirection.getDx();
			int dy = moveDirection.getDy();

			Assert.assertTrue((dx == 0) ^ (dy == 0));
		}
	}

	@Test
	public void testGetDxDy_unique()
	{
		for (MoveDirection moveDirection : MoveDirection.values())
		{
			int dx = moveDirection.getDx();
			int dy = moveDirection.getDy();

			for (MoveDirection otherMoveDirection : MoveDirection.values())
			{
				if (moveDirection != otherMoveDirection
						&& dx == otherMoveDirection.getDx()
						&& dy == otherMoveDirection.getDy())
				{
					Assert.fail(String.format(
							"MoveDirection.%s and MoveDirection.%s "
							+ "have the same dx and dy (%d and%d)",
							moveDirection,
							otherMoveDirection,
							dx,
							dy));
				}
			}
		}
	}

	@Test
	public void testGetKeyCodes_sameLengthForAllMoveDirections()
	{
		int expectedKeyCodesLength = -1;
		MoveDirection[] moveDirections = MoveDirection.values();
		for (int i = 0; i < moveDirections.length; i++)
		{
			MoveDirection moveDirection = moveDirections[i];

			if (i == 0)
			{
				expectedKeyCodesLength = moveDirection.getKeyCodes().length;
			}
			else
			{
				int keyCodesLength = moveDirection.getKeyCodes().length;

				Assert.assertEquals(expectedKeyCodesLength, keyCodesLength);
			}
		}
	}

	@Test
	public void testFromKeyEvent()
	{
		for (MoveDirection moveDirection : MoveDirection.values())
		{
			for (int keyCode : moveDirection.getKeyCodes())
			{
				KeyEvent keyEvent = mockKeyEvent(keyCode);

				MoveDirection moveDirectionFromKeyCode
						= MoveDirection.fromKeyEvent(keyEvent);

				Assert.assertEquals(moveDirection, moveDirectionFromKeyCode);
			}
		}
	}

	private static KeyEvent mockKeyEvent(int keyCode)
	{
		KeyEvent mockKeyEvent = Mockito.mock(KeyEvent.class);

		Mockito.when(mockKeyEvent.getKeyCode()).thenReturn(keyCode);

		return mockKeyEvent;
	}
}
