package de.fuberlin.mindmap2d.client.svg.shape;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;

import de.fuberlin.mindmap2d.client.svg.SvgDom;
import de.fuberlin.mindmap2d.client.svg.Shape;
import de.fuberlin.mindmap2d.client.svg.shape.path.Arc;
import de.fuberlin.mindmap2d.client.svg.shape.path.ClosePath;
import de.fuberlin.mindmap2d.client.svg.shape.path.CurveTo;
import de.fuberlin.mindmap2d.client.svg.shape.path.LineTo;
import de.fuberlin.mindmap2d.client.svg.shape.path.MoveTo;
import de.fuberlin.mindmap2d.client.svg.shape.path.PathStep;


/**
 * Path represents a path consisting of pen movement commands. Currently,
 * moveTo, lineTo and close commands are supported. The moveTo and lineTo
 * commands support both relative and absolute coordinates.
 * <p>
 * The code below creates a path drawing a 100 x 100 pixels rectangle at the
 * position (50, 50):
 * </p>
 * 
 * <pre>
 * Path path = new Path(50, 50);
 * path.lineRelativelyTo(100, 0);
 * path.lineRelativelyTo(0, 100);
 * path.lineRelativelyTo(-100, 0);
 * path.close();
 * </pre>
 * 
 * This rectangle is modified as a triangle with the following code:
 * 
 * <pre>
 * path.setStep(2, new LineTo(true, -50, 100));
 * path.removeStep(3);
 * </pre>
 * 
 * @author Henri Kerola / IT Mill Ltd
 * 
 */
public class Path extends Shape {

	private final List<PathStep> steps = new ArrayList<PathStep>();

	/**
	 * Creates a new Path and sets its starting point at the given position.
	 * 
	 * @param x
	 *            the x-coordinate position in pixels
	 * @param y
	 *            the y-coordinate position in pixels
	 */
	public Path(int x, int y) {
		moveTo(x, y);
	}

	@Override
	protected Element createElement() {
		return SvgDom.createSVGElementNS("path");
	}
	
	@Override
	public int getX() {
		return ((MoveTo) steps.get(0)).getX();
	}

	@Override
	public void setX(int x) {
		steps.set(0, new MoveTo(false, x, getY()));
		drawPath();
	}

