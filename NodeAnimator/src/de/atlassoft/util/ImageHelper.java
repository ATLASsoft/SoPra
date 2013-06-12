package de.atlassoft.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Image;

public class ImageHelper {

	private static Map<String, Image> images = new HashMap<String, Image>();
	
	public static void createImage(String key, String fileName) {
		// check constraints
		
		// set values
		Image img = new Image(null, fileName);
		images.put(key, img);
	}
	
	public static Image getImage(String key) {
		return images.get(key);
	}
	
	public static void disposeImages() {
		for (String key : images.keySet()) {
			images.get(key).dispose();
		}
		images.clear();
	}
	
	public static void disposeImage(String key) {
		Image img = images.get(key);
		images.remove(key);
		if (img != null) {
			img.dispose();
		}
		
	}
	

}
