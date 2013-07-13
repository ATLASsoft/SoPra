package de.atlassoft.util;

import java.io.IOException;
import java.lang.reflect.Field;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.io.persistence.PersistenceService;
import de.atlassoft.io.persistence.PersistenceServiceImpl;
import de.atlassoft.model.Node;
import de.atlassoft.model.Path;
import de.atlassoft.model.RailwaySystem;
import de.hohenheim.modell.RouteSectionMarker;

abstract class SaveDataAssistent {
	
	private static void createRailSys1() throws IOException {
		RailwaySystem railSys = new RailwaySystem("RailSys 1");

		// create nodes
		Node node1 = new Node("Node 1", 15, 23, 15, 15);
		railSys.addNode(node1);
		Node node2 = new Node("Node 2", 555, 200, 15, 15);
		railSys.addNode(node2);
		Node node3 = new Node("Node 3", 400, 300, 15, 15);
		railSys.addNode(node3);
		Node node4 = new Node("Node 4", 555, 400, 15, 15);
		railSys.addNode(node4);
		Node node5 = new Node("Node 5", 100, 53, 15, 15);
		railSys.addNode(node5);

		// creates paths
		Path path = new Path(node1, node5, 120);
		railSys.addPath(path);
		path = new Path(node5, node3, 200);
		railSys.addPath(path);
		path = new Path(node4, node3, 5);
		railSys.addPath(path);
		path = new Path(node3, node2, 500);
		railSys.addPath(path);
		path = new Path(node2, node4, 500);
		railSys.addPath(path);

		new PersistenceServiceImpl().saveRailwaySystem(railSys);
	}
	
