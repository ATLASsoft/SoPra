package de.atlassoft.util;

/**
 * Provides I18N support.
 */
public interface I18NService {

	/**
	 * Returns the message that is associated with the given key. Place holders
	 * in the message are replace with the given objects.
	 * @param key to get the message for.
	 * @param values  to set for place holders in the message.
	 * @return Message with replaced place holders.
	 */
	String getMessage(String key, Object... values);
	
}
