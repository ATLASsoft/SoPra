package de.atlassoft.io.docexport;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

import de.atlassoft.model.Node;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.SimulationStatistic;
// TODO: Klasse implementieren
public class DocExportServicePDFImpl implements DocExportService {

	PDFCreator pdfCreator = new PDFCreator();
	private static final Path SAVE_DATA_PATH = Paths.get("C:\\SOPRAsavedata");
	private static final Path SCHEDULE_DOC_PATH = SAVE_DATA_PATH.resolve("schedule.pdf");
	private static final Path STATISTIC_DOC_PATH = SAVE_DATA_PATH.resolve("statistic.pdf");
	private static final Path DEPARTURE_DOC_PATH = SAVE_DATA_PATH.resolve("departure.pdf");
	
	@Override
	public void createStatisticDoc(SimulationStatistic stat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createScheduleDoc(ScheduleScheme schedule) throws DocumentException, MalformedURLException, IOException {
	      Document document = new Document();
	      PdfWriter.getInstance(document, new FileOutputStream(SCHEDULE_DOC_PATH.toString()));
	      document.open();
	      pdfCreator.addTitlePage(document, "Schedule", "Test");
	      pdfCreator.addScheduleContent(document, schedule);
	      document.close();	
	}

	@Override
	public void createDepartureBoard(Node station) {
		// TODO Auto-generated method stub
		
	}

}