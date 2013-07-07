package de.atlassoft.io.docexport;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import de.atlassoft.model.Node;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.ScheduleType;

//TODO: implementieren, Konstruktor fehlt noch.
/**
 * Is used to generate a PDF document.
 * 
 * @author Andreas Szlatki & Linus Neﬂler
 * 
 */
class PDFCreator {

	/**
	 * One of the Fonts used in the PDF document.
	 */
	private Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
			Font.NORMAL);

	/**
	 * One of the Fonts used in the PDF document.
	 */
	private Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 18,
			Font.BOLD);

	private Font bigBold = new Font(Font.FontFamily.TIMES_ROMAN, 24, Font.BOLD);

	/**
	 * Generates empty lines in an specific paragraph. If paragraph is null, an
	 * {@link IllegalArgumentException} is thrown.
	 * 
	 * @param paragraph
	 *            {@link Paragraph} which gets edited.
	 * @param number
	 *            The number of empty lines which should be added.
	 * 
	 */
	protected void addEmptyLine(Paragraph paragraph, int number) {
		if (paragraph == null) {
			throw new IllegalArgumentException("paragraph must not be null");
		}
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	/**
	 * Generates Meta data to a PDF document.Accessiable in documents
	 * property's. If document is null, an {@link IllegalArgumentException} is
	 * thrown.
	 * 
	 * @param document
	 *            {@link Document} to which the Metadata should be added.
	 * 
	 */
	protected void addMetaData(Document document, String title) {
		if (document == null) {
			throw new IllegalArgumentException("document must not be null");
		}
		document.addTitle(title);
		document.addSubject("Using iText");
		document.addKeywords("Java, PDF, iText");
		document.addAuthor("ATLASsoft");
		document.addCreator("ATLASsoft");
	}

	// protected static Image loadImage(String ref) {
	// Image bimg = null;
	// try {
	//
	// bimg = ImageIO.re
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return bimg;
	// }

	/**
	 * Generates a title page for a PDF document. If document is null, an
	 * {@link IllegalArgumentException} is thrown. If the insertion to the
	 * document fails an {@link DocumentException} is thrown.
	 * 
	 * @param document
	 *            {@link Document} which gets edited.
	 * @param title
	 *            The name of the title.
	 * @param description
	 *            The description to the title page.
	 * @throws DocumentException
	 * @throws IOException
	 * @throws MalformedURLException
	 * 
	 */
	protected void addTitlePage(Document document, String title,
			String description) throws DocumentException,
			MalformedURLException, IOException {
		if (document == null) {
			throw new IllegalArgumentException("document must not be null");
		}
		Paragraph preface = new Paragraph();
		// We add one empty line
		addEmptyLine(preface, 1);
		// Lets write a big header
		preface.add(new Paragraph("                     " + title, bigBold));

		// Description
		addEmptyLine(preface, 10);
		preface.add(new Paragraph(description, bigBold));

		addEmptyLine(preface, 8);
		// TODO Path geht so nicht
		// BufferedImage img =
		// loadImage("C:/Users/Szlatki/git/SoPra/NodeAnimator/img/ATLASsoftLogo.gif");
		// Image imgToAdd =
		// Image.getInstance("C:/Users/Szlatki/git/SoPra/NodeAnimator/img/ATLASsoftLogo.gif");

		document.add(preface);
		// document.add(imgToAdd);
		// Start a new page
		document.newPage();
	}

	protected String getWeekDay(int dayOfWeek) {
		String res = null;
		switch (dayOfWeek) {
		case 1:
			res = "Montag";
			break;
		case 2:
			res = "Dienstag";
			break;
		case 3:
			res = "Mittwoch";
			break;
		case 4:
			res = "Donnerstag";
			break;
		case 5:
			res = "Freitag";
			break;
		case 6:
			res = "Samstag";
			break;
		case 7:
			res = "Sonntag";
			break;
		default:
			res = "Invalid Day";
			break;
		}

		return res;
	}

	/**
	 * Adds content to the document. If document is null, an
	 * {@link IllegalArgumentException} is thrown. If the insertion to the
	 * document fails an {@link DocumentException} is thrown.
	 * 
	 * @param document
	 *            {@link Document} which gets edited.
	 * @param data
	 *            The actual content which should be added.
	 * @throws DocumentException
	 * 
	 */
	protected void addScheduleContent(Document document, ScheduleScheme schedule)
			throws DocumentException {
		if (document == null) {
			throw new IllegalArgumentException("document must not be null");
		}
		String fahrplanTyp;
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		// Einzelfahrt
		if (schedule.getScheduleType() == ScheduleType.SINGLE_RIDE) {
			fahrplanTyp = "Einzelfahrt";

			// Fharplanname
			Paragraph scheduleName = new Paragraph("Fahrplanname :  "
					+ schedule.getID(), smallBold);
			// Fahrplantyp
			Paragraph scheduleType = new Paragraph("Fahrplantyp :  "
					+ fahrplanTyp, smallBold);
			// Fahrtage
			Paragraph drivingDays = new Paragraph("F‰hrt an den Tagen :  ", smallBold);
			List<Integer> dday = schedule.getDays();
			for (int i = 0 ; i < dday.size() ; i++) {
				if (i == dday.size() - 1){
					drivingDays.add(getWeekDay(dday.get(i)));
				} else {
					drivingDays.add(getWeekDay(dday.get(i))+ ", ");
				}
			}
			// Abfahrtszeit der Einzelfahrten
			Paragraph abfahrtszeit = new Paragraph("Abfahrtszeit :  "
					+ dateFormat.format(schedule.getFirstRide().getTime()), smallBold);
			// Streckennetz
			Paragraph railsys = new Paragraph(
					"Gehˆrt zu Streckennetz :  "
							+ schedule.getRailSysID(), smallBold);

			addEmptyLine(railsys, 3);
			addEmptyLine(abfahrtszeit, 1);
			addEmptyLine(scheduleName, 1);
			addEmptyLine(scheduleType, 1);
			addEmptyLine(drivingDays, 1);
			document.add(scheduleName);
			document.add(scheduleType);
			document.add(drivingDays);
			document.add(abfahrtszeit);
			document.add(railsys);

		} // Intervallfahrt
		else {

			fahrplanTyp = "Intervallfahrt";
			// Fharplanname
			Paragraph scheduleName = new Paragraph("Fahrplanname :  "
					+ schedule.getID(), smallBold);
			// Fahrplantyp
			Paragraph scheduleType = new Paragraph("Fahrplantyp :  "
					+ fahrplanTyp, smallBold);
			// Erster Fahrtag
			Paragraph firstDay = new Paragraph("Erste Fahrt :  "
					+ dateFormat.format(schedule.getFirstRide().getTime()), smallBold);
			// Letzter Fahrtag
			Paragraph lastDay = new Paragraph("Letzte Fahrt :  "
					+ dateFormat.format(schedule.getLastRide().getTime()), smallBold);
			// Fahrtage
			Paragraph drivingDays = new Paragraph("F‰hrt an den Tagen :  ", smallBold);
			List<Integer> dday = schedule.getDays();
			for (int i = 0 ; i < dday.size() ; i++) {
				if (i == dday.size() - 1){
					drivingDays.add(getWeekDay(dday.get(i)));
				} else {
					drivingDays.add(getWeekDay(dday.get(i))+ ", ");
				}
			}
			// Intervall
			Paragraph intervall = new Paragraph("F‰hrt alle :  "
					+ schedule.getInterval()
					+ " Minuten", smallBold);
			// Streckennetz
			Paragraph railsys = new Paragraph(
					"Gehˆrt zu Streckennetz :  "
							+ schedule.getRailSysID(), smallBold);
			addEmptyLine(railsys, 3);
			addEmptyLine(scheduleName, 1);
			addEmptyLine(scheduleType, 1);
			addEmptyLine(firstDay, 1);
			addEmptyLine(lastDay, 1);
			addEmptyLine(drivingDays, 1);
			addEmptyLine(intervall, 1);
			document.add(scheduleName);
			document.add(scheduleType);
			document.add(firstDay);
			document.add(lastDay);
			document.add(drivingDays);
			document.add(intervall);
			document.add(railsys);
		}

		// Angefahrene Haltestellen Tabelle
		Paragraph table = new Paragraph("Angefahrene Haltestellen:", smallBold);
		addEmptyLine(table, 2);
		createTable(table, schedule);
		document.add(table);
	}

	/**
	 * Adds content to the document. If document is null, an
	 * {@link IllegalArgumentException} is thrown. If the insertion to the
	 * document fails an {@link DocumentException} is thrown.
	 * 
	 * @param document
	 *            {@link Document} which gets edited.
	 * @param data
	 *            The actual content which should be added.
	 * 
	 */
	protected void addStatisticContent(Document document, Object data,
			ScheduleScheme schedule) {
		if (document == null) {
			throw new IllegalArgumentException("document must not be null");
		}

	}

	/**
	 * Adds content to the document. If document is null, an
	 * {@link IllegalArgumentException} is thrown. If the insertion to the
	 * document fails an {@link DocumentException} is thrown.
	 * 
	 * @param document
	 *            {@link Document} which gets edited.
	 * @param data
	 *            The actual content which should be added.
	 * 
	 */
	protected void addDepartureContent(Document document, Node station,
			List<ScheduleScheme> scedList) {
		if (document == null) {
			throw new IllegalArgumentException("document must not be null");
		}
		List<Integer> relevantSpots = new ArrayList<Integer>();
		for (ScheduleScheme sced : scedList) {
			List<Node> nodeList = sced.getStations();
			for (int i = 0; i < nodeList.size(); i++) {
				if (station.getName().equals(nodeList.get(i).getName())) {
					relevantSpots.add(i);
				}
			}
			sced.getArrivalTimes();
			sced.getIdleTimes();
		}

	}

	/**
	 * Generates a table in the PDF document.If subCatPart is null, an
	 * {@link IllegalArgumentException} is thrown. If an Element gets added
	 * which doesn't fit the tables property's, an {@link BadElementException}
	 * is thrown.
	 * 
	 * @param subCatPart
	 *            {@link Section} describes the structure and the content of the
	 *            table.
	 * 
	 */
	protected void createTable(Paragraph buildTable, ScheduleScheme sced) {
		if (buildTable == null) {
			throw new IllegalArgumentException("document must not be null");
		}
		PdfPTable table = new PdfPTable(3);

		// t.setBorderColor(BaseColor.GRAY);
		// t.setPadding(4);
		// t.setSpacing(4);
		// t.setBorderWidth(1);

		PdfPCell c1 = new PdfPCell(new Phrase("Name"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
		
		if (sced.getScheduleType() == ScheduleType.SINGLE_RIDE){
			c1 = new PdfPCell(new Phrase("Ankunftszeit"));
		} else {
			c1 = new PdfPCell(new Phrase("Ankunft nach (in Minuten)"));
		}
		
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Aufenthaltszeit (in Minuten)"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
		table.setHeaderRows(1);

		List<Integer> arrivalTimesList = sced.getArrivalTimes();
		List<Integer> idleTimesList = sced.getIdleTimes();
		List<Node> nodeList = sced.getStations();
		
		DecimalFormat doubleFormater = new DecimalFormat("#0"); 
		DateFormat arrivalFormat = new SimpleDateFormat("HH:mm");
		double idleMin;
		double arrivalMin;
		Calendar newCal;
		
		for (int i = 0; i < nodeList.size(); i++) {
			
			// Adding the seconds of the i-th arrival time to the start time
			newCal = sced.getFirstRide();
			newCal.add(Calendar.SECOND, arrivalTimesList.get(i));
			
			// Calculating the idle time in minutes
			idleMin = idleTimesList.get(i) / 60.0;
			arrivalMin = arrivalTimesList.get(i) / 60.0;
			
			// adding the Content to the table
			table.addCell(nodeList.get(i).getName());
			
			if (sced.getScheduleType() == ScheduleType.SINGLE_RIDE){
				table.addCell(arrivalFormat.format(newCal.getTime()));
			} else {
				table.addCell(doubleFormater.format(arrivalMin));
			}
			
			table.addCell(doubleFormater.format(idleMin));
		}

		buildTable.add(table);
	}

	/**
	 * Generates a list in the PDF document.If subCatPart is null, an
	 * {@link IllegalArgumentException} is thrown.
	 * 
	 * @param subCatPart
	 *            {@link Section} describes the structure and the content of the
	 *            list.
	 * 
	 */
	protected void createList(Section subCatPart) {
		if (subCatPart == null) {
			throw new IllegalArgumentException("document must not be null");
		}
	}

}