	private static void createRailSysNodeServerLarge() throws IOException {
		RailwaySystem railSys = new RailwaySystem("NodeServer large");
		
		// create nodes
		Node m_1 = new Node("1", 75, 40); railSys.addNode(m_1);
	    Node m_2 = new Node("2", 25, 100); railSys.addNode(m_2);	    
	    Node m_3 = new Node("3",25, 200); railSys.addNode(m_3);	    
	    Node m_4 = new Node("4", 25, 275); railSys.addNode(m_4);	    
	    Node m_5 = new Node("5", 25, 350); railSys.addNode(m_5);
	    Node m_6 = new Node("6", 75, 400); railSys.addNode(m_6);	    
	    Node m_7 = new Node("7", 150, 400); railSys.addNode(m_7);    
	    Node m_8 = new Node("8", 400, 400); railSys.addNode(m_8);
	    Node m_9 = new Node("9", 200, 375); railSys.addNode(m_9);
	    Node m_10 = new Node("10", 375, 375); railSys.addNode(m_10);
	    Node m_11 = new Node("11", 175, 425); railSys.addNode(m_11);
	    Node m_12 = new Node("12", 200, 450); railSys.addNode(m_12);
	    Node m_13 = new Node("13", 425, 450); railSys.addNode(m_13);
	    Node m_14 = new Node("14", 150, 450); railSys.addNode(m_14);
	    Node m_15 = new Node("15", 175, 475); railSys.addNode(m_15);
	    Node m_16 = new Node("16", 400, 475); railSys.addNode(m_16);
	    Node m_17 = new Node("17", 460, 450); railSys.addNode(m_17);
	    Node m_18 = new Node("18", 480, 425); railSys.addNode(m_18);
	    Node m_19 = new Node("19", 500, 400); railSys.addNode(m_19);
	    Node m_20 = new Node("20", 450, 375); railSys.addNode(m_20);
	    Node m_21 = new Node("21", 515, 425); railSys.addNode(m_21);
	    Node m_22 = new Node("22", 535, 400); railSys.addNode(m_22);
	    Node m_23 = new Node("23", 570, 350); railSys.addNode(m_23);
	    Node m_24 = new Node("24", 540, 350); railSys.addNode(m_24);
	    Node m_25 = new Node("25", 540, 275); railSys.addNode(m_25);
	    Node m_26 = new Node("26", 540, 200); railSys.addNode(m_26);
	    Node m_27 = new Node("27", 570, 275); railSys.addNode(m_27);
	    Node m_28 = new Node("28", 570, 200); railSys.addNode(m_28);
	    Node m_29 = new Node("29", 100, 450); railSys.addNode(m_29);
	    Node m_30 = new Node("30", 100, 475); railSys.addNode(m_30);
	    Node m_31 = new Node("31", 570, 140); railSys.addNode(m_31);
	    Node m_32 = new Node("32", 540, 140); railSys.addNode(m_32);
	    Node m_33 = new Node("33", 500, 100); railSys.addNode(m_33);
	    Node m_34 = new Node("34", 500, 70); railSys.addNode(m_34);
	    Node m_35 = new Node("35", 450, 100); railSys.addNode(m_35);
	    Node m_36 = new Node("36", 420, 125); railSys.addNode(m_36);
	    Node m_37 = new Node("37", 420, 100); railSys.addNode(m_37);
	    Node m_38 = new Node("38", 420, 70); railSys.addNode(m_38);
	    Node m_39 = new Node("39", 200, 70); railSys.addNode(m_39);
	    Node m_40 = new Node("40", 225, 100); railSys.addNode(m_40);
	    Node m_41 = new Node("41", 450, 70); railSys.addNode(m_41);
	    Node m_42 = new Node("42", 420, 40); railSys.addNode(m_42);
	    Node m_43 = new Node("43", 175, 40); railSys.addNode(m_43);
	    Node m_44 = new Node("44", 250, 125); railSys.addNode(m_44);
	    Node m_45 = new Node("45", 175, 100); railSys.addNode(m_45);
	    Node m_46 = new Node("46", 450, 475); railSys.addNode(m_46);
	    Node m_47 = new Node("47", 125, 375); railSys.addNode(m_47);
	    Node m_48 = new Node("48", 100, 350); railSys.addNode(m_48);
	    Node m_49 = new Node("49", 100, 300); railSys.addNode(m_49);
	    Node m_50 = new Node("50", 125,275); railSys.addNode(m_50);
	    Node m_51 = new Node("51", 125,225); railSys.addNode(m_51);
	    Node m_52 = new Node("52", 100, 200); railSys.addNode(m_52);
	    Node m_53 = new Node("53", 75, 175); railSys.addNode(m_53);
	    Node m_54 = new Node("54", 100, 125); railSys.addNode(m_54);
	    Node m_55 = new Node("55", 75, 125); railSys.addNode(m_55);
	    Node m_56 = new Node("56", 175, 400); railSys.addNode(m_56);
        
	    // create paths
	    railSys.addPath(new Path( m_55, m_53, 100.0));
	    railSys.addPath(new Path( m_53, m_52, 100.0));
	    railSys.addPath(new Path( m_54, m_52, 100.0));
	    railSys.addPath(new Path( m_52, m_49, 100.0));
	    railSys.addPath(new Path( m_49, m_48, 100.0));
	    railSys.addPath(new Path( m_48, m_47, 100.0));
	    railSys.addPath(new Path( m_47, m_9, 100.0));
	    railSys.addPath(new Path( m_49, m_50, 100.0));
	    railSys.addPath(new Path( m_50, m_51, 100.0));
	    railSys.addPath(new Path( m_51, m_52, 100.0));
	    railSys.addPath(new Path( m_42, m_43, 100.0));
	    railSys.addPath(new Path( m_16, m_46, 100.0));
	    railSys.addPath(new Path( m_14, m_29, 100.0));
	    railSys.addPath(new Path( m_30, m_15, 100.0));
	    railSys.addPath(new Path( m_43, m_1, 100.0));
	    railSys.addPath(new Path( m_39, m_40, 100.0));
	    railSys.addPath(new Path( m_40, m_44, 100.0));
	    railSys.addPath(new Path( m_44, m_36, 100.0));
	    railSys.addPath(new Path( m_40, m_37, 100.0));
	    railSys.addPath(new Path( m_35, m_33, 100.0));
	    railSys.addPath(new Path( m_32, m_26, 100.0));
	    railSys.addPath(new Path( m_38, m_41, 100.0));
	    railSys.addPath(new Path( m_41, m_34, 100.0));
	    railSys.addPath(new Path( m_31, m_28, 100.0));
	    railSys.addPath(new Path( m_27, m_28, 100.0));
	    railSys.addPath(new Path( m_23, m_27, 100.0));
	    railSys.addPath(new Path( m_41, m_42, 100.0));
	    railSys.addPath(new Path( m_38, m_39, 100.0));
	    railSys.addPath(new Path( m_40, m_45, 100.0));
	    railSys.addPath(new Path( m_39, m_43, 100.0));
	    railSys.addPath(new Path( m_31, m_34, 100.0));
	    railSys.addPath(new Path( m_32, m_33, 100.0));
	    railSys.addPath(new Path( m_35, m_38, 100.0));
	    railSys.addPath(new Path( m_35, m_37, 100.0));
	    railSys.addPath(new Path( m_24, m_25, 100.0));
	    railSys.addPath(new Path( m_25, m_26, 100.0));
	    railSys.addPath(new Path( m_21, m_22, 100.0));
	    railSys.addPath(new Path( m_22, m_23, 100.0));
	    railSys.addPath(new Path( m_18, m_21, 100.0));
	    railSys.addPath(new Path( m_19, m_24, 100.0));
	    railSys.addPath(new Path( m_10, m_20, 100.0));
	    railSys.addPath(new Path( m_11, m_12, 100.0));
	    railSys.addPath(new Path( m_12, m_14, 100.0));
	    railSys.addPath(new Path( m_14, m_15, 100.0));
	    railSys.addPath(new Path( m_15, m_16, 100.0));
	    railSys.addPath(new Path( m_12, m_13, 100.0));
	    railSys.addPath(new Path( m_13, m_17, 100.0));
	    railSys.addPath(new Path( m_17, m_18, 100.0));
	    railSys.addPath(new Path( m_18, m_19, 100.0));
	    railSys.addPath(new Path( m_16, m_13, 100.0));
	    railSys.addPath(new Path( m_8, m_19, 100.0));
	    railSys.addPath(new Path( m_11, m_18, 100.0));
	    railSys.addPath(new Path( m_7, m_11, 100.0));
	    railSys.addPath(new Path( m_1, m_2, 100.0));	    
	    railSys.addPath(new Path( m_2, m_3, 100.0));
	    railSys.addPath(new Path( m_4, m_3, 100.0));
	    railSys.addPath(new Path( m_4, m_5, 100.0));
	    railSys.addPath(new Path( m_5, m_6, 100.0));
	    railSys.addPath(new Path( m_7, m_6, 100.0));
	    railSys.addPath(new Path( m_56, m_7, 100.0));
	    railSys.addPath(new Path( m_56, m_8, 100.0));
	    railSys.addPath(new Path( m_9, m_56, 100.0));
	    railSys.addPath(new Path( m_8, m_10, 100.0));
	    railSys.addPath(new Path( m_9, m_10, 100.0));
	  
	    new PersistenceServiceImpl().saveRailwaySystem(railSys);
	}
	
