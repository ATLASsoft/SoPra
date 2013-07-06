package de.atlassoft.ui;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.model.Node;
import de.atlassoft.model.Path;
import de.atlassoft.model.RailwaySystem;
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;
import de.atlassoft.util.ImageHelper;
import de.hohenheim.view.map.NodeMap;

/**
 * This class creates the composite for creating a new railway system.
 * 
 * @author Silvan Haeussermann
 */
public class RailSysComposite {

	private Shell shell;
	private Composite mainComposite, railSysComposite;
	private StackLayout layout;
	private ApplicationService applicationService;
	private I18NService I18N;
	private NodeMap map;
	private RailwaySystem railSys;
	private Text railSysNameText, stationNameText, xCoord, yCoord, topSpeed;
	private boolean manualCoordActive;
	private Button fromMap, addStation, addPath, save;
	private Label errorRailSysName, errorStationName;
	private List<Node> nodeList;
	private Combo startCombo, endCombo;
	
	/**
	 * The constructor for the class RailSysComposite.
	 * 
	 * @param mainComposite
	 * 		The main composite of the application.
	 * @param layout
	 * 		The stack layout of the main window.
	 * @param applicationService
	 * 		The ApplicationService of the application. 
	 */
	public RailSysComposite(Shell shell, Composite mainComposite, StackLayout layout, ApplicationService applicationService) {
		this.shell = shell;
		this.mainComposite = mainComposite;
		this.applicationService = applicationService;
		this.layout = layout;
		railSysComposite = new Composite(mainComposite, SWT.BORDER);
		I18N = I18NSingleton.getInstance();
		railSys = new RailwaySystem("temp");
		map = railSys.getNodeMap();
		manualCoordActive = false;
		nodeList = new ArrayList<Node>();
		
		railSysComposite.setLayout(new GridLayout(2, false));
		initUI();
	}
	
