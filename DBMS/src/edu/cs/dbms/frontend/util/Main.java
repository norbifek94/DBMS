package edu.cs.dbms.frontend.util;

import edu.cs.dbms.backend.service.XMLFileService;

import edu.cs.dbms.frontend.gui.DbmsFrame;

public class Main {
	public static void main(String[] args) {
		XMLFileService fileService = new XMLFileService();
		fileService.createXMLFile();
		new DbmsFrame();
		
//		KeyValueStoring kvs = new KeyValueStoring();
//		kvs.setPath(Config.KEY_VALUE_PATH_UQ + "db/tb2", "uniq");
//		kvs.setPath(Config.KEY_VALUE_PATH_IF + "db/tb2", "proba");
//		kvs.setPath(Config.KEY_VALUE_PATH_FK + "db_tb2", "tb");
//		Map<String, List<String>> m = kvs.getAll();
//		for(Map.Entry<String, List<String>> entry : m.entrySet()) {
//
//			System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue().toString());
//			
//		}
		
	}
}
