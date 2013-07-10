package de.atlassoft.model;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

import de.atlassoft.util.ImageHelper;
// TODO: Klasse testen
/**
 * Describes a train type.
 * 
 * @author Alexander Balogh
 *
 */
public class TrainType {

	/**
	 * Name of this objects. Must not be null or empty.
	 */
	private String name;

	/**
	 * Top speed of this object. Must be a positive number.
	 */
	private double topSpeed;

	/**
	 * Priority of this object. Must be a number between zero and ten
	 * (inclusive).
	 */
	private int priority;

	/**
	 * Image of this object. May be null.
	 */
	private Image img;


	/**
	 * Creates a new instance of this class. Throws an
	 * <code>IllegalArgumentException</code> if one ore more parameter are not
	 * valid.
	 * 
	 * @param name
	 *            Name of this <code>TrainType</code>, must not be null or empty
	 * @param topSpeed
	 *            Top speed of this <code>TrainType</code>, must be a positive
	 *            number
	 * @param priority
	 *            Priority of this <code>TrainType</code>, must be a number
	 *            between zero and ten (inclusive)
	 * 
	 * @throws IllegalArgumentException
	 *             if one ore more parameter are not valid
	 */
	public TrainType(String name, double topSpeed, int priority) {
		// check for illegal arguments
		if (name == null) {
			throw new IllegalArgumentException("name must not be null");
		}
		if (name.trim().length() == 0) {
			throw new IllegalArgumentException("name must not be empty");
		}
		if (topSpeed < 0) {
			throw new IllegalArgumentException(
					"topSpeed must be a positive number");
		}
		if (priority < 0 || priority > 10) {
			throw new IllegalArgumentException(
					"priority must be a number between 0 and 10 (inclusive)");
		}

		// set properties
		this.name = name;
		this.topSpeed = topSpeed;
		this.priority = priority;
	}


	/**
	 * Sets the image property of this object. img may be null. If img is not
	 * 32x32 pixel large, an IllegalArgumentException is thrown.
	 * 
	 * @param img
	 *            <code>Image</code> to be set
	 * @throws IllegalArgumentException
	 *             if img is not 32x32 pixel large
	 */
	public void setImg(Image img) {
		if (img != null) {
			Rectangle rec = img.getBounds();
			if (rec.height != 32 || rec.width != 32) {
				throw new IllegalArgumentException(
						"img must be of size 32x32 pixel");
			}
		}
		this.img = img;
	}

	/**
	 * Returns the image property of this object or null if no
	 * <code>Image</code> is set.
	 * 
	 * @return the image
	 */
	public Image getImg() {
		return img;
	}

	/**
	 * Returns the name property of this object.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the topSpeed property of this object.
	 * 
	 * @return the top speed
	 */
	public double getTopSpeed() {
		return topSpeed;
	}

	/**
	 * Returns the priority property of this object.
	 * 
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[name:");
		sb.append(name);
		sb.append("; priority:");
		sb.append(priority);
		sb.append("; topSpeed:");
		sb.append(topSpeed);
		sb.append("]");
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		// only compares name since name should be unique
		if (obj instanceof TrainType) {
			TrainType other = (TrainType) obj;
			if (other.name.equals(this.name)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
}