	private static void createAIChallenge() throws IOException {
		RailwaySystem railSys = new RailwaySystem("AI Challenge 1");
		
		Node n1 = new Node("1", 25, 100); railSys.addNode(n1);
		Node n2 = new Node("2", 75, 100); railSys.addNode(n2);
		Node n3 = new Node("3", 175, 100); railSys.addNode(n3);
		Node n4 = new Node("4", 250, 100); railSys.addNode(n4);
		Node n5 = new Node("5", 175, 150); railSys.addNode(n5);
		Node n6 = new Node("6", 175, 225); railSys.addNode(n6);
		Node n7 = new Node("7", 275, 150); railSys.addNode(n7);
		Node n8 = new Node("8", 275, 100); railSys.addNode(n8);
		Node n9 = new Node("9", 275, 50); railSys.addNode(n9);
		Node n10 = new Node("10", 75, 50); railSys.addNode(n10);
//		Node n = new Node("", , ); railSys.addNode(n);
		
		
        
	    // create paths
	    railSys.addPath(new Path(n1, n2, 100.0));
	    railSys.addPath(new Path(n2, n3, 100.0));
	    railSys.addPath(new Path(n3, n4, 100.0));
	    railSys.addPath(new Path(n3, n5, 100.0));
	    railSys.addPath(new Path(n5, n6, 100.0));
	    railSys.addPath(new Path(n5, n7, 100.0));
	    railSys.addPath(new Path(n7, n8, 100.0));
	    railSys.addPath(new Path(n8, n9, 100.0));
	    railSys.addPath(new Path(n9, n10, 100.0));
	    railSys.addPath(new Path(n10, n2, 100.0));
//	    railSys.addPath(new Path(n, n, 100.0));
//	    railSys.addPath(new Path(n, n, 100.0));
		
		new PersistenceServiceImpl().saveRailwaySystem(railSys);
	}
	
	public static void main(String[] args) throws IOException {
//		createRailSys1();
//		createRailSysNodeServerLarge();
//		createAIChallenge();
	}
}
