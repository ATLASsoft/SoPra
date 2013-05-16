package de.atlassoft.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Provides I18N support.
 * Implements the I18N service interface as GoF Singleton.
 */
public class I18NSingleton implements I18NService {

	/**
	 * Singleton attribute to access an instance of this.
	 */
	private static I18NService instance;
	
	/**
	 * The base name of the file containing the translations.
	 */
	// TODO: Pfad zum ressourcebundle
	private static final String MESSAGES_BASE_NAME = "jabi.res.Messages";

	
	/**
	 * Returns the only instance of this class.
	 * This method is thread safe.
	 * @return Only instance of this class.
	 */
	public static synchronized I18NService getInstance() {
		if (instance == null) {
			instance = new I18NSingleton();
		}
		return instance;
	}
	
	
	
	/**
	 * The resource bundle that holds the translations for the UI.
	 */
	private final ResourceBundle bundle;

	
	/**
	 * Creates new instance of this class and loads standard
	 * resource bundle.
	 */
	private I18NSingleton() {
		// Load the resource bundle
		bundle = ResourceBundle.getBundle(MESSAGES_BASE_NAME);
	}


	/**
	 * Returns the message that is associated with the given key. Place holders
	 * in the message are replace with the given objects.
	 * @param key to get the message for.
	 * @param values  to set for place holders in the message.
	 * @return Message with replaced place holders.
	 */
	public String getMessage(String key, Object... values) {
		String msg = bundle.getString(key);
		if (values.length > 0) {
			return MessageFormat.format(msg, values);
		}
		return msg;
	}
	
}
