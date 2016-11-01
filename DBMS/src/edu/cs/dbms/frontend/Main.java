package edu.cs.dbms.frontend;

import edu.cs.dbms.backend.Attribute;
import edu.cs.dbms.backend.Database;
import edu.cs.dbms.backend.DatabaseException;
import edu.cs.dbms.backend.IndexFile;
import edu.cs.dbms.frontend.ui.GUI;

public class Main {
	public static void main(String[] args) {
		Database db = new Database();
		
		try{
			/*db.createDatabase("test");
			db.createTable("test", "testTable", "table.bin");
			db.createAttribute(new Attribute.AttributeBuilder()
											.setAttrName("valami")
											.setIsNull("0")
											.setLength("56")
											.setType("int")
											.setPrimary(true)
											.setUnique(false)
											.setDatabaseName("test")
											.setTableName("testTable")
											.creatAttr());*/
			db.addIndexFile(new IndexFile.IndexFileBuilder()
										.setDatabaseName("test")
										.setTableName("testTable")
										.setIndexName("inde")
										.setKeyLength("64")
										.setUnique("1")
										.setIndexName("BTree")
										.createIndexFile());	
		}catch(DatabaseException e){
			System.out.println(e);
		}
	}
}
