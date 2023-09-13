package com.github.ants280.slidegame.ui;

import com.github.ants280.slidegame.logic.MoveDirection;
import java.awt.Color;
import java.util.concurrent.TimeUnit;
import javax.swing.JLabel;
import javax.swing.Timer;

public class SlideGameLabelManager
{
	private final JLabel scoreLabel;
	private final JLabel highScoreLabel;
	private final JLabel moveLabel;
	private final JLabel goalLabel;
	private final JLabel gameOverLabel;
	private final Timer moveLabelClearingTimer;

	public SlideGameLabelManager(
			JLabel scoreLabel,
			JLabel highScoreLabel,
			JLabel moveLabel,
			JLabel goalLabel,
			JLabel gameOverLabel)
	{
		this.scoreLabel = scoreLabel;
		this.highScoreLabel = highScoreLabel;
		this.moveLabel = moveLabel;
		this.goalLabel = goalLabel;
		this.gameOverLabel = gameOverLabel;

		this.moveLabelClearingTimer = new Timer(
				(int) TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS),
				actiovEvent -> this.clearMoveLabel());
		moveLabelClearingTimer.setRepeats(false);
	}

	public void updateScoreLabels(
			boolean gameOver,
			boolean gameWon,
			int score,
			int highScore)
	{
		String gameOverLabelText = "";
		if (gameOver)
		{
			gameOverLabelText = gameWon ? "You Win!" : "You Lose.";
		}
		gameOverLabel.setText(gameOverLabelText);
		scoreLabel.setText("SCORE: " + score);
		highScoreLabel.setText("BEST: " + highScore);
	}

	public void updateGoalLabel(int goalTileValue)
	{
		goalLabel.setText(String.format(
				"Goal: Create %d tile",
				goalTileValue));
	}

	public void updateMoveLabel(
			MoveDirection moveDirection,
			boolean validMove)
	{
		if (moveDirection != null)
		{
			moveLabel.setForeground(validMove ? Color.BLACK : Color.RED);
			moveLabel.setText(String.format(
					validMove ? "Moved %s" : "Cannot move %s",
					moveDirection.getDisplayValue()));
			moveLabelClearingTimer.stop();
			moveLabelClearingTimer.start();
		}
	}

	public void clearMoveLabel()
	{
		moveLabelClearingTimer.stop();
		moveLabel.setText("");
	}
}
