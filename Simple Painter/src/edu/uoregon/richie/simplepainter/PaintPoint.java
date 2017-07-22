package edu.uoregon.richie.simplepainter;

/**
 * <code>PaintPoint</code> is a simple container for an individual point.
 * @author Richard Northen
 * @version 1.0
 * @see PaintStroke
 */
public class PaintPoint {
	// point data
	private int x, y;

	/**
	 * Creates a new <code>PaintPoint</code> with the coordinates
	 * <code>x</code> and <code>y</code>.
	 * @param x  The x coordinate
	 * @param y  The y coordinate
	 */
	public PaintPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the x coordinate.
	 * @return The x coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x coordinate.
	 * @param x  The x coordinate
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Returns the y coordinate.
	 * @return The y coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y coordinate.
	 * @param y  The y coordinate
	 */
	public void setY(int y) {
		this.y = y;
	}
}