package edu.cs.dbms.frontend.util;

import edu.cs.dbms.backend.service.XMLFileService;
import edu.cs.dbms.frontend.gui.DbmsFrame;

public class Main {
	public static void main(String[] args) {
		XMLFileService fileService = new XMLFileService();
		fileService.createXMLFile();
		new DbmsFrame();
	}
}
