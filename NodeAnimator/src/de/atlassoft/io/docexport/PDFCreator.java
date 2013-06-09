package de.atlassoft.io.docexport;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;

import de.atlassoft.model.ScheduleScheme;
//TODO: implementieren, Konstruktor fehlt noch.
/**
 * Is used to generate a PDF document.
 * 
 * @author Andreas Szlatki & Linus Neﬂler
 * 
 */
public class PDFCreator {
	
	/**
	 * Represents the path at which the document will be saved.
	 */
	private String fileDirectory;
	
	/**
	 * One of the Fonts used in the PDF document.
	 */
	private Font smallFont;
	
	/**
	 * One of the Fonts used in the PDF document.
	 */
	private Font smallBold;
	
	/**
	 * Generates empty lines in an specific paragraph. If paragraph is null, an
	 * {@link IllegalArgumentException} is thrown.
	 * 
	 * @param paragraph 
	 *            {@link Paragraph} which gets edited.
	 * @param number The number of empty lines which should be added.    
	 *    
	 */
	protected void addEmptyLine (Paragraph paragraph, int number){
		if (paragraph == null) {
			throw new IllegalArgumentException(
					"paragraph must not be null");
		}
		
	}
	
	/**
	 * Generates Meta data to a PDF document.Accessiable in documents property's. If document is null, an
	 * {@link IllegalArgumentException} is thrown.
	 * 
	 * @param document 
	 *            {@link Document} to which the Metadata should be added.   
	 *    
	 */
	protected void addMetaData (Document document) {
		if (document == null) {
			throw new IllegalArgumentException(
					"document must not be null");
		}
		
	}
	
	/**
	 * Generates a title page for a PDF document. If document is null, an
	 * {@link IllegalArgumentException} is thrown.
	 * If the insertion to the document fails an {@link DocumentException} 
	 * is thrown.
	 * 
	 * @param document 
	 *            {@link Document} which gets edited.
	 * @param title The name of the title.
	 * @param description The description to the title page.    
	 *    
	 */
	protected void addTitlePage (Document document, String title, String descrption) {
		if (document == null) {
			throw new IllegalArgumentException(
					"document must not be null");
		}
	}
	
	/**
	 * Adds content to the document. If document is null, an
	 * {@link IllegalArgumentException} is thrown.
	 * If the insertion to the document fails an {@link DocumentException} 
	 * is thrown.
	 * 
	 * @param document 
	 *            {@link Document} which gets edited.
	 * @param data The actual content which should be added.    
	 *    
	 */
	protected void addContent (Document document, Object data) {
		if (document == null) {
			throw new IllegalArgumentException(
					"document must not be null");
		}
	}
	
	/**
	 * Generates a table in the PDF document.If subCatPart is null, an
	 * {@link IllegalArgumentException} is thrown.
	 * If an Element gets added which doesn't fit the tables property's,
	 * an {@link BadElementException} is thrown.
	 * 
	 * @param subCatPart 
	 *            {@link Section} describes the structure and the content
	 *            of the table.   
	 *    
	 */
	protected void createTabel (Section subCatPart) {
		if (subCatPart == null) {
			throw new IllegalArgumentException(
					"document must not be null");
		}
	}
	
	/**
	 * Generates a list in the PDF document.If subCatPart is null, an
	 * {@link IllegalArgumentException} is thrown.
	 * 
	 * @param subCatPart 
	 *            {@link Section} describes the structure and the content
	 *            of the list.   
	 *    
	 */
	protected void createList (Section subCatPart) {
		if (subCatPart == null) {
			throw new IllegalArgumentException(
					"document must not be null");
		}
	}
	

}
