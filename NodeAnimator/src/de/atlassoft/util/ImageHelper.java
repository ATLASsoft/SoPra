package de.atlassoft.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Image;

/**
 * This class provides universal access to all {@link Image} objects used in
 * this application.
 * 
 * @author Alexander Balogh
 * 
 */
public class ImageHelper {

	private static Map<String, Image> images = new HashMap<String, Image>();

	
	
	/**
	 * Creates a new {@link Image} from the given file.
	 * 
	 * @param key
	 *            to access this {@link Image}
	 * @param fileName
	 *            location of the {@link Image} to be loaded
	 * @throws IllegalArgumentException
	 *             if key is already used
	 */
	public static void createImage(String key, String fileName) {
		if (images.keySet().contains(key)) {
			throw new IllegalArgumentException("key is already used");
		}

		Image img = new Image(null, fileName);
		images.put(key, img);
	}
	
	/**
	 * Return the {@link Image} affiliated with key or null if no {@link Image}
	 * is affiliated with key.
	 * 
	 * @param key
	 * @return image affiliated with key
	 */
	public static Image getImage(String key) {
		return images.get(key);
	}
	
	/**
	 * Disposes all images that have been added to this class. Once the images
	 * have been disposed, they are no longer accessible via
	 * {@link ImageHelper#getImage(String)}.
	 */
	public static void disposeImages() {
		for (String key : images.keySet()) {
			images.get(key).dispose();
		}
		images.clear();
	}
	
	/**
	 * Disposes the {@link Image} affiliated with key. Once the image has been
	 * disposed, it will be no longer accessible via
	 * {@link ImageHelper#getImage(String)}.
	 * 
	 * @param key
	 */
	public static void disposeImage(String key) {
		Image img = images.get(key);
		images.remove(key);
		if (img != null) {
			img.dispose();
		}
		
	}
}
