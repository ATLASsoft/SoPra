package de.atlassoft.io.docexport;

import java.io.FileNotFoundException;
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
import de.atlassoft.model.TrainRideStatistic;

public class DocExportServicePDFImpl implements DocExportService {
	//TODO Exception Handling!
	PDFCreator pdfCreator = new PDFCreator();
	private static final Path SAVE_DATA_PATH = Paths.get("C:\\SOPRAsavedata");
	private static final Path SCHEDULE_DOC_PATH = SAVE_DATA_PATH.resolve("schedule");
	private static final Path STATISTIC_DOC_PATH = SAVE_DATA_PATH.resolve("statistic");
	private static final Path DEPARTURE_DOC_PATH = SAVE_DATA_PATH.resolve("departure");
	
	@Override
	public void createStatisticDoc(SimulationStatistic stat) throws DocumentException, MalformedURLException, IOException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(STATISTIC_DOC_PATH.toString() + "_"  + ".pdf"));
		document.open();
		pdfCreator.addTitlePage(document, "Statistikinformationen", "Dieses Dokument, beinhaltet alle relevanten Informationen zu der Statistik", "");
		System.out.print(stat.getStatistics().get(0));//TODO pdfCreator.addStatisticContent(document, data, schedule);
		document.close();
//		stat.
		List<TrainRideStatistic> test = stat.getStatistics();
		test.get(0).getScheduleScheme().getTrainType();
//		test.get(0).getScheduleScheme().get
//		List<Node> stat23 = test.get(0).getStations();
//		test.get(0).getDelay(stat23.get(0));
//		
//		stat.get
	}

	@Override
	public void createScheduleDoc(ScheduleScheme schedule) throws DocumentException, MalformedURLException, IOException {
	      Document document = new Document();
	      PdfWriter.getInstance(document, new FileOutputStream(SCHEDULE_DOC_PATH.toString() + "_" + schedule.getID() + ".pdf"));
	      document.open();
	      pdfCreator.addTitlePage(document, "Fahrplaninformationen" , "Dieses Dokument, beinhaltet alle relevanten Informationen zu dem Fahrplan: " , schedule.getID());
	      pdfCreator.addScheduleContent(document, schedule);
	      document.close();	
	}

	@Override
	public void createDepartureBoard(Node station, List<ScheduleScheme> scedList) throws DocumentException, MalformedURLException, IOException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(DEPARTURE_DOC_PATH.toString() + "_"  + station.getName() + ".pdf"));
		document.open();
		pdfCreator.addTitlePage(document, "Abfahrtentafel", "Dieses Dokument, beinhaltet alle relevanten Informationen zu allen Abfahrtszeiten zu der Station: " , station.getName());
		pdfCreator.addDepartureContent(document, station, scedList);
		document.close();
	}

}