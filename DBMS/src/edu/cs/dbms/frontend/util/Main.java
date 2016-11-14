package edu.cs.dbms.frontend.util;

import edu.cs.dbms.backend.service.XMLFileService;
import edu.cs.dbms.frontend.gui.DbmsFrame;

public class Main {
	public static void main(String[] args) {
		XMLFileService fileService = new XMLFileService();
		fileService.createXMLFile();
		new DbmsFrame();
		/*
		AttributeService attributeService = new AttributeService();
		Attribute attr = new Attribute.AttributeBuilder()
									  .setDatabaseName("test")
									  .setTableName("table2")
									  .creatAttr();
		
		List<Attribute> attrList = attributeService.getAttribute(attr);
		
		for(Attribute a : attrList){
			System.out.println("-----------------------------------");
			System.out.println("Database: " + a.getDatabaseName());
			System.out.println("Table: " + a.getTableName());
			System.out.println("Name: " + a.getAttrName());
			System.out.println("Type: " + a.getType());
			System.out.println("Length: " + a.getLength());
			System.out.println("Is null: " + a.getIsNull());
			System.out.println("Primary: " + a.isPrimary());
			System.out.println("Foreing: " + a.isForignKey());
			System.out.println("Unique: " + a.isUnique());
		}*/
	}
}
