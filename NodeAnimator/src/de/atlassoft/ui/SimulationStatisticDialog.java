package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.model.Node;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.TrainType;
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;
import de.atlassoft.util.ImageHelper;

/**
 * This class is for the SimulationStatistic Dialog where
 * you can see the Statistic after a Simulation.
 * 
 * @author Tobias Ilg
 */
public class SimulationStatisticDialog {
	private Shell shell;
	private ApplicationService applicationService;
	
	/**
	 * Public constructor for the SimulationStatistic Dialog. Creates a new
	 * SimulationStatistic shell and open it.
	 * 
	 * @param applicationService
	 * 			to access application layer
	 */
	
	public SimulationStatisticDialog (ApplicationService applicationService) {
		this.applicationService = applicationService;
		I18NService I18N = I18NSingleton.getInstance();
		
		Display display = Display.getCurrent();
		shell = new Shell(display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText(I18N.getMessage("SimulationStatisticDialog.Title"));
		shell.setSize(SWT.DEFAULT, SWT.DEFAULT);
		shell.setLayout(new GridLayout(3, false));
		
		initUI();
		MainWindow.center(shell);
		shell.open();
	}
	
	/**
	 * A method which creates the content, 
	 * label, text, buttons and tool tips of
	 * the shell of the SimulationStatistic Dialog.
	 * 
	 */
	private void initUI() {
		final I18NService I18N = I18NSingleton.getInstance();
		
		// First row with three labels for "TotalDelay"
		new Label(shell, SWT.NONE).setText(I18N.getMessage("SimulationStatisticDialog.TotalDelay") + "\t");
		new Label(shell, SWT.NONE).setText(String.valueOf(applicationService.getModel().getStatistic().getTotalDelay()));
		new Label(shell, SWT.NONE).setText("s");
		
		// Second row with three labels for "MeanDelay"
		new Label(shell, SWT.NONE).setText(I18N.getMessage("SimulationStatisticDialog.MeanDelay") + "\t");
		new Label(shell, SWT.NONE).setText(String.valueOf(applicationService.getModel().getStatistic().getMeanDelay()));
		new Label(shell, SWT.NONE).setText("s");
		
		// Third row with three labels for "MeanDelay (TrainType)"
		new Label(shell, SWT.NONE).setText(I18N.getMessage("SimulationStatisticDialog.MeanDelayTrainType") + "\t");
	 	java.util.List<TrainType> trainTypes = applicationService.getModel().getTrainTypes();
		final String[] trainTypeSelection = new String [trainTypes.size()];
		int index = 0;
		for (TrainType type : trainTypes) {
			trainTypeSelection[index] = type.getName();
			index++;
		}
		final Combo comboTrainType = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
	    comboTrainType.setItems(trainTypeSelection);
	    comboTrainType.select(0);
		new Label(shell, SWT.NONE);
		
		// Fourth row with the result of "MeanDelay (TrainType)"
		new Label(shell, SWT.NONE);
		TrainType selectedTrainType = null;
		for (TrainType type : trainTypes) {
			if (type.getName().equals(comboTrainType.getSelection())) {
				selectedTrainType = type;
			}
		}
		new Label(shell, SWT.NONE).setText(String.valueOf(applicationService.getModel().getStatistic().getMeanDelay(selectedTrainType)));
		new Label(shell, SWT.NONE).setText("s");
		
		// Fifth row with "MeanDelay (Node)"
		new Label(shell, SWT.NONE).setText(I18N.getMessage("SimulationStatisticDialog.MeanDelayNode") + "\t");
	 	java.util.List<Node> nodes = applicationService.getModel().getActiveRailwaySys().getNodes();
		final String[] nodeSelection = new String [trainTypes.size()];
		index = 0;
		for (Node node : nodes) {
			nodeSelection[index] = node.getName();
			index++;
		}
		final Combo comboNode = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
	    comboNode.setItems(nodeSelection);
	    comboNode.select(0);
		new Label(shell, SWT.NONE);
		
		// Sixth row with the result of "MeanDelay (Node)"
		new Label(shell, SWT.NONE);
		Node selectedNode = null;
		for (Node node : nodes) {
			if (node.getName().equals(comboNode.getSelection())) {
				selectedNode = node;
			}
		}
		new Label(shell, SWT.NONE).setText(String.valueOf(applicationService.getModel().getStatistic().getMeanDelay(selectedNode)));
		new Label(shell, SWT.NONE).setText("s");
		
		// Seventh row with "MeanDelay (ScheduleScheme)"
		new Label(shell, SWT.NONE).setText(I18N.getMessage("SimulationStatisticDialog.MeanDelayScheduleScheme") + "\t");
	 	java.util.List<ScheduleScheme> scheduleSchemes = applicationService.getModel().getActiveScheduleSchemes();
		final String[] scheduleSchemeSelection = new String [scheduleSchemes.size()];
		index = 0;
		for (ScheduleScheme scheduleScheme : scheduleSchemes) {
			nodeSelection[index] = scheduleScheme.getID();
			index++;
		}
		final Combo comboScheduleScheme = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
		comboScheduleScheme.setItems(scheduleSchemeSelection);
		comboScheduleScheme.select(0);
		new Label(shell, SWT.NONE);
		
		// Eighth row with the result of "MeanDelay (ScheduleScheme)"
		new Label(shell, SWT.NONE);
		ScheduleScheme selectedScheduleScheme = null;
		for (ScheduleScheme scheduleScheme : scheduleSchemes) {
			if (scheduleScheme.getID().equals(comboScheduleScheme.getSelection())) {
				selectedScheduleScheme = scheduleScheme;
			}
		}
		new Label(shell, SWT.NONE).setText(String.valueOf(applicationService.getModel().getStatistic().getMeanDelay(selectedScheduleScheme)));
		new Label(shell, SWT.NONE).setText("s");
		
		// Ninth row with "MeanDelay (ScheduleScheme, Node)"
		new Label(shell, SWT.NONE).setText(I18N.getMessage("SimulationStatisticDialog.MeanDelayScheduleSchemeAndNode") + "\t");
		final Combo comboScheduleScheme2 = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
		comboScheduleScheme2.setItems(scheduleSchemeSelection);
		comboScheduleScheme2.select(0);
		final Combo comboNode2 = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
	    comboNode2.setItems(nodeSelection);
	    comboNode2.select(0);
	    
	    // Tenth row with the result of "MeanDelay (ScheduleScheme, Node)"
		new Label(shell, SWT.NONE);
		ScheduleScheme selectedScheduleScheme2 = null;
		for (ScheduleScheme scheduleScheme : scheduleSchemes) {
			if (scheduleScheme.getID().equals(comboScheduleScheme2.getSelection())) {
				selectedScheduleScheme2 = scheduleScheme;
			}
		}
		Node selectedNode2 = null;
		for (Node node : nodes) {
			if (node.getName().equals(comboNode2.getSelection())) {
				selectedNode2 = node;
			}
		}
		new Label(shell, SWT.NONE).setText(String.valueOf(applicationService.getModel().getStatistic().getMeanDelay(selectedScheduleScheme2, selectedNode2)));
		new Label(shell, SWT.NONE).setText("s");
		
		// Eleventh row with "NumberOfRides"
		new Label(shell, SWT.NONE).setText(I18N.getMessage("SimulationStatisticDialog.NumberOfRides") + "\t");
		new Label(shell, SWT.NONE).setText(String.valueOf(applicationService.getModel().getStatistic().getNumberOfRides()));
		new Label(shell, SWT.NONE).setText(I18N.getMessage("SimulationStatisticDialog.Rides"));
		
		// Twelfth row with Button "Save as PDF"
		new Label(shell, SWT.NONE);
		Button save = new Button(shell, SWT.PUSH);
		save.setImage(ImageHelper.getImage("loadButton"));
		save.setText(I18N.getMessage("SimulationStatisticDialog.Save"));
		new Label(shell, SWT.NONE);
		
		// Function of Save-Button
		save.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {
	       		MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
	       		messageBox.setText(I18N.getMessage("SimulationStatisticDialog.informationSaved"));
        	    messageBox.setMessage(I18N.getMessage("SimulationStatisticDialog.informationStatisticSaved"));
        	    shell.setVisible(false);
        	    messageBox.open();
	            shell.close();
	        }
	    });
		
		shell.pack();
	}
}