	private void initUI() {
		//Controls
		Composite buttonComposite = new Composite(railSysComposite, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(SWT.NULL, SWT.TOP, false, true));
		GridLayout buttonCompositeLayout = new GridLayout();
		buttonCompositeLayout.marginRight = 15;
		buttonCompositeLayout.marginTop = 9;
		buttonCompositeLayout.numColumns = 2;
		buttonCompositeLayout.verticalSpacing = 8;
		buttonComposite.setLayout(buttonCompositeLayout);
		
		//RailSys Name
		createQuestionMark(buttonComposite, I18N.getMessage("RailSysComposite.Help.Name"));
		
		Composite railSysNameComposite = new Composite(buttonComposite, SWT.BORDER);
		railSysNameComposite.setLayoutData(new GridData(SWT.FILL, SWT.NULL, true, false));
		railSysNameComposite.setLayout(new GridLayout(2, false));
		
		Label railSysLabel = new Label(railSysNameComposite, SWT.NONE);
		railSysLabel.setText(I18N.getMessage("RailSysComposite.Railsystem"));
		railSysLabel.setLayoutData(new GridData(SWT.FILL, SWT.NULL, true, false, 2, 1));
		
		Label railSysNameLabel = new Label(railSysNameComposite, SWT.NONE);
		railSysNameLabel.setText(I18N.getMessage("RailSysComposite.Name"));
		
			//Name of the railSys and on-the-fly correction
		Composite textErrorComposite = new Composite(railSysNameComposite, SWT.NONE);
		textErrorComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		textErrorComposite.setLayout(new GridLayout(1, false));
		
		railSysNameText = new Text(textErrorComposite, SWT.BORDER);
		railSysNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		railSysNameText.addListener(SWT.KeyUp, new Listener() {
			public void handleEvent(Event e) {
		    	Boolean twice = false;
		    	if (railSysNameText.getCharCount() == 0) {
		    		errorRailSysName.setText(I18N.getMessage("ScheduleComposite.ErrorField.NoName"));
		    		errorRailSysName.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		    		save.setEnabled(false);
		    		return;
		    	}
		    	for (String temp : applicationService.getModel().getRailwaySystemIDs()) {
		    		if (temp.toLowerCase().equals(railSysNameText.getText().toLowerCase().trim())) {
		    			twice = true;
		    		}
		    	}
		    	if (twice == true) {
		    		errorRailSysName.setText(I18N.getMessage("ScheduleComposite.ErrorField.NameNotAvailable"));
		    		errorRailSysName.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		   		  	save.setEnabled(false);
		    	} else {
		    		errorRailSysName.setText(I18N.getMessage("ScheduleComposite.ErrorField.NameAvailable"));
		    		errorRailSysName.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
		    		save.setEnabled(true);
		    	}
			}
		});
		
		errorRailSysName = new Label(textErrorComposite, SWT.NONE);
		errorRailSysName.setText(I18N.getMessage("RailSysComposite.Help.NoName"));
		errorRailSysName.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		
		//Station Buttons
		createQuestionMark(buttonComposite, I18N.getMessage("RailSysComposite.Help.Station"));
		
		Composite stationButtons = new Composite(buttonComposite, SWT.BORDER);
		stationButtons.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout stationButtonsLayout = new GridLayout();
		stationButtonsLayout.numColumns = 3;
		stationButtons.setLayout(stationButtonsLayout);
		
		Label stationLabel = new Label(stationButtons, SWT.NONE);
		stationLabel.setText(I18N.getMessage("RailSysComposite.NewStation"));
		stationLabel.setLayoutData(new GridData(SWT.FILL, SWT.NULL, true, false, 3, 1));
		
		Label stationNameLabel = new Label(stationButtons, SWT.NONE);
		stationNameLabel.setText(I18N.getMessage("RailSysComposite.Name"));
		
			//Name of the station and on-the-fly correction
		Composite stationNameComposite = new Composite(stationButtons, SWT.NONE);
		stationNameComposite.setLayout(new GridLayout());
		GridData stationNameData = new GridData();
		stationNameData.horizontalSpan = 2;
		stationNameData.horizontalAlignment = SWT.FILL;
		stationNameComposite.setLayoutData(stationNameData);
		
		stationNameText = new Text(stationNameComposite, SWT.BORDER);
		stationNameText.setLayoutData(new GridData(SWT.FILL, SWT.NULL, true, false));
		stationNameText.addListener(SWT.KeyUp, new Listener() {
			public void handleEvent(Event e) {
		    	Boolean twice = false;
		    	if (stationNameText.getCharCount() == 0) {
		    		errorStationName.setText(I18N.getMessage("ScheduleComposite.ErrorField.NoName"));
		    		errorStationName.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		    		addStation.setEnabled(false);
		    		return;
		    	}
		    	for (Node temp : nodeList) {
		    		if (temp.getName().toLowerCase().equals(stationNameText.getText().toLowerCase().trim())) {
		    			twice = true;
		    		}
		    	}
		    	if (twice == true) {
		    		checkStationButton();
		    		errorStationName.setText(I18N.getMessage("ScheduleComposite.ErrorField.NameNotAvailable"));
		    		errorStationName.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		    		addStation.setEnabled(false);
		    	} else {
		    		errorStationName.setText(I18N.getMessage("ScheduleComposite.ErrorField.NameAvailable"));
		    		errorStationName.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
		    		checkStationButton();
		    	}
			}
		});
		
		errorStationName = new Label(stationNameComposite, SWT.NONE);
		errorStationName.setText(I18N.getMessage("RailSysComposite.Help.NoName"));
		errorStationName.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		
		Label coordsNameLabel = new Label(stationButtons, SWT.NONE);
		coordsNameLabel.setText(I18N.getMessage("RailSysComposite.Coordinates"));
		
		Composite coordComposite = new Composite(stationButtons, SWT.NONE);
		coordComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.NULL, true, false, 2, 1));
		coordComposite.setLayout(new RowLayout());
		
			//coord buttons
		xCoord = new Text(coordComposite, SWT.BORDER);
		xCoord.setToolTipText(I18N.getMessage("RailSysComposite.XCoordinate"));
		xCoord.setLayoutData(new RowData(22, 15));
		xCoord.setText("0");
		xCoord.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				//catches non numbers
				if (!Character.isDigit(e.character) && !Character.isISOControl(e.character)) {
		          e.doit = false;
		          Display.getCurrent().beep();
		        }
				//catches numbers higher than 600
				else if(e.text.equals("") == false) {
					final String oldS = xCoord.getText();
					String newS = oldS.substring(0, e.start) + e.text + oldS.substring(e.end);
					if(Integer.parseInt(newS) > 600) {
						e.doit = false;
						Display.getCurrent().beep();
					}
				}
			}
		});
		xCoord.addListener(SWT.KeyUp, new Listener() {
			//Checks if the text area is empty
			@Override
			public void handleEvent(Event event) {
				checkStationButton();
			}
		});
		
		yCoord = new Text(coordComposite, SWT.BORDER);
		yCoord.setToolTipText(I18N.getMessage("RailSysComposite.YCoordinate"));
		yCoord.setLayoutData(new RowData(22, 15));
		yCoord.setText("0");
		yCoord.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				//catches non numbers
				if (!Character.isDigit(e.character) && !Character.isISOControl(e.character)) {
		          e.doit = false;
		          Display.getCurrent().beep();
		        }
				//catches numbers higher than 600
				else if(e.text.equals("") == false) {
					final String oldS = yCoord.getText();
					String newS = oldS.substring(0, e.start) + e.text + oldS.substring(e.end);
					if(Integer.parseInt(newS) > 500) {
						e.doit = false;
						Display.getCurrent().beep();
					}
				}
			}
		});
		yCoord.addListener(SWT.KeyUp, new Listener() {
			//Checks if the text area is empty
			@Override
			public void handleEvent(Event event) {
				checkStationButton();
			}
		});
		
		fromMap = new Button(coordComposite, SWT.TOGGLE);
		fromMap.setLayoutData(new RowData(22, 22));
		fromMap.setImage(ImageHelper.getImage("crosshair"));
		fromMap.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!manualCoordActive) {
					manualCoordActive = true;
				} else {
					manualCoordActive = false;
				}
			}
		});
		
		addStation = new Button(stationButtons, SWT.PUSH);
		addStation.setText(I18N.getMessage("RailSysComposite.AddStation"));
		addStation.setLayoutData(new GridData(SWT.FILL, SWT.NULL, true, false, 3, 1));
		addStation.setEnabled(false);
		addStation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (Integer.parseInt(xCoord.getText()) > 600 | Integer.parseInt(yCoord.getText()) > 500) {
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
					messageBox.setText(I18N.getMessage("RailSysComposite.Error.RangeTitle"));
					messageBox.setMessage(I18N.getMessage("RailSysComposite.Error.CoordinateRange"));
					messageBox.open();
					return;
				}
				
				Node node = new Node(stationNameText.getText(),
						Integer.parseInt(xCoord.getText()), Integer.parseInt(yCoord.getText()));
				nodeList.add(node);
				railSys.addNode(node);
				
				stationNameText.setText("");
				xCoord.setText("0");
				yCoord.setText("0");
				fromMap.setSelection(false);
	    		errorStationName.setText(I18N.getMessage("ScheduleComposite.ErrorField.NoName"));
	    		errorStationName.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
	    		addStation.setEnabled(false);
	    		
	    		startCombo.add(node.getName());
	    		endCombo.add(node.getName());
	    		if (startCombo.getItemCount() == 2) {
	    			startCombo.setEnabled(true);
	    			startCombo.select(0);
	    			endCombo.setEnabled(true);
	    			endCombo.select(1);
	    			addPath.setEnabled(true);
	    			topSpeed.setEnabled(true);
	    		}
			}
		});
		
		//Path Buttons
		createQuestionMark(buttonComposite, I18N.getMessage("RailSysComposite.Help.Path"));
		
		Composite createPathButtons = new Composite(buttonComposite, SWT.BORDER);
		createPathButtons.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		createPathButtons.setLayout(new GridLayout(2, true));
		
		Label addPathLabel = new Label(createPathButtons, SWT.NONE);
		addPathLabel.setText(I18N.getMessage("RailSysComposite.AddPath"));
		addPathLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		startCombo = new Combo(createPathButtons, SWT.DROP_DOWN|SWT.READ_ONLY);
		startCombo.setEnabled(false);
		startCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (startCombo.getItem(startCombo.getSelectionIndex()).equals
						(endCombo.getItem(endCombo.getSelectionIndex()))) {
					addPath.setEnabled(false);
				}
				else if (existsPath(endCombo.getItem(endCombo.getSelectionIndex()), startCombo.getItem(startCombo.getSelectionIndex()))) {
					addPath.setEnabled(false);
				}
				else {
					addPath.setEnabled(true);
				}
			}
		});
		
		endCombo = new Combo(createPathButtons, SWT.DROP_DOWN|SWT.READ_ONLY);
		endCombo.setEnabled(false);
		endCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (endCombo.getItem(endCombo.getSelectionIndex()).equals
						(startCombo.getItem(startCombo.getSelectionIndex()))) {
					addPath.setEnabled(false);
				}
				else if (existsPath(endCombo.getItem(endCombo.getSelectionIndex()), startCombo.getItem(startCombo.getSelectionIndex()))) {
					addPath.setEnabled(false);
				}
				else {
					if (topSpeed.getCharCount() != 0) {
						addPath.setEnabled(true);
					}
				}
			}
		});
		
		//Top speed label and text field of the path
		Composite topSpeedComposite = new Composite(createPathButtons, SWT.NONE);
		topSpeedComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		topSpeedComposite.setLayout(new GridLayout(3, false));
		
		Label topSpeedLabel = new Label(topSpeedComposite, SWT.NONE);
		topSpeedLabel.setText(I18N.getMessage("RailSysComposite.TopSpeed"));
		
		topSpeed = new Text(topSpeedComposite, SWT.BORDER);
		topSpeed.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		topSpeed.setEnabled(false);
		topSpeed.setText("0");
		topSpeed.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				//catches non numbers
				if (!Character.isDigit(e.character) && !Character.isISOControl(e.character)) {
		          e.doit = false;
		          Display.getCurrent().beep();
		        }
				//catches numbers higher than 300
				else if(e.text.equals("") == false) {
					final String oldS = topSpeed.getText();
					String newS = oldS.substring(0, e.start) + e.text + oldS.substring(e.end);
					if(Integer.parseInt(newS) > 300) {
						e.doit = false;
						Display.getCurrent().beep();
					}
				}
			}
		});
		topSpeed.addListener(SWT.KeyUp, new Listener() {
			public void handleEvent(Event e) {
				if (topSpeed.getCharCount() == 0) {
					addPath.setEnabled(false);
				}
				else {
					if (!startCombo.getItem(startCombo.getSelectionIndex()).equals
							(endCombo.getItem(endCombo.getSelectionIndex()))) {
						addPath.setEnabled(true);
					}
				}
			}
		});
		
		Label kilometerLabel = new Label(topSpeedComposite, SWT.NONE);
		kilometerLabel.setText("km/h");
		
		addPath = new Button(createPathButtons, SWT.PUSH);
		addPath.setText(I18N.getMessage("RailSysComposite.AddPath"));
		addPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		addPath.setEnabled(false);
		addPath.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Node start = getNode(startCombo.getItem(startCombo.getSelectionIndex()));
				Node end = getNode(endCombo.getItem(endCombo.getSelectionIndex()));
				Path path = new Path(start, end , Integer.parseInt(topSpeed.getText()));
				railSys.addPath(path);
				addPath.setEnabled(false);
			}
		});
		
		//Place holder
		Label placeHolder = new Label (buttonComposite, SWT.NONE);
		placeHolder.setVisible(false);
		
		Composite saveCancelComposite = new Composite(buttonComposite, SWT.NONE);
		saveCancelComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		RowLayout saveCancelLayout = new RowLayout();
		saveCancelLayout.justify = true;
		saveCancelComposite.setLayout(saveCancelLayout);
		
		//Save button
		save = new Button(saveCancelComposite, SWT.PUSH);
		save.setText(I18N.getMessage("RailSysComposite.Save"));
		save.setEnabled(false);
		save.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				railSys.setID(railSysNameText.getText());
				applicationService.saveRailwaySystem(railSys);
				HomeScreenComposite homeScreenComposite = new HomeScreenComposite(shell, mainComposite, applicationService);		
	    		layout.topControl = homeScreenComposite.getComposite();
	    		mainComposite.layout();
	    		railSysComposite.dispose();
			}
		});
		
		//Cancel button
		Button cancel = new Button(saveCancelComposite, SWT.PUSH);
		cancel.setText(I18N.getMessage("RailSysComposite.Cancel"));
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				HomeScreenComposite homeScreenComposite = new HomeScreenComposite(shell, mainComposite, applicationService);		
	    		layout.topControl = homeScreenComposite.getComposite();
	    		mainComposite.layout();
	    		railSysComposite.dispose();
			}
		});
		
		//Map
		Canvas c = new Canvas(railSysComposite, SWT.FILL);
	    c.setBackground(ColorConstants.white);
	    c.setBounds(0, 0, 600, 500);
	    GridData canvasData = new GridData(600, 500);
	    canvasData.horizontalIndent = 30;
	    c.setLayoutData(canvasData);
	    c.addMouseMoveListener(new MouseMoveListener() {
			
			@Override
			public void mouseMove(MouseEvent e) {
				if (manualCoordActive) {
					xCoord.setText(Integer.toString(e.x));
					yCoord.setText(Integer.toString(e.y));
				}
			}
		});
	    c.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseUp(MouseEvent e) {
	    		checkStationButton();
	    		manualCoordActive = false;
	    		fromMap.setSelection(false);
	    	}
	    });
	    	
	    map.paintNodeMap(c);
	}
	
	/**
	 * Checks if there is already a path in this railway system.
	 * 
	 * @param start
	 * 		The start node.
	 * @param end
	 * 		The end node.
	 * @return
	 * 		True if there is, false if not.
	 */
	private boolean existsPath(String start, String end) {
		for (Path temp : railSys.getPaths()) {
			if (temp.getStart().getName().equals(start)) {
				if (temp.getEnd().getName().equals(end)) {
					return true;
				}
			}
			else if (temp.getStart().getName().equals(end)) {
				if (temp.getEnd().getName().equals(start)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks if it is allowed to set the addStation button active. If
	 * it is allowed it sets the button active otherwise passive.
	 */
	private void checkStationButton() {
		if (yCoord.getText().equals("") || xCoord.getText().equals("") || stationNameText.getText().equals("")) {
			addStation.setEnabled(false);
		}
		else if (!yCoord.getText().equals("") && !xCoord.getText().equals("") && !stationNameText.getText().equals("")){
			boolean bol = false;
    		for (Node temp : nodeList) {
    			if (temp.getName().toLowerCase().equals(stationNameText.getText().toLowerCase().trim())) {
    				bol = true;
    			}
			}
			if (bol) {
				addStation.setEnabled(false);
			}
			else {
				addStation.setEnabled(true);
			}
		}
	}
	
	/**
	 * Returns the node from the nodeList with the given name.
	 * 
	 * @param name
	 * 		The name of the wanted node.
	 * @return
	 * 		The node with the given name.
	 */
	private Node getNode(String name) {
		for (Node temp : nodeList) {
			if (temp.getName().equals(name)) {
				return temp;
			}
		}
		return null;
	}
	
	/**
	 * Creates a help icon that shows some tips when hovering over it.
	 * 
	 * @param composite
	 * 			The parent composite of the icon.
	 * @param message
	 * 			The message that should be displayed.
	 */
	public void createQuestionMark(Composite composite, String message) {
		final Shell tipShell = new Shell(new Shell(), SWT.TOOL|SWT.ON_TOP);
		tipShell.setVisible(false);
		tipShell.setLayout(new RowLayout());
		
		Label information = new Label(tipShell, SWT.NONE);
		information.setText(message);
		tipShell.layout();
		tipShell.setSize(information.getSize().x + 10, information.getSize().y + 10);
		
		final Label questionLabel = new Label(composite, SWT.NONE);
		questionLabel.setImage(ImageHelper.getImage("questionMarkSmall"));
		questionLabel.addMouseTrackListener(new MouseTrackAdapter() {
			public void mouseEnter(MouseEvent e) {
				PointerInfo a = MouseInfo.getPointerInfo();
				java.awt.Point b = a.getLocation();
				tipShell.setLocation(b.x + 15, b.y + 20);
				tipShell.setVisible(true);
			}
			
			public void mouseExit(MouseEvent e) {
				tipShell.setVisible(false);
			}
		});
	}
	
	/**
	 * Returns the main composite of the RailSysComposite class.
	 * 
	 * @return
	 * 		The composite.
	 */
	public Composite getComposite() {
		return railSysComposite;
	}
}
