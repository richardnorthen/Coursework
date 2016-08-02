package edu.uoregon.richie.simplepainter;

import java.awt.BasicStroke;
import java.awt.Color;

import java.util.ArrayList;

/**
 * The <code>PaintStroke</code> class represents a stroke on the panel that has
 * been drawn. It stores the {@link Color} and {@link BasicStroke} used by the
 * stroke, as well as an {@link ArrayList} of {@link PaintPoint} objects.
 * @author Richard Northen
 * @version 1.0
 * @see Color
 * @see BasicStroke
 * @see PaintPoint
 */
public class PaintStroke {
	// stroke data
	private ArrayList<PaintPoint> points;
	private Color strokeColor;
	private BasicStroke strokeStyle;

	/**
	 * Creates a new <code>PaintStroke</code> with a specified {@link Color}
	 * and {@link BasicStroke}.
	 * @param strokeColor  The {@link Color} associated with the stroke
	 * @param strokeStyle  The {@link BasicStroke} associated with the stroke
	 */
	public PaintStroke(Color strokeColor, BasicStroke strokeStyle) {
		points = new ArrayList<PaintPoint>();
		this.strokeColor = strokeColor;
		this.strokeStyle = strokeStyle;
	}

	/**
	 * Adds a new {@link PaintPoint} to the <code>PaintStroke</code> with
	 * coordinates <code>x</code> and <code>y</code>.
	 * @param x  The x coordinate
	 * @param y  The y coordinate
	 */
	public void addPoint(int x, int y) {
		// add a new point to the stroke
		points.add(new PaintPoint(x, y));
	}

	/**
	 * Returns the {@link ArrayList} of {@link PaintPoint} objects.
	 * @return The {@link ArrayList} of {@link PaintPoint} objects
	 */
	public ArrayList<PaintPoint> getPoints() {
		return points;
	}

	/**
	 * Sets the {@link ArrayList} of {@link PaintPoint} objects.
	 * @param points  The {@link ArrayList} of {@link PaintPoint} objects
	 */
	public void setPoints(ArrayList<PaintPoint> points) {
		this.points = points;
	}

	/**
	 * Returns the {@link Color} of the <code>PaintStroke</code>.
	 * @return The {@link Color} of the <code>PaintStroke</code>
	 */
	public Color getStrokeColor() {
		return strokeColor;
	}

	/**
	 * Sets the {@link Color} of the <code>PaintStroke</code>.
	 * @param strokeColor  The {@link Color} of the <code>PaintStroke</code>
	 */
	public void setStrokeColor(Color strokeColor) {
		this.strokeColor = strokeColor;
	}

	/**
	 * Returns the {@link BasicStroke} of the <code>PaintStroke</code>.
	 * @return The {@link BasicStroke} of the <code>PaintStroke</code>
	 */
	public BasicStroke getStrokeStyle() {
		return strokeStyle;
	}

	/**
	 * Sets the {@link BasicStroke} of the <code>PaintStroke</code>.
	 * @param strokeStyle  The {@link BasicStroke} of the <code>PaintStroke</code>
	 */
	public void setStrokeStyle (BasicStroke strokeStyle) {
		this.strokeStyle = strokeStyle;
	}
}