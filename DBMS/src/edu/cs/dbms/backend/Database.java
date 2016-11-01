package edu.cs.dbms.backend;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.*;
import org.w3c.dom.*;

public class Database {
	
	private static DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	private static TransformerFactory trasformerFact = TransformerFactory.newInstance();
	private Transformer transformer;
	
	private DocumentBuilder docBuilder;
	
	public Database(){
		
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			transformer = trasformerFact.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		} catch (ParserConfigurationException | TransformerConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	//XML file letrehozas ha meg nem letezik es a fo Node letrehozasa (Databases)
	public void createXMLFile() throws DatabaseException{

		try {
			
			Document doc = docBuilder.newDocument();
	        Element rootEle = doc.createElement(Config.XML_PARENT_TAG);

	        doc.appendChild(rootEle);

	        File file = new File(Config.XML_NAME);
	        //ha nem letezik letrehozom
	        if(!file.exists()){
	        	transformer.transform(new DOMSource(doc), 
                                 new StreamResult(new FileOutputStream(file, true)));
	        }else{
	        	throw new DatabaseException("XML is already existing!");
	        }
        } catch (TransformerException | IOException e) {
            System.out.println(e.getMessage());
            throw new DatabaseException("Can't create file, try again later!");
        }
	}
	
	//adatbazis letrehozasa
	public void createDatabase(String databaseName) throws DatabaseException{
		try {
			if(!searchDatabase(databaseName)){
				//megnyitom az XML-t
				Document doc = docBuilder.parse(Config.XML_NAME);
				//keresett tag
				Element datatbasesTag =  
						(Element)doc.getElementsByTagName(Config.XML_PARENT_TAG).item(0);
				//uj tag letrehozas
				Element databaseElement = doc.createElement(Config.DATABASE_TAG);
				//beallitom a nevet
				databaseElement.setAttribute(Config.DATABASE_ID, databaseName);
				datatbasesTag.appendChild(databaseElement);
				//meglevo XML-hez hozzailesztem az uj adatokat
				appendToXML(doc);
			}else{
				throw new DatabaseException(databaseName + " database alredy exist!");
			}
		} catch (SAXException | IOException e1) {
			e1.printStackTrace();
			throw new DatabaseException("Can't create database, try again later!");
		}	
	}
	
	//Adatbazis megkeresese es torlese
	public void dropDatabase(String databaseName)throws DatabaseException{
		
		try {
			//megnyitom az XML file-t
			Document doc = docBuilder.parse(Config.XML_NAME);

			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr;
			NodeList nl;
			
			//mindegyik db-nek a nevet kiszedem
			expr = xpath.compile(Config.DATABASE_SEARCH);
			nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			
			for (int i = 0; i < nl.getLength(); i++){	
			    Node currentItem = nl.item(i);
			    String key = currentItem.getAttributes()
			    		.getNamedItem(Config.DATABASE_ID).getNodeValue();
			    
			    //a megadott nevu db torlese
			    if(key.equals(databaseName)){
			    	currentItem.getParentNode().removeChild(currentItem);
			    	appendToXML(doc);
			    	return;
			    }	
			}
		} catch (SAXException | IOException | XPathExpressionException e1) {
			e1.printStackTrace();
			throw new DatabaseException("Can't drop the database!");
		}
	}
	
	//parameter a felepitett xml
	private void appendToXML(Document doc) throws DatabaseException{

		//a felepittett xml a doc
		DOMSource domSource = new DOMSource(doc);
		File file = new File(Config.XML_NAME);
        StreamResult streamResult = new StreamResult(file);
        if(file.exists()){
        	try {
				transformer.transform(domSource, streamResult);
			} catch (TransformerException e) {
				e.printStackTrace();
				throw new DatabaseException("Can't append to XML!");
			}	
        }
	}
	
	//tabla letrehozasa
	public void createTable(String databaseName, String tableName, String fileName)
								throws DatabaseException{
		
		try {
			
			//megnyitom az XML-t
			Document doc = docBuilder.parse(Config.XML_NAME);

			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr;
			NodeList nl;
			
			//mindegyik db-nek a nevet keresem
			expr = xpath.compile(Config.DATABASE_SEARCH);
			nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			
			for (int i = 0; i < nl.getLength(); i++){
			    Node currentItem = nl.item(i);
			    String key = currentItem.getAttributes()
			    		.getNamedItem(Config.DATABASE_ID).getNodeValue();
			    //a megadott nevu adatbazishoz hozzaadom a tablat
			    if(key.equals(databaseName)){
			    	if(!searchTable(currentItem, tableName)){
				    	Element tableElement = doc.createElement(Config.TABLE_TAG);
				    	Element struc = doc.createElement(Config.STRUCTURE_TAG);
						Element primaryKey = doc.createElement(Config.PRIMARY_KEY_TAG);
						Element uniqueKey = doc.createElement(Config.UNIQUE_KEY_TAG);
						Element indexFiles = doc.createElement(Config.INDEX_FILES_TAG);
						
						tableElement.setAttribute(Config.TABLE_ID, tableName);
						tableElement.setAttribute(Config.TABLE_FILE_NAME, fileName);
						tableElement.appendChild(struc);
						tableElement.appendChild(primaryKey);
						tableElement.appendChild(uniqueKey);
						tableElement.appendChild(indexFiles);
						currentItem.appendChild(tableElement);
			    	}else{
			    		throw new DatabaseException("In " + databaseName 
			    				+ ", " + tableName + " table alredy exist!");
			    	}
			    }	
			}
			appendToXML(doc);
		
		} catch (SAXException | IOException | XPathExpressionException e1) {
			e1.printStackTrace();
			throw new DatabaseException("Can't create database, try again later!");
		}
	}
	
	public void dropTable(String databaseName, String tableName) throws DatabaseException{
		try {
			//if(searchTable(databaseName, tableName))
			//megnyitom az XML-t
			Document doc = docBuilder.parse(Config.XML_NAME);

			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr;
			NodeList nl;
			
			//mindegyik db-nek a nevet keresem
			expr = xpath.compile(Config.DATABASE_SEARCH);
			nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			
			for (int i = 0; i < nl.getLength(); i++){
			    Node currentDatabase = nl.item(i);
			    String keyDatabase = currentDatabase.getAttributes()
			    		.getNamedItem(Config.DATABASE_ID).getNodeValue();
			    
			    //a megadott nevu adatbazishoz hozzaadom a tablat
			    if(keyDatabase.equals(databaseName)){
			    	//a megadott tablat keresem	
					NodeList nlTable = currentDatabase.getChildNodes();
					
					for (int j = 0; j < nlTable.getLength(); j++){
					    Node currentTable = nlTable.item(j);
					    
					    //az indentalast is node-nak veszi ezert ellenorzom h tabla - e
					    if(currentTable.getNodeName() == (Config.TABLE_TAG)){
					    	String keyTable = currentTable.getAttributes()
						    		.getNamedItem(Config.TABLE_ID).getNodeValue();
						    System.out.println(currentTable.getNodeName());
						   
						    //a megadott nevu tabla torlese
						    if(keyTable.equals(tableName)){
						    	currentTable.getParentNode().removeChild(currentTable);
						    	appendToXML(doc);
						    }
					    }	
					}
			    }	
			}
			appendToXML(doc);

		} catch (SAXException | IOException | XPathExpressionException e1) {
			e1.printStackTrace();
			throw new DatabaseException("Can't create database, try again later!");
		}	
	}
	
	//megnezem, hogy van-e egy adott nevu adatbazis
	public boolean searchDatabase(String databaseName){
		
		try {
			//megnyitom az XML file-t
			Document doc = docBuilder.parse(Config.XML_NAME);

			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr;
			NodeList nl;
			
			//mindegyik db-nek a nevet kiszedem
			expr = xpath.compile(Config.DATABASE_SEARCH);
			nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			
			for (int i = 0; i < nl.getLength(); i++){	
			    Node currentItem = nl.item(i);
			    String key = currentItem.getAttributes()
			    		.getNamedItem(Config.DATABASE_ID).getNodeValue();
			    
			    if(key.equals(databaseName)){
			    	return true;
			    }	
			}
		} catch (SAXException | IOException | XPathExpressionException e1) {
			e1.printStackTrace();
		}
		return false;
	}
	
	//megnezem, hogy van-e egy adott nevu 
	//tabla az parameterkent kapott adatbazisban
	public boolean searchTable(Node database, String tableName) {
		
		NodeList nlTable = database.getChildNodes();
		for (int j = 0; j < nlTable.getLength(); j++){
		    Node currentTable = nlTable.item(j);
		    
		    //az indentalast is node-nak veszi ezert ellenorzom h tabla - e
		    if(currentTable.getNodeName() == (Config.TABLE_TAG)){
		    	String keyTable = currentTable.getAttributes()
			    		.getNamedItem(Config.TABLE_ID).getNodeValue();
			    
		    	//a megadott nevu tabla letezike
			    if(keyTable.equals(tableName)){
			    	return true;
			    }
		    }	
		}
		
		return false;
	}

	//attributum letehozasa
	public void createAttribute(Attribute attribute) throws DatabaseException{
		
		try {
			boolean saved = false;
			//megnyitom az XML-t
			Document doc = docBuilder.parse(Config.XML_NAME);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr;
			NodeList nlDatabase;
			
			//adatbazist keresem
			expr = xpath.compile(Config.DATABASE_SEARCH);
			nlDatabase = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			
			for (int i = 0; i < nlDatabase.getLength(); i++){
			    Node currentDatabase = nlDatabase.item(i);
			    String keyDatabase = currentDatabase.getAttributes()
			    		.getNamedItem(Config.DATABASE_ID).getNodeValue();
			    
			    if(keyDatabase.equals(attribute.getDatabaseName())){
			    	NodeList nlTable = currentDatabase.getChildNodes();
			    	//tablat keresem
					for (int j = 0; j < nlTable.getLength(); j++){
					    Node currentTable = nlTable.item(j);
					    System.out.println(currentTable.getNodeName());
					    //az indentalast is node-nak veszi ezert ellenorzom h tabla - e
					    if(currentTable.getNodeName() == Config.TABLE_TAG){
					    	String keyTable = currentTable.getAttributes()
						    		.getNamedItem(Config.TABLE_ID).getNodeValue();
					    	
						    if(keyTable.equals(attribute.getTableName())){
						    	NodeList nl = currentTable.getChildNodes();
						    	
						    	//structure taget keresm
						    	for(int k = 0; k < nl.getLength(); k++){
						    		Node currentNode = nl.item(k);
						    		
						    		if(currentNode.getNodeName() == Config.STRUCTURE_TAG){
						    			if(!searchAttribute(currentNode, attribute.getAttrName())){
							    			Element attrElement = doc.createElement(Config.ATTRIBUTE_TAG);
									    	attrElement.setAttribute(Config.ATTRIBUTE_ID, attribute.getAttrName());
									    	attrElement.setAttribute(Config.ATTRIBUTE_TYPE, attribute.getType());
									    	attrElement.setAttribute(Config.ATTRIBUTE_LENGTH, attribute.getLength());
									    	attrElement.setAttribute(Config.ATTRIBUTE_IS_NULL, attribute.getIsNull());
											currentNode.appendChild(attrElement);
											saved = true;
						    			}else{
						    				throw new DatabaseException(attribute.getAttrName() + " is alredy exist!");
						    			}
						    		}
						    		
						    		//ha az attr primaryKey
						    		if(attribute.isPrimary() && currentNode.getNodeName() == 
						    				Config.PRIMARY_KEY_TAG){
						    			Element pkAttribute = doc.createElement(Config.PRIMARY_ATTR_TAG);
						    			pkAttribute.setTextContent(attribute.getAttrName());
						    			currentNode.appendChild(pkAttribute);
						    		}
						    		//ha az attr uniqueKey
						    		if(attribute.isUnique() && currentNode.getNodeName() == 
						    				Config.UNIQUE_KEY_TAG){
						    			Element uqAttribute = doc.createElement(Config.UNIQUE_ATTR_TAG);
						    			uqAttribute.setTextContent(attribute.getAttrName());
						    			currentNode.appendChild(uqAttribute);
						    		}
						    	}
						    }
					    }	
					}
			    }
			}
			appendToXML(doc);
		} catch (SAXException | IOException | XPathExpressionException e1) {
			e1.printStackTrace();
			throw new DatabaseException("Can't create attribute, try again later!");
		}
	}
	
	public boolean searchAttribute(Node structure, String attributeName) {
		
		NodeList nlTable = structure.getChildNodes();
		for (int j = 0; j < nlTable.getLength(); j++){
		    Node currentTable = nlTable.item(j);
		    
		    //az indentalast is node-nak veszi ezert ellenorzom h attr - e
		    if(currentTable.getNodeName() == (Config.ATTRIBUTE_TAG)){
		    	String keyAttribute = currentTable.getAttributes()
			    		.getNamedItem(Config.ATTRIBUTE_ID).getNodeValue();
			    //a megadott nevu tabla torlese
			    if(keyAttribute.equals(attributeName)){
			    	return true;
			    }
		    }	
		}
		
		return false;
	}
	
	public void addIndexFile(IndexFile indexFile){
		try {

			//megnyitom az XML-t
			Document doc = docBuilder.parse(Config.XML_NAME);

			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr;
			NodeList nlDatabase;
			
			//tabla nevet keresem
			expr = xpath.compile(Config.DATABASE_SEARCH);
			nlDatabase = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			
			//keres az adatbazisok kozott
			for (int i = 0; i < nlDatabase.getLength(); i++){
			    Node currentDatabase = nlDatabase.item(i);
			    String keyDatabase = currentDatabase.getAttributes()
			    		.getNamedItem(Config.DATABASE_ID).getNodeValue();
			    
			    //megnezem az adatbazis nevet
			    if(keyDatabase.equals(indexFile.getDatabaseName())){
			    	NodeList nlTable = currentDatabase.getChildNodes();
					for (int j = 0; j < nlTable.getLength(); j++){
					    Node currentTable = nlTable.item(j);
					    
					    //az indentalast is node-nak veszi ezert ellenorzom h tabla - e
					    if(currentTable.getNodeName() == Config.TABLE_TAG){
					    	String keyTable = currentTable.getAttributes()
						    		.getNamedItem(Config.TABLE_ID).getNodeValue();
					  
					    	//megnezem a tabla nevet
						    if(keyTable.equals(indexFile.getTableName())){
						    	NodeList nl = currentTable.getChildNodes();
						    	
						    	//keresem az indexFiles taget
						    	for(int k = 0; k < nl.getLength(); k++){
						    		Node currentNode = nl.item(k);
						    	
						    		if( currentNode.getNodeName() == 
						    							Config.INDEX_FILES_TAG){
						    			Element tagIndex = doc.createElement(Config.INDEX_FILE_TAG);
						    			tagIndex.setAttribute(Config.INDEX_FILE_ID, indexFile.getIndexName());
						    			tagIndex.setAttribute(Config.INDEX_FILE_LEGTH, indexFile.getKeyLength());
						    			tagIndex.setAttribute(Config.INDEX_FILE_IS_UNIQUE, indexFile.getUnique());
						    			tagIndex.setAttribute(Config.INDEX_FILE_TYPE, indexFile.getType());
						    			currentNode.appendChild(tagIndex);
						    			appendToXML(doc);
						    			return;
						    		}
						    	}
						    }
					    }	
					}
			    }
			}
			appendToXML(doc);
		} catch (SAXException | IOException | XPathExpressionException e1) {
			e1.printStackTrace();
			throw new DatabaseException("Can't create attribute, try again later!");
		}
	}
	
}
