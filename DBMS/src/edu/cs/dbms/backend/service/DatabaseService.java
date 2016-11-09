package edu.cs.dbms.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.*;
import org.jdom2.Document;
import org.jdom2.Element;

import edu.cs.dbms.backend.util.Config;

public class DatabaseService extends XMLFileService{

	//adatbazis letrehozasa
	public void createDatabase(String databaseName) throws DatabaseException{
		
		Document doc = getDocument();
		Element root = doc.getRootElement();
		if(!searchDatabase(root, databaseName)){
			Element database = new Element(Config.DATABASE_TAG);
			database.setAttribute(new Attribute(Config.DATABASE_ID, databaseName));
			root.addContent(database);
			save(doc);
		}else{
			throw new DatabaseException("Database alredy exist!");
		}
		
	}
	
	//Adatbazis megkeresese es torlese
	public void dropDatabase(String databaseName)throws DatabaseException{
		
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			if(e.getAttributeValue(Config.DATABASE_ID).equals(databaseName)){
				listDatabase.remove(e);
			}
		}
		save(doc);
	}
	
	//megnezem, hogy van-e egy adott nevu adatbazis
	public boolean searchDatabase(Element root, String databaseName){
		
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			if(e.getAttributeValue(Config.DATABASE_ID).equals(databaseName)){
				return true;
			}
		}
		return false;
	}
	
	public List<String> getDatabase(){
		
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> list = root.getChildren();
		List<String> databaseList = new ArrayList<String>();
		
		for(Element e : list){
			databaseList.add(e.getAttributeValue(Config.DATABASE_ID));
		}
		return databaseList;
	}
	
}
