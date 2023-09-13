package com.github.ants280.slidegame.ui;

import com.github.ants280.slidegame.logic.Grid;
import com.github.ants280.slidegame.logic.MoveDirection;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class SlideGameManager
{
	private final Grid grid;
	private final JFrame slideGameRootComponent;
	private final JComponent slideGameDisplayComponent;
	private final SlideGameLabelManager slideGameLabelManager;
	private final KeyListener keyListener;
	private final MouseListener mouseListener;
	private int score;
	private int highScore;
	private boolean gameOver;
	private boolean gameWon;
	private boolean listenersAdded;
	private MouseEvent mousePressedLocation;

	public SlideGameManager(
			Grid grid,
			JFrame slideGameRootComponent,
			JComponent slideGameDisplayComponent,
			SlideGameLabelManager slideGameLabelManager)
	{
		this.grid = grid;
		this.slideGameRootComponent = slideGameRootComponent;
		this.slideGameDisplayComponent = slideGameDisplayComponent;
		this.slideGameLabelManager = slideGameLabelManager;

		this.keyListener = new SlideGameKeyListener(
				this::keyReleased);
		this.mouseListener = new SlideGameMouseListener(
				this::mousePressed,
				this::mouseReleased);
		this.score = 0;
		this.highScore = 0;
		this.gameOver = false;
		this.gameWon = false;
		this.listenersAdded = false;

		this.initGame();
	}

	public int getGridLength()
	{
		return grid.getLength();
	}

	public void setGridLength(int length)
	{
		grid.setLength(length);
		this.newGame();
	}

	public int getGoalTileValue()
	{
		return grid.getGoalTileValue();
	}

	public void setGoalTileValue(int goalTileValue)
	{
		grid.setGoalTileValue(goalTileValue);
		this.newGame();
		slideGameLabelManager.updateGoalLabel(grid.getGoalTileValue());
	}

	public void makeMove(MoveDirection moveDirection)
	{
		boolean validMove = moveDirection != null
				&& grid.canSlideTiles(moveDirection);

		if (validMove)
		{

			int moveScore = grid.slideTiles(moveDirection);

			this.incrementScore(moveScore);

			if (!grid.canSlideInAnyDirection() || grid.goalTileCreated())
			{
				gameWon = grid.goalTileCreated();
				this.endGame();
			}
			else
			{
				grid.addRandomTile();

				if (!grid.canSlideInAnyDirection())
				{
					this.endGame();
				}
			}

			slideGameLabelManager.updateScoreLabels(
					gameOver,
					gameWon,
					score,
					highScore);

			slideGameDisplayComponent.repaint();
		}

		slideGameLabelManager.updateMoveLabel(moveDirection, validMove);
	}

	private void initGame()
	{
		gameOver = false;
		gameWon = false;
		score = 0;
		grid.addRandomTile();
		grid.addRandomTile();
		this.addListeners();
		slideGameLabelManager.updateScoreLabels(
				gameOver,
				gameWon,
				score,
				highScore);
		slideGameLabelManager.updateGoalLabel(grid.getGoalTileValue());
		slideGameLabelManager.clearMoveLabel();
	}

	public void newGame()
	{
		grid.clear();
		this.initGame();
		slideGameDisplayComponent.repaint();
	}

	private void endGame()
	{
		gameOver = true;
		this.removeListeners();
	}

	private void addListeners()
	{
		if (!listenersAdded)
		{
			slideGameRootComponent.addKeyListener(keyListener);
			slideGameDisplayComponent.addMouseListener(mouseListener);
			mousePressedLocation = null;
			listenersAdded = true;
		}
	}

	private void removeListeners()
	{
		slideGameRootComponent.removeKeyListener(keyListener);
		slideGameDisplayComponent.removeMouseListener(mouseListener);
		mousePressedLocation = null;
		listenersAdded = false;
	}

	private void incrementScore(int additionalScore)
	{
		if (additionalScore != 0)
		{
			this.score += additionalScore;
			if (score > highScore)
			{
				highScore = score;
			}
		}
	}

	private void keyReleased(KeyEvent e)
	{
		this.makeMove(MoveDirection.fromKeyEvent(e));
	}

	private void mousePressed(MouseEvent e)
	{
		if (e.getButton() != MouseEvent.BUTTON1)
		{
			return;
		}

		mousePressedLocation = e;
	}

	private void mouseReleased(MouseEvent e)
	{
		if (e.getButton() != MouseEvent.BUTTON1
				|| mousePressedLocation == null)
		{
			return;
		}

		this.makeMove(MoveDirection.fromMouseEvents(mousePressedLocation, e));
	}
}
