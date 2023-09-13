package com.github.ants280.slidegame.ui;

import com.github.ants280.slidegame.logic.Grid;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

public class SlideGameFrame
{
	private final JFrame frame;
	private final SlideGameManager slideGameManager;
	private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(
			2, 10, 2, 10); // top, left, bottom, right
	private static final Border LINE_BORDER
			= BorderFactory.createCompoundBorder(
					EMPTY_BORDER,
					BorderFactory.createLineBorder(
							SlideGameColors.SPACER_COLOR,
							1, // width
							true)); // rounded

	public SlideGameFrame()
	{
		this.frame = new JFrame("Slide Game");

		Grid grid = new Grid();
		JComponent slideGameDisplayComponent
				= new SlideGameDisplayComponent(grid).getComponent();
		JLabel gameOverLabel = this.createJLabel(false);
		JLabel scoreLabel = this.createJLabel(true);
		JLabel highScoreLabel = this.createJLabel(true);
		JLabel goalLabel = this.createJLabel(false);
		JLabel moveLabel = this.createJLabel(false);

		this.slideGameManager = new SlideGameManager(
				grid,
				frame,
				slideGameDisplayComponent,
				new SlideGameLabelManager(
						scoreLabel,
						highScoreLabel,
						moveLabel,
						goalLabel,
						gameOverLabel));

		this.initSize(
				slideGameDisplayComponent,
				gameOverLabel,
				scoreLabel,
				highScoreLabel,
				goalLabel,
				moveLabel);
	}

	private void initSize(
			JComponent slideGameDisplayComponent,
			JLabel gameOverLabel,
			JLabel scoreLabel,
			JLabel highScoreLabel,
			JLabel goalLabel,
			JLabel moveLabel)
	{
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.add(gameOverLabel);
		topPanel.add(Box.createGlue());
		topPanel.add(scoreLabel);
		topPanel.add(highScoreLabel);
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		bottomPanel.add(goalLabel);
		bottomPanel.add(Box.createGlue());
		bottomPanel.add(moveLabel);
		slideGameDisplayComponent.setBorder(EMPTY_BORDER);

		frame.setJMenuBar(createJMenuBar());
		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(bottomPanel, BorderLayout.SOUTH);
		frame.add(slideGameDisplayComponent);

		frame.setMinimumSize(new Dimension(400, 447));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
	}

	public JFrame getWindow()
	{
		return frame;
	}

	private JLabel createJLabel(boolean addBorder)
	{
		JLabel label = new JLabel();
		label.setBorder(addBorder ? LINE_BORDER : EMPTY_BORDER);
		return label;
	}

	private JMenuBar createJMenuBar()
	{
		JMenuItem setGridLengthMenuItem = new JMenuItem("Set grid length...");
		setGridLengthMenuItem.addActionListener(
				actionEvent -> this.showSetGridLengthPopup());

		JMenuItem setGoalTileValueMenuItem = new JMenuItem(
				"Set goal tile value...");
		setGoalTileValueMenuItem.addActionListener(
				actionEvent -> this.showSetGoalTileValuePopup());

		JMenuItem newGameMenuItem = new JMenuItem("New Game", KeyEvent.VK_N);
		newGameMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		newGameMenuItem.addActionListener(
				actionEvent -> slideGameManager.newGame());

		JMenuItem exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
		exitMenuItem.addActionListener(
				actionEvent -> Runtime.getRuntime().exit(0));

		JMenuItem helpMenuItem = new JMenuItem("Help...", KeyEvent.VK_H);
		helpMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK));
		helpMenuItem.addActionListener(
				actionEvent -> this.showHelpPopup());

		JMenuItem aboutMenuItem = new JMenuItem("About...", KeyEvent.VK_F1);
		aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_F1, KeyEvent.CTRL_DOWN_MASK));
		aboutMenuItem.addActionListener(
				actionEvent -> this.showAboutPopup());

		JMenu actionMenu = new JMenu("Action");
		actionMenu.add(setGridLengthMenuItem);
		actionMenu.add(setGoalTileValueMenuItem);
		actionMenu.addSeparator();
		actionMenu.add(newGameMenuItem);
		actionMenu.add(exitMenuItem);

		JMenu helpMenu = new JMenu("Help");
		helpMenu.add(helpMenuItem);
		helpMenu.add(aboutMenuItem);

		JMenuBar mainMenu = new JMenuBar();
		mainMenu.add(actionMenu);
		mainMenu.add(helpMenu);

		return mainMenu;
	}

	private void showAboutPopup()
	{
		String message
				= "By: Jacob Patterson"
				+ "\n"
				+ "\nCopyright(©) 2018"
				+ "\n"
				+ "\nBased on the game by Gabriele Cirulli.";
		String title = "About " + frame.getTitle();
		this.showPopup(message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	private void showHelpPopup()
	{
		String message
				= "Slide the tiles all to the top, left, bottom, and right of "
				+ "the grid to\ncombine tiles of equal values. When tiles "
				+ "combine, the combined tile's value\nis their sum of values."
				+ " Combine tiles by typing the appropriate arrow key,\nusing "
				+ "WASD controls, or clicking and dragging in the desired "
				+ "direction on the\ngrid. After each turn a new tile is "
				+ "added. The game is over if the grid is\nfull and no moves "
				+ "can be made. It is won when a tile of the goal value is\n"
				+ "created.";
		String title = "Help for " + frame.getTitle();
		this.showPopup(message, title, JOptionPane.QUESTION_MESSAGE);
	}

	private void showPopup(String message, String title, int messageType)
	{
		JOptionPane.showMessageDialog(frame, message, title, messageType);
	}

	private void showSetGridLengthPopup()
	{
		String message = "Set grid length";
		int goalTileValue = slideGameManager.getGoalTileValue();
		int minimumGridLength = (int) Math.ceil(Math.sqrt(
				Math.log(goalTileValue) / Math.log(2d) - 1));
		Object[] selectionValues = IntStream.range(
				minimumGridLength, minimumGridLength + 10)
				.boxed()
				.toArray();
		int initialSelectionValue = slideGameManager.getGridLength();
		this.showOptionDialog(
				message,
				selectionValues, initialSelectionValue,
				slideGameManager::setGridLength);
	}

	private void showSetGoalTileValuePopup()
	{
		String message = "Set goal tile value";
		int gridLength = slideGameManager.getGridLength();
		int maximumGoalTileValue = (int) Math.pow(2, Math.pow(gridLength, 2));
		Object[] selectionValues = IntStream.range(0, 10)
				.map(i -> (int) Math.pow(2d, i + 3d))
				.filter(possibleGoalTileValue
						-> possibleGoalTileValue <= maximumGoalTileValue)
				.boxed()
				.toArray();
		int initialSelectionValue = slideGameManager.getGoalTileValue();
		this.showOptionDialog(
				message,
				selectionValues, initialSelectionValue,
				slideGameManager::setGoalTileValue);
	}

	private void showOptionDialog(
			String message,
			Object[] selectionValues, Object initialSelectionValue,
			IntConsumer setValueFunction)
	{
		Object optionChoice = JOptionPane.showInputDialog(
				frame,
				message, "Change field for " + frame.getTitle(),
				JOptionPane.QUESTION_MESSAGE, null,
				selectionValues, initialSelectionValue);
		if (optionChoice != null)
		{
			setValueFunction.accept(Integer.parseInt(optionChoice.toString()));
		}
	}
}