	@Override
	public int getY() {
		return ((MoveTo) steps.get(0)).getY();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vaadin.gwtgraphics.client.Shape#setY(int)
	 */
	@Override
	public void setY(int y) {
		steps.set(0, new MoveTo(false, getX(), y));
		drawPath();
	}

	/**
	 * Sets PathStep at the specified position.
	 * 
	 * @param index
	 *            the index of the PathStep element to set
	 * @param step
	 *            PathStep to be stored at the specified position
	 * @throws IllegalArgumentException
	 */
	public void setStep(int index, PathStep step)
			throws IllegalArgumentException {
		if (index == 0
				&& !(step instanceof MoveTo || ((MoveTo) step)
						.isRelativeCoords())) {
			throw new IllegalArgumentException(
					"The first step must be an absolute MoveTo step.");
		} else {
			steps.set(index, step);
			drawPath();
		}
	}

	/**
	 * Removes the PathStep element at the specified position. Shifts any
	 * subsequent elements to the left.
	 * 
	 * @param index
	 *            the index of the PathStep element to removed
	 */
	public void removeStep(int index) {
		steps.remove(index);
		drawPath();
	}

	/**
	 * Returns the number of PathSteps in this Path.
	 * 
	 * @return the number of PathSteps in this Path.
	 */
	public int getStepCount() {
		return steps.size();
	}

	/**
	 * Returns the PathStep element at the specified position.
	 * 
	 * @param index
	 *            index of element to return.
	 * @return the PathStep element at the specified position.
	 */
	public PathStep getStep(int index) {
		return steps.get(index);
	}

	/**
	 * Start a new sub-path at the given absolute point.
	 * 
	 * @param x
	 *            an absolute x-coordinate in pixels
	 * @param y
	 *            an absolute y-coordinate in pixels
	 */
	public void moveTo(int x, int y) {
		steps.add(new MoveTo(false, x, y));
		drawPath();
	}

	/**
	 * Start a new sub-path at the given relative point.
	 * 
	 * @param x
	 *            a relative x-coordinate in pixels
	 * @param y
	 *            a relative y-coordinate in pixels
	 */
	public void moveRelativelyTo(int x, int y) {
		steps.add(new MoveTo(true, x, y));
		drawPath();
	}

	/**
	 * Draw a line from the current point to the given absolute point.
	 * 
	 * @param x
	 *            an absolute x-coordinate in pixels
	 * @param y
	 *            an absolute y-coordinate in pixels
	 */
	public void lineTo(int x, int y) {
		steps.add(new LineTo(false, x, y));
		drawPath();
	}

	/**
	 * Draw a line from the current point to the given relative point.
	 * 
	 * @param x
	 *            a relative x-coordinate in pixels
	 * @param y
	 *            a relative y-coordinate in pixels
	 */
	public void lineRelativelyTo(int x, int y) {
		steps.add(new LineTo(true, x, y));
		drawPath();
	}

	/**
	 * Draws a cubic B�zier curve.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x
	 * @param y
	 */
	public void curveTo(int x1, int y1, int x2, int y2, int x, int y) {
		steps.add(new CurveTo(false, x1, y1, x2, y2, x, y));
		drawPath();
	}

	/**
	 * Draws a cubic B�zier curve.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x
	 * @param y
	 */
	public void curveRelativelyTo(int x1, int y1, int x2, int y2, int x, int y) {
		steps.add(new CurveTo(true, x1, y1, x2, y2, x, y));
		drawPath();
	}

	public void arc(int rx, int ry, int xAxisRotation, boolean largeArc,
			boolean sweep, int x, int y) {
		steps.add(new Arc(false, rx, ry, xAxisRotation, largeArc, sweep, x, y));
		drawPath();
	}

	public void arcRelatively(int rx, int ry, int xAxisRotation,
			boolean largeArc, boolean sweep, int x, int y) {
		steps.add(new Arc(true, rx, ry, xAxisRotation, largeArc, sweep, x, y));
		drawPath();
	}

	/**
	 * Close the path.
	 */
	public void close() {
		steps.add(new ClosePath());
		drawPath();
	}

	private void drawPath() {
		StringBuilder path = new StringBuilder();
		for (PathStep step : steps) {
			if (step.getClass() == ClosePath.class) {
				path.append(" z");
			} else if (step.getClass() == MoveTo.class) {
				MoveTo moveTo = (MoveTo) step;
				path.append(moveTo.isRelativeCoords() ? " m" : " M").append(
						moveTo.getX()).append(" ").append(moveTo.getY());
			} else if (step.getClass() == LineTo.class) {
				LineTo lineTo = (LineTo) step;
				path.append(lineTo.isRelativeCoords() ? " l" : " L").append(
						lineTo.getX()).append(" ").append(lineTo.getY());
			} else if (step.getClass() == CurveTo.class) {
				CurveTo curve = (CurveTo) step;
				path.append(curve.isRelativeCoords() ? " c" : " C");
				path.append(curve.getX1()).append(" ").append(curve.getY1());
				path.append(" ").append(curve.getX2()).append(" ").append(
						curve.getY2());
				path.append(" ").append(curve.getX()).append(" ").append(
						curve.getY());
			} else if (step.getClass() == Arc.class) {
				Arc arc = (Arc) step;
				path.append(arc.isRelativeCoords() ? " a" : " A");
				path.append(arc.getRx()).append(",").append(arc.getRy());
				path.append(" ").append(arc.getxAxisRotation());
				path.append(" ").append(arc.isLargeArc() ? "1" : "0").append(
						",").append(arc.isSweep() ? "1" : "0");
				path.append(" ").append(arc.getX()).append(",").append(
						arc.getY());
			}
		}

		SvgDom.setAttributeNS(getElement(), "d", path.toString());
	}
}
