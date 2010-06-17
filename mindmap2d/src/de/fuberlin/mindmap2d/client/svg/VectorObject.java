package de.fuberlin.mindmap2d.client.svg;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

/**
 * An abstract base class for drawing objects the DrawingArea can contain.
 * 
 * @author Henri Kerola / IT Mill Ltd
 * @author flofreud
 */
public abstract class VectorObject extends Widget implements HasClickHandlers,
		HasAllMouseHandlers, HasDoubleClickHandlers {

	/**
	 * Return-Object for Transfrom-Getter(Scale,Translate,Rotation,SkewX,SkewY)
	 * Which values are set depends on the getter. x + y: Scale, Translate,
	 * Rotation angle: Rotation, SkewX, SkewY
	 */
	public class TransfromValue {
		public float x, y, angle;

		TransfromValue(float angle) {
			this.angle = angle;
		}

		TransfromValue(float x, float y) {
			this.x = x;
			this.y = y;
		}

		TransfromValue(float x, float y, float angle) {
			this.x = x;
			this.y = y;
			this.angle = angle;
		}
	}

	private Widget parent;

	public VectorObject() {
		setElement(createElement());
	}
	
	public void deactivateContextMenu(){
		sinkEvents(Event.ONCONTEXTMENU);
	}

	@Override
	public void onBrowserEvent(Event event) {
		if (DOM.eventGetType(event) == Event.ONCONTEXTMENU) {
			event.stopPropagation();
			event.preventDefault();
		} else
			super.onBrowserEvent(event);
	}

	protected abstract Element createElement();

	public Widget getParent() {
		return parent;
	}

	@SuppressWarnings("all")
	public void setParent(Widget parent) {
		Widget oldParent = this.parent;
		if (parent == null) {
			if (oldParent != null && oldParent.isAttached()) {
				onDetach();
				assert !isAttached() : "Failure of "
						+ this.getClass().getName()
						+ " to call super.onDetach()";
			}
			this.parent = null;
		} else {
			if (oldParent != null) {
				throw new IllegalStateException(
						"Cannot set a new parent without first clearing the old parent");
			}
			this.parent = parent;
			if (parent.isAttached()) {
				onAttach();
				assert isAttached() : "Failure of " + this.getClass().getName()
						+ " to call super.onAttach()";
			}
		}
	}

	public double getOpacity() {
		return SVGDom.parseDoubleValue(getElement().getAttribute("opacity"), 1);
	}

	public void setOpacity(final double opacity) {
		SVGDom.setAttributeNS(getElement(), "opacity", "" + opacity);
	}

	public TransfromValue getRotation() {
		String[] values = SVGDom.getTransformProperty(getElement(), "rotate")
				.split(",\\s");
		return (values.length == 3) ? new TransfromValue(Float
				.parseFloat(values[1]), Float.parseFloat(values[2]), Float
				.parseFloat(values[0])) : new TransfromValue(0);
	}

	public void setRotation(final float degree) {
		if (degree == 0) {
			SVGDom.removeTransformProperty(getElement(), "rotate");
			return;
		}
		DeferredCommand.addCommand(new Command() {
			public void execute() {
				SVGBBox box = SVGDom.getBBBox(getElement(), isAttached());
				int x = box.getX() + box.getWidth() / 2;
				int y = box.getY() + box.getHeight() / 2;
				SVGDom.addTransformProperty(getElement(), "rotate", degree
						+ "," + x + "," + y);
			}
		});
	}

	public TransfromValue getTranslation() {
		String[] values = SVGDom
				.getTransformProperty(getElement(), "translate").split(",\\s");
		return (values.length == 2) ? new TransfromValue(Float
				.parseFloat(values[0]), Float.parseFloat(values[1]))
				: new TransfromValue(0);
	}

	public void setTranslation(final float x, final float y) {
		if (x == 0 && y == 0) {
			SVGDom.removeTransformProperty(getElement(), "translate");
			return;
		}
		SVGDom.addTransformProperty(getElement(), "translate", x + "," + y);
	}

	public TransfromValue getScale() {
		String[] values = SVGDom.getTransformProperty(getElement(), "scale")
				.split(",\\s");
		return (values.length == 2) ? new TransfromValue(Float
				.parseFloat(values[0]), Float.parseFloat(values[1]))
				: new TransfromValue(0);
	}

	public void setScale(final float x, final float y) {
		if (x == 0 && y == 0) {
			SVGDom.removeTransformProperty(getElement(), "scale");
			return;
		}
		SVGDom.addTransformProperty(getElement(), "scale", x + "," + y);
	}

	public TransfromValue getSkewX() {
		String value = SVGDom.getTransformProperty(getElement(), "skewX");
		return (value == "") ? new TransfromValue(Float.parseFloat(value))
				: new TransfromValue(0);
	}

	public void setSkewX(final int degree) {
		if (degree == 0) {
			SVGDom.removeTransformProperty(getElement(), "rotate");
			return;
		}
		SVGDom.addTransformProperty(getElement(), "rotate", "" + degree);
	}

	public TransfromValue getSkewY() {
		String value = SVGDom.getTransformProperty(getElement(), "skewX");
		return (value == "") ? new TransfromValue(Float.parseFloat(value))
				: new TransfromValue(0);
	}

	public void setSkewY(final int degree) {
		if (degree == 0) {
			SVGDom.removeTransformProperty(getElement(), "rotate");
			return;
		}

		SVGDom.addTransformProperty(getElement(), "rotate", "" + degree);

	}

	@Override
	public void setStyleName(String style) {
		SVGDom.setClassName(getElement(), style);
	}

	@Override
	public void setHeight(String height) {
		throw new UnsupportedOperationException(
				"VectorObject doesn't support setHeight");
	}

	@Override
	public void setWidth(String width) {
		throw new UnsupportedOperationException(
				"VectorObject doesn't support setWidth");
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return addDomHandler(handler, DoubleClickEvent.getType());
	}

	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return addDomHandler(handler, MouseDownEvent.getType());
	}

	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return addDomHandler(handler, MouseUpEvent.getType());
	}

	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}

	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler, MouseOverEvent.getType());
	}

	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return addDomHandler(handler, MouseMoveEvent.getType());
	}

	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return addDomHandler(handler, MouseWheelEvent.getType());
	}

	protected void onAttach() {
		super.onAttach();
	}

	protected void onDetach() {
		super.onDetach();
	}
}