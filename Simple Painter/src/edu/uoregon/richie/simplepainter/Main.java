package edu.uoregon.richie.simplepainter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * The starting point of the application. <code>Main</code> extends
 * {@link JFrame} and is used to setup the properties and layout of the window,
 * as well as initializing and adding the GUI components.
 * @author Richard Northen
 * @version 1.0
 * @see PaintPanel
 */
@SuppressWarnings("serial")
public class Main extends JFrame implements ActionListener {
	// GUI components
	private PaintPanel paintPanel;
	private JLabel statsLabel;
	private JPanel colorsPanel,
			sizesPanel,
			toolsPanel;
	private JButton blackButton,
			redButton,
			greenButton,
			blueButton,
			smallButton,
			mediumButton,
			largeButton,
			undoButton,
			clearButton;
	// current cursor location
	private int x, y;

	/**
	 * Initializes the Main class and displays it.
	 * @param args  Unused
	 */
	public static void main(String[] args) {
		Main frame = new Main();
		frame.setVisible(true);
	}
	
	/**
	 * Creates a new <code>Main</code> that the application will use with the
	 * required features.
	 */
	public Main() {
		// frame properties
		super("SimplePainter");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		// GBL layout
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(layout);

		// paint panel
		paintPanel = new PaintPanel(this);
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 9;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		add(paintPanel, c);

		// stats label
		statsLabel = new JLabel();
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridy = 9;
		c.insets = new Insets(2, 4, 2, 4);
		c.weightx = 0;
		c.weighty = 0;
		add(statsLabel, c);

		// color buttons panel
		colorsPanel = new JPanel();
		colorsPanel.setBorder(BorderFactory.createTitledBorder("Colors"));
		colorsPanel.setLayout(layout);

		// black button
		blackButton = new JButton("Black", new ImageIcon("src/res/black.png"));
		blackButton.addActionListener(this);
		blackButton.setHorizontalAlignment(SwingConstants.LEADING);
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = Double.MIN_VALUE;
		colorsPanel.add(blackButton, c);

		// red button
		redButton = new JButton("Red", new ImageIcon("src/res/red.png"));
		redButton.addActionListener(this);
		redButton.setHorizontalAlignment(SwingConstants.LEADING);
		c.gridy = 1;
		colorsPanel.add(redButton, c);

		// green button
		greenButton = new JButton("Green", new ImageIcon("src/res/green.png"));
		greenButton.addActionListener(this);
		greenButton.setHorizontalAlignment(SwingConstants.LEADING);
		c.gridy = 2;
		colorsPanel.add(greenButton, c);

		// blue button
		blueButton = new JButton("Blue", new ImageIcon("src/res/blue.png"));
		blueButton.addActionListener(this);
		blueButton.setHorizontalAlignment(SwingConstants.LEADING);
		c.gridy = 3;
		colorsPanel.add(blueButton, c);

		add(colorsPanel, c);
		// color buttons panel

		// size buttons panel
		sizesPanel = new JPanel();
		sizesPanel.setBorder(BorderFactory.createTitledBorder("Sizes"));
		sizesPanel.setLayout(layout);

		// small button
		smallButton = new JButton("Small",
				new ImageIcon("src/res/small_black.png"));
		smallButton.addActionListener(this);
		smallButton.setHorizontalAlignment(SwingConstants.LEADING);
		c.gridy = 4;
		sizesPanel.add(smallButton, c);

		// medium button
		mediumButton = new JButton("Medium",
				new ImageIcon("src/res/medium_black.png"));
		mediumButton.addActionListener(this);
		mediumButton.setHorizontalAlignment(SwingConstants.LEADING);
		c.gridy = 5;
		sizesPanel.add(mediumButton, c);

		// large button
		largeButton = new JButton("Large",
				new ImageIcon("src/res/large_black.png"));
		largeButton.addActionListener(this);
		largeButton.setHorizontalAlignment(SwingConstants.LEADING);
		c.gridy = 6;
		sizesPanel.add(largeButton, c);

		add(sizesPanel, c);
		// size buttons panel

		// tool buttons panel
		toolsPanel = new JPanel();
		toolsPanel.setBorder(BorderFactory.createTitledBorder("Tools"));
		toolsPanel.setLayout(layout);

		// undo button
		undoButton = new JButton("Undo");
		undoButton.addActionListener(this);
		c.anchor = GridBagConstraints.PAGE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 7;
		toolsPanel.add(undoButton, c);

		// clear button
		clearButton = new JButton("Clear");
		clearButton.addActionListener(this);
		c.gridy = 8;
		toolsPanel.add(clearButton, c);

		add(toolsPanel, c);
		// tool buttons panel

		pack();
	}

	/**
	 * Determines the source of the {@link ActionEvent} and performs the
	 * appropriate action.
	 * @param event  The {@link ActionEvent} that occurred
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		// perform appropriate action
		if (source.equals(blackButton)) {
			paintPanel.setBrushColor(Color.BLACK);
			updateIcons("black");
		} else if (source.equals(redButton)) {
			paintPanel.setBrushColor(Color.RED);
			updateIcons("red");
		} else if (source.equals(greenButton)) {
			paintPanel.setBrushColor(Color.GREEN);
			updateIcons("green");
		} else if (source.equals(blueButton)) {
			paintPanel.setBrushColor(Color.BLUE);
			updateIcons("blue");
		} else if (source.equals(smallButton)) {
			paintPanel.setBrushStyle(new BasicStroke(0));
		} else if (source.equals(mediumButton)) {
			paintPanel.setBrushStyle(new BasicStroke(2));
		} else if (source.equals(largeButton)) {
			paintPanel.setBrushStyle(new BasicStroke(4));
		} else if (source.equals(undoButton)) {
			paintPanel.undoStroke();
		} else if (source.equals(clearButton)) {
			paintPanel.clearStrokes();
		}
		// update stats label
		updateStats();
	}

	/**
	 * Updates the icons for the size buttons to match the current color.
	 * @param color  The name of the current color
	 */
	private void updateIcons(String color) {
		smallButton.setIcon(new ImageIcon("src/res/small_" + color + ".png"));
		mediumButton.setIcon(new ImageIcon("src/res/medium_" + color + ".png"));
		largeButton.setIcon(new ImageIcon("src/res/large_" + color + ".png"));
	}

	/**
	 * Updates the statistics label to display the number of {@link PaintStroke}
	 * objects, the number of {@link PaintPoint} objects, the current color,
	 * and the current size. The cursor position is not updated.
	 * @see Main#updateStats(int,int)
	 */
	private void updateStats() {
		// update the stats label
		statsLabel.setText("X: " + x + " Y: " + y 
				+ " | PaintStrokes: " + paintPanel.getTotalStrokes()
				+ " | PaintPoints: " + paintPanel.getTotalPoints()
				+ " | Color: " + paintPanel.getBrushColor().toString()
				+ " | Size: " + paintPanel.getBrushStyle().getLineWidth());
	}
	
	/**
	 * Updates the statistics label to display the current cursor position, the
	 * number of {@link PaintStroke} objects, the number of {@link PaintPoint}
	 * objects, the current color, and the current size.
	 * @param x  Cursor x position
	 * @param y  Cursor y position
	 * @see Main#updateStats()
	 */
	public void updateStats(int x, int y) {
		// update the cursor location
		this.x = x;
		this.y = y;
		// update the stats label
		updateStats();
	}
}