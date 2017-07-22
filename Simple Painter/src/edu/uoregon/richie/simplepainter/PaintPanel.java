package edu.uoregon.richie.simplepainter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * <code>PaintPanel</code> represents the paint-able region of the application
 * that the user can interact with. It stores brush information, such as the
 * {@link Color} and {@link BasicStroke} in use. User actions are stored as
 * {@link PaintStroke} and {@link PaintPoint} objects and can be undone or
 * deleted.
 * @author Richard Northen
 * @version 1.0
 * @see Color
 * @see BasicStroke
 * @see PaintStroke
 * @see PaintPoint
 */
@SuppressWarnings("serial")
public class PaintPanel extends JPanel {
	// brush and stroke data
	private Color brushColor;
	private BasicStroke brushStyle;
	private ArrayList<PaintStroke> strokes;
	private PaintStroke currentStroke;
	// local variables
	private final Main parentFrame;
	private boolean isMouseOnScreen,
			isMousePressed;

	/**
	 * Creates a new <code>PaintPanel</code> with a reference to the
	 * {@link Main} that created it.
	 * @param frame  The parent Main object
	 */
	public PaintPanel(Main frame) {
		// panel properties
		setBackground(Color.WHITE);
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		setPreferredSize(new Dimension(800, 600));

		// initialize values
		brushColor = Color.BLACK;
		brushStyle = new BasicStroke(0);
		strokes = new ArrayList<PaintStroke>();
		currentStroke = new PaintStroke(brushColor, brushStyle);
		parentFrame = frame;

		// mouse motion events
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				// stop drawing when mouse leaves panel
				if (isMouseOnScreen) {
					currentStroke.addPoint(e.getX(), e.getY());
					parentFrame.updateStats(e.getX(), e.getY());
					repaint();
				}
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				parentFrame.updateStats(e.getX(), e.getY());
			}
		}); // mouse motion

		// other mouse inputs
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// begin a new stroke
				isMousePressed = true;
				currentStroke = new PaintStroke(brushColor, brushStyle);
				currentStroke.addPoint(e.getX(), e.getY());
				strokes.add(currentStroke);
				parentFrame.updateStats(e.getX(), e.getY());
				repaint();
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				isMousePressed = false;
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				isMouseOnScreen = true;
				// begin a new stroke if re-entering the screen with mouse held
				if (isMousePressed) {
					currentStroke = new PaintStroke(brushColor, brushStyle);
					currentStroke.addPoint(e.getX(), e.getY());
					strokes.add(currentStroke);
					parentFrame.updateStats(e.getX(), e.getY());
					repaint();
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				isMouseOnScreen = false;
			}
		}); // mouse inputs
	}

	/**
	 * Updates the display area of the <code>PaintPanel</code> with the current
	 * set of {@link PaintStroke}s.
	 * @param g  The {@link Graphics} object to draw with
	 */
	@Override
	public void paintComponent(Graphics g) {
		// enable setStroke functionality
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);

		// add all strokes to panel
		for (PaintStroke stroke : strokes) {
			g2.setColor(stroke.getStrokeColor());
			g2.setStroke(stroke.getStrokeStyle());
			for (int i = 0; i < stroke.getPoints().size(); i++) {
				PaintPoint point = stroke.getPoints().get(i);
				try {
					// draw a line from point to point
					PaintPoint nextPoint = stroke.getPoints().get(i+1);
					g2.drawLine(point.getX(), point.getY(), nextPoint.getX(), nextPoint.getY());
				} catch (IndexOutOfBoundsException e) {
					// allows dots to be drawn (line with no length)
					g2.drawLine(point.getX(), point.getY(), point.getX(), point.getY());
				}
			}
		}
	}

	/**
	 * Removes the most recent {@link PaintStroke}.
	 */
	public void undoStroke() {
		if (!strokes.isEmpty()) {
			strokes.remove(strokes.size() - 1);
			repaint();
		}
	}

	/**
	 * Removes all {@link PaintStroke}s.
	 */
	public void clearStrokes() {
		strokes.clear();
		repaint();
	}

	/**
	 * Returns the number of {@link PaintStroke} objects.
	 * @return The number of {@link PaintStroke} objects
	 */
	public int getTotalStrokes() {
		return strokes.size();
	}

	/**
	 * Returns the number of {@link PaintPoint} objects.
	 * @return The number of {@link PaintPoint} objects
	 */
	public int getTotalPoints() {
		int totalPoints = 0;
		for (PaintStroke stroke : strokes)
			totalPoints += stroke.getPoints().size();
		return totalPoints;
	}

	/**
	 * Returns the {@link Color} object of the brush.
	 * @return The {@link Color} object of the brush
	 */
	public Color getBrushColor() {
		return brushColor;
	}

	/**
	 * Sets the {@link Color} object of the brush.
	 * @param brushColor  The {@link Color} object of the brush
	 */
	public void setBrushColor(Color brushColor) {
		this.brushColor = brushColor;
	}

	/**
	 * Returns the {@link BasicStroke} object of the brush.
	 * @return The {@link BasicStroke} object of the brush
	 */
	public BasicStroke getBrushStyle() {
		return brushStyle;
	}

	/**
	 * Sets the {@link BasicStroke} object of the brush.
	 * @param brushStyle  The {@link BasicStroke} object of the brush
	 */
	public void setBrushStyle(BasicStroke brushStyle) {
		this.brushStyle = brushStyle;
	}
}