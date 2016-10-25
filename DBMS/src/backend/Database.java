package backend;

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
	private String fileName;
	
	//TODO SZEPITENI A FELEPITEST ES A KODOT
	public Database(String fileName){
		
		this.fileName = fileName;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			transformer = trasformerFact.newTransformer();
		} catch (ParserConfigurationException | TransformerConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	//XML file letrehozas ha meg nem letezik es a fo Node letrehozasa (Databases)
	public void createXMLFile(){

		try {
			
			Document doc = docBuilder.newDocument();
	        Element rootEle = doc.createElement("Databases");

	        doc.appendChild(rootEle);

	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	        File file = new File(fileName);
	        //ha nem letezik letrehozom
	        if(!file.exists()){
	        	transformer.transform(new DOMSource(doc), 
                                 new StreamResult(new FileOutputStream(file, true)));
	        }
        } catch (TransformerException | IOException e) {
            System.out.println(e.getMessage());
        }
	}
	 
	//Adatbazis megkeresese es torlese
	public void dropDatabase(String databaseName){
		
		try {
			//megnyitom az XML file-t
			Document doc = docBuilder.parse(fileName);

			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr;
			NodeList nl;
			
			try {
				//mindegyik db-nek a nevet kiszedem
				expr = xpath.compile("//Databases/Database[@databaseName]");
				nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
				
				for (int i = 0; i < nl.getLength(); i++){
					
				    Node currentItem = nl.item(i);
				    String key = currentItem.getAttributes().getNamedItem("databaseName").getNodeValue();
				    //a megadott nevu db torlese
				    if(key.equals(databaseName)){
				    	currentItem.getParentNode().removeChild(currentItem);
				    	appendToXML(doc);
				    }	
				}
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SAXException | IOException e1) {
			e1.printStackTrace();
		}	
	}

	//TODO levedeni, hogy csak ugyan azzal a nevvel egy db szerepeljen
	//adatbazis letrehozasa
	public void createDatabase(String databaseName){
		try {
			//megnyitom az XML-t
			Document doc = docBuilder.parse(fileName);
			//keresett tag
			Element datatbasesTag =  (Element) doc.getElementsByTagName("Databases").item(0);
			//uj tag letrehozas
			Element databaseElement = doc.createElement("Database");
			//beallitom a nevet
			databaseElement.setAttribute("databaseName", databaseName);
			datatbasesTag.appendChild(databaseElement);
			//meglevo XML-hez hozzailesztem az uj adatokat
			appendToXML(doc);
	        
		} catch (SAXException | IOException e1) {
			e1.printStackTrace();
		}	
	}
	
	//parameter a felepitett xml
	private void appendToXML(Document doc){

		//beallitom az indentalast
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		
		//a felepittett xml a doc
		DOMSource domSource = new DOMSource(doc);
		File file = new File(fileName);
        StreamResult streamResult = new StreamResult(file);
        if(file.exists()){
        	try {
				transformer.transform(domSource, streamResult);
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
        }
	}
	
	//TODO levedeni, hogy ugyan azzan a nevvel egy tabla szerepeljen
	//tabla letrehozasa
	public void creatTable(String databaseName, String tableName){
		
		try {
			//megnyitom az XML-t
			Document doc = docBuilder.parse(fileName);

			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr;
			NodeList nl;
			
			try {
				//mindegyik db-nek a nevet keresem
				expr = xpath.compile("//Databases/Database[@databaseName]");
				nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
				
				for (int i = 0; i < nl.getLength(); i++)
				{
				    Node currentItem = nl.item(i);
				    String key = currentItem.getAttributes().getNamedItem("databaseName").getNodeValue();
				    //a megadott nevu adatbazishoz hozzaadom a tablat
				    if(key.equals(databaseName)){
				    	Element tableElement = doc.createElement("Table");
						tableElement.setAttribute("tableName", tableName);
						currentItem.appendChild(tableElement);
				    }	
				}
				appendToXML(doc);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		} catch (SAXException | IOException e1) {
			e1.printStackTrace();
		}
	}
	
	//TODO ugyan azzal a nevvel egy attr
	//attributum letehozasa
	public void creatAttribute(String tableName, String attrName, 
			String type, String length, String isNull){
		
		try {
			//megnyitom az XML-t
			Document doc = docBuilder.parse(fileName);

			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr;
			NodeList nl;
			try {
				//tabla nevet keresem
				expr = xpath.compile("//Databases/Database/Table[@tableName]");
				nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
				
				for (int i = 0; i < nl.getLength(); i++)
				{
				    Node currentItem = nl.item(i);
				    String key = currentItem.getAttributes().getNamedItem("tableName").getNodeValue();
				    //a megadott nevu tablaba uj attributum hozzaadasa
				    if(key.equals(tableName)){
				    	Element attrElement = doc.createElement("Attribute");
				    	attrElement.setAttribute("attributeName", attrName);
				    	attrElement.setAttribute("type", type);
				    	attrElement.setAttribute("length", length);
				    	attrElement.setAttribute("isNull", isNull);
						currentItem.appendChild(attrElement);
				    }	
				}
				appendToXML(doc);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		} catch (SAXException | IOException e1) {
			e1.printStackTrace();
		}
	}
	
	//TODO csak egy adott adatbazisbol torolje a tablat
	public void dropTable(String tableName){
		try {
			//megnyitom az XML-t
			Document doc = docBuilder.parse(fileName);

			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr;
			NodeList nl;
			try {
				//a megadott tablat keresem
				expr = xpath.compile("//Databases/Database/Table[@tableName]");
				nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
				
				for (int i = 0; i < nl.getLength(); i++)
				{
				    Node currentItem = nl.item(i);
				    String key = currentItem.getAttributes().getNamedItem("tableName").getNodeValue();
				    //a megadott nevu tabla torlese
				    if(key.equals(tableName)){
				    	currentItem.getParentNode().removeChild(currentItem);
				    	appendToXML(doc);
				    }	
				}
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SAXException | IOException e1) {
			e1.printStackTrace();
		}	
	}
}
