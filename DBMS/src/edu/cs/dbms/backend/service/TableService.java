package edu.cs.dbms.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import edu.cs.dbms.backend.model.Table;
import edu.cs.dbms.backend.util.Config;

public class TableService extends XMLFileService{
	
	//tabla letrehozasa
	public void createTable(Table table)throws DatabaseException{

		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			if(e.getAttributeValue(Config.DATABASE_ID).equals(table.getDatabaseName())){
				if(!searchTable(e, table)){
					Element tableElement = new Element(Config.TABLE_TAG);
					Element struc = new Element(Config.STRUCTURE_TAG);
					Element pk = new Element(Config.PRIMARY_KEY_TAG);
					Element uk = new Element(Config.UNIQUE_KEY_TAG);
					Element fk = new Element(Config.FOREIGN_KEYS_TAG);
					Element indexFile = new Element(Config.INDEX_FILES_TAG);
					tableElement.setAttribute(Config.TABLE_ID, table.getTableName());
					tableElement.setAttribute(Config.TABLE_FILE_NAME, table.getFileName() 
							+ Config.TABLE_FILE_EXTENSION);
					tableElement.addContent(struc);
					tableElement.addContent(pk);
					tableElement.addContent(uk);
					tableElement.addContent(fk);
					tableElement.addContent(indexFile);
					e.addContent(tableElement);
					save(doc);
					return;
				}else{
					throw new DatabaseException("Table alredy exist!");
				}
			}
		}
	}
	
	public void dropTable(Table table) throws DatabaseException{
		
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			if(e.getAttributeValue(Config.DATABASE_ID).equals(table.getDatabaseName())){
				List<Element> listTable = e.getChildren();
				for(Element t: listTable){
					if(t.getAttributeValue(Config.TABLE_ID).equals(table.getTableName())){
						listTable.remove(t);
						save(doc);
						ForeignKeyService foreignKeyService = new ForeignKeyService();
						foreignKeyService.deleteAllForeignKeyTable(table);
						return;
					}
				}
			}
		}
	}
	
	//megnezem, hogy van-e egy adott nevu 
	//tabla az parameterkent kapott adatbazisban
	public boolean searchTable(Element database, Table table) {
			
		List<Element> listTable = database.getChildren();
		for(Element t: listTable){
			if(t.getAttributeValue(Config.TABLE_ID).equals(table.getTableName())){
				return true;
			}
		}
		return false;
	}
	
	public List<String> getTable(String databaseName){
		
		List<String> tableNames = new ArrayList<String>();
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			if(e.getAttributeValue(Config.DATABASE_ID).equals(databaseName)){
				List<Element> listTable = e.getChildren();
				for(Element t: listTable){
					tableNames.add(t.getAttributeValue(Config.TABLE_ID));
				}
			}
		}
		return tableNames;
	}
}
