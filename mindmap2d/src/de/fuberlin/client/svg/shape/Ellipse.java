package de.fuberlin.client.svg.shape;

import de.fuberlin.client.svg.Shape;
import de.fuberlin.client.svg.VectorObject;
import de.fuberlin.client.svg.util.SVGBase;

/**
 * Ellipse represents an ellipse.
 * 
 * @author Henri Kerola / IT Mill Ltd
 * 
 */
public class Ellipse extends Shape {

	/**
	 * Creates a new Ellipse with the given position and radius properties.
	 * 
	 * @param x
	 *            the x-coordinate position of the center of the ellipse in
	 *            pixels
	 * @param y
	 *            the y-coordinate position of the center of the ellipse in
	 *            pixels
	 * @param radiusX
	 *            the x-axis radius of the ellipse in pixels
	 * @param radiusY
	 *            the y-axis radius of the ellipse in pixels
	 */
	public Ellipse(int x, int y, int radiusX, int radiusY) {
		setRadiusX(radiusX);
		setRadiusY(radiusY);
		setX(x);
		setY(y);
	}

	@Override
	protected Class<? extends VectorObject> getType() {
		return Ellipse.class;
	}

	/**
	 * Returns the x-axis radius of the ellipse in pixels.
	 * 
	 * @return the x-axis radius of the ellipse in pixels
	 */
	public int getRadiusX() {
		return SVGBase.getEllipseRadiusX(getElement());
	}

	/**
	 * Sets the x-axis radius of the ellipse in pixels.
	 * 
	 * @param radiusX
	 *            the x-axis radius of the ellipse in pixels
	 */
	public void setRadiusX(int radiusX) {
		SVGBase.setEllipseRadiusX(getElement(), radiusX);
	}

	/**
	 * Returns the y-axis radius of the ellipse in pixels.
	 * 
	 * @return the y-axis radius of the ellipse in pixels
	 */
	public int getRadiusY() {
		return SVGBase.getEllipseRadiusY(getElement());
	}

	/**
	 * Sets the y-axis radius of the ellipse in pixels.
	 * 
	 * @param radiusY
	 *            the y-axis radius of the ellipse in pixels
	 */
	public void setRadiusY(int radiusY) {
		SVGBase.setEllipseRadiusY(getElement(), radiusY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.vaadin.gwtgraphics.client.Shape#setPropertyDouble(java.lang.String,
	 * double)
	 */
	public void setPropertyDouble(String property, double value) {
		property = property.toLowerCase();
		if ("radiusx".equals(property)) {
			setRadiusX((int) value);
		} else if ("radiusy".equals(property)) {
			setRadiusY((int) value);
		} else {
			super.setPropertyDouble(property, value);
		}
	}

}
