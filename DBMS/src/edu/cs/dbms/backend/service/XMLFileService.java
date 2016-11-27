package edu.cs.dbms.backend.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import edu.cs.dbms.backend.util.Config;

public class XMLFileService {
	
	private XMLOutputter writer;
	private Document doc;
	
	public XMLFileService(){
		
		writer = new XMLOutputter();
		writer.setFormat(Format.getPrettyFormat());
		
	}
	public void deleteFile(String path){

	}
	//XML file letrehozas ha meg nem letezik es a fo Node letrehozasa (Databases)
	public void createXMLFile() throws DatabaseException{
		Element databases = new Element(Config.XML_PARENT_TAG);
		Document doc = new Document(databases);
		new File(Config.XML_PATH).mkdir();
		File file = new File(Config.XML_NAME);
		if(!file.exists()){
			try {
				writer.output(doc, new FileWriter(file));
			} catch (IOException e) {
				throw new DatabaseException("Can't create XML file!");
			}
		}else{
			System.out.println("XML is already existing!");
		}
	}
	
	public Document getDocument() throws DatabaseException{
		if(doc == null){
			SAXBuilder saxBuilder = new SAXBuilder();
			
			try {
				doc = saxBuilder.build(new FileInputStream(Config.XML_NAME));
			} catch (JDOMException | IOException e) {
				throw new DatabaseException("Can't open XML!");
			}
		}
		return doc;
	}
	
	public void save(Document doc){
		try {
			writer.output(doc, new FileOutputStream(new File(Config.XML_NAME)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
