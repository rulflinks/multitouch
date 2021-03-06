package de.fuberlin.mindmap2d.client.svg;

import com.google.gwt.animation.client.Animation;

/**
 * This class can be used to animate classes implementing the Animatable
 * interface.
 * 
 * @author Henri Kerola / IT Mill Ltd
 * 
 */
public class Animate {

	private Animatable target;

	private String property;

	private double startValue;

	private double endValue;

	private int duration;

	private Animation animation = new Animation() {

		@Override
		protected void onUpdate(double progress) {
			double value = (endValue - startValue) * progress + startValue;
			target.setPropertyDouble(property, value);
		}
	};

	public Animate(Animatable target, String property, double startValue,
			double endValue, int duration) {
		this.target = target;
		this.property = property;
		this.startValue = startValue;
		this.endValue = endValue;
		this.duration = duration;
	}

	public int getDuration() {
		return duration;
	}

	public double getEndValue() {
		return endValue;
	}
	
	public String getProperty() {
		return property;
	}

	public double getStartValue() {
		return startValue;
	}

	public Animatable getTarget() {
		return target;
	}

	/**
	 * Start the animation.
	 */
	public void start() {
		animation.run(duration);
	}

	/**
	 * Stop the animation.
	 */
	public void stop() {
		animation.cancel();
	}

}
