package com.github.ants280.slidegame.logic;

import java.awt.event.MouseEvent;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

@RunWith(Parameterized.class)
public class MoveDirectionFromMouseEventsTest
{
	private final MouseEvent startMouseEvent;
	private final MouseEvent endMouseEvent;
	private final MoveDirection expectedMoveDirection;

	public MoveDirectionFromMouseEventsTest(
			MouseEvent startMouseEvent,
			MouseEvent endMouseEvent,
			MoveDirection expectedMoveDirection)
	{
		this.startMouseEvent = startMouseEvent;
		this.endMouseEvent = endMouseEvent;
		this.expectedMoveDirection = expectedMoveDirection;
	}

	@Test
	public void test()
	{
		MoveDirection actualMoveDirection
				= MoveDirection.fromMouseEvents(startMouseEvent, endMouseEvent);

		Assert.assertEquals(expectedMoveDirection, actualMoveDirection);
	}

	@Parameterized.Parameters(
			name = "{index}: start[{0}] end[{1}]")
	public static Iterable<Object[]> data()
	{
		return Arrays.asList(
				// simple cases:
				createTestCase(0, 0, -1, 0, MoveDirection.LEFT),
				createTestCase(0, 0, 1, 0, MoveDirection.RIGHT),
				createTestCase(0, 0, 0, -1, MoveDirection.UP),
				createTestCase(0, 0, 0, 1, MoveDirection.DOWN),
				// The greater delta value should be used:
				createTestCase(10, 5, 0, 0, MoveDirection.LEFT),
				createTestCase(1, 2, 3, 3, MoveDirection.RIGHT),
				createTestCase(42, 17, 51, 7, MoveDirection.UP),
				createTestCase(19, 123, 5, 217, MoveDirection.DOWN),
				// if the dx and dy are equal, expect null:
				createTestCase(5, 7, 5, 7, null),
				createTestCase(0, 0, 0, 0, null));
	}

	private static Object[] createTestCase(
			int startX,
			int startY,
			int endX,
			int endY,
			MoveDirection expectedMoveDirection)
	{
		return new Object[]
		{
			mockMouseEvent(startX, startY),
			mockMouseEvent(endX, endY),
			expectedMoveDirection
		};
	}

	private static MouseEvent mockMouseEvent(int x, int y)
	{
		MouseEvent mockMouseEvent = Mockito.mock(MouseEvent.class);

		Mockito.when(mockMouseEvent.getX()).thenReturn(x);
		Mockito.when(mockMouseEvent.getY()).thenReturn(y);
		Mockito.when(mockMouseEvent.toString())
				.thenReturn(String.format("MockMouseEvent{x=%d,y=%d}", x, y));

		return mockMouseEvent;
	}
}
