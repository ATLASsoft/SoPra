package de.atlassoft.io.docexport;


import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

import de.atlassoft.model.Node;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.SimulationStatistic;


public class DocExportServicePDFImpl implements DocExportService {
	//TODO Exception Handling!
	PDFCreator pdfCreator = new PDFCreator();
	private static final Path SAVE_DATA_PATH = Paths.get("C:\\SOPRAsavedata");
	private static final Path SCHEDULE_DOC_PATH = SAVE_DATA_PATH.resolve("schedule");
	private static final Path STATISTIC_DOC_PATH = SAVE_DATA_PATH.resolve("simulationStatistic");
	private static final Path DEPARTURE_DOC_PATH = SAVE_DATA_PATH.resolve("departure");
	
	@Override
	public void createStatisticDoc(SimulationStatistic stat) throws DocumentException, MalformedURLException, IOException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(STATISTIC_DOC_PATH.toString() + ".pdf"));
		document.open();
		pdfCreator.addMetaData(document, "SimulationStatistic");
		pdfCreator.addTitlePage(document, "Statistikinformationen", "Dieses Dokument, beinhaltet alle relevanten Informationen zu der Statistik des letzten Simulationdurchlaufs", "");
		pdfCreator.addStatisticContent(document, stat);
		document.close();
		try { 															
			Runtime.getRuntime().exec("cmd.exe /c start " + STATISTIC_DOC_PATH.toString() + ".pdf");
		} 
		catch (Exception e) {System.out.println("Anzeigen fehlgeschlagen"); }
	}

	@Override
	public void createScheduleDoc(ScheduleScheme schedule) throws DocumentException, MalformedURLException, IOException {
	      Document document = new Document();
	      PdfWriter.getInstance(document, new FileOutputStream(SCHEDULE_DOC_PATH.toString() + "_" + schedule.getID() + ".pdf"));
	      document.open();
	      pdfCreator.addMetaData(document, "ScheduleScheme_" + schedule.getID());
	      pdfCreator.addTitlePage(document, "Fahrplaninformationen" , "Dieses Dokument, beinhaltet alle relevanten Informationen zu dem Fahrplan: " , schedule.getID());
	      pdfCreator.addScheduleContent(document, schedule);
	      document.close();	
			try { 															
				Runtime.getRuntime().exec("cmd.exe /c start " + SCHEDULE_DOC_PATH.toString() + "_" + schedule.getID() + ".pdf");
			} 
			catch (Exception e) {System.out.println("Anzeigen fehlgeschlagen"); }
	}

	@Override
	public void createDepartureBoard(Node station, List<ScheduleScheme> scedList) throws DocumentException, MalformedURLException, IOException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(DEPARTURE_DOC_PATH.toString() + "_"  + station.getName() + ".pdf"));
		document.open();
		pdfCreator.addMetaData(document, "DepartureBoard_" + station.getName());
		pdfCreator.addTitlePage(document, "Abfahrtentafel", "Dieses Dokument, beinhaltet alle relevanten Informationen, zu allen Abfahrtszeiten, zu der Station: " , station.getName());
		pdfCreator.addDepartureContent(document, station, scedList);
		document.close();
		try { 															
			Runtime.getRuntime().exec("cmd.exe /c start " + DEPARTURE_DOC_PATH.toString() + "_"  + station.getName() + ".pdf");
		} 
		catch (Exception e) {System.out.println("Anzeigen fehlgeschlagen"); }
	}

}