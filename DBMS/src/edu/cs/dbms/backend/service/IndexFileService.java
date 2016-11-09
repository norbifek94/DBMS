package edu.cs.dbms.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

import edu.cs.dbms.backend.model.IndexFile;
import edu.cs.dbms.backend.util.Config;
public class IndexFileService extends XMLFileService{

	public void creatIndexFile(IndexFile indexFile){
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			
			if(e.getAttributeValue(Config.DATABASE_ID).equals(indexFile.getDatabaseName())){
				
				List<Element> listTable = e.getChildren();
				for(Element t : listTable){
					if(t.getAttributeValue(Config.TABLE_ID).equals(indexFile.getTableName())){
						Element indexFiles = t.getChild(Config.INDEX_FILES_TAG);
						if(!searchIndexFile(indexFiles, indexFile)){
							Element index = new Element(Config.INDEX_FILE_TAG);
							index.setAttribute(new Attribute(
									Config.INDEX_FILE_ID, indexFile.getIndexName()
														+ Config.INDEX_FILE_EXTENSION));
							index.setAttribute(new Attribute(
									Config.INDEX_FILE_KEY_LEGTH, indexFile.getKeyLength()));
							index.setAttribute(new Attribute(
									Config.INDEX_FILE_IS_UNIQUE, indexFile.getUnique()));
							index.setAttribute(new Attribute(
									Config.INDEX_FILE_TYPE, indexFile.getType()));
							Element indexAttr = new Element(Config.INDEX_ATTRIBUTES_TAG);
							Element attr = new Element(Config.INDEX_ATTRIBUTE_TAG);
							attr.setText(indexFile.getIndexName());
							indexAttr.addContent(attr);
							index.addContent(indexAttr);
							indexFiles.addContent(index);
							save(doc);	
							return;
						}else{
							throw new DatabaseException("Index file alredy exist!");
						}
					}
				}
			}
		}
	}
	public void addToIndexFile(String attrName, IndexFile indexFile){
		
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			
			if(e.getAttributeValue(Config.DATABASE_ID).equals(indexFile.getDatabaseName())){
				
				List<Element> listTable = e.getChildren();
				for(Element t : listTable){
					if(t.getAttributeValue(Config.TABLE_ID).equals(indexFile.getTableName())){
						Element indexFiles = t.getChild(Config.INDEX_FILES_TAG);
						List<Element> index = indexFiles.getChildren();
						for(Element i : index){
							if(i.getAttributeValue(Config.INDEX_FILE_ID).equals(indexFile.getIndexName())){
								Element element = i.getChild(Config.INDEX_ATTRIBUTES_TAG);
								if(!searchIndexAttr(element, attrName)){
									Element newIndex = new Element(Config.INDEX_ATTRIBUTE_TAG);
									newIndex.setText(attrName);
									element.addContent(newIndex);
									save(doc);
									return;
								}else{
									throw new DatabaseException("Alredy added this attribute to index file!");
								}
							}
						}
					}
				}
			}
		}
	}
	public boolean searchIndexFile(Element indexFiles, IndexFile indexFile) {
		List<Element> index = indexFiles.getChildren();
		for(Element i : index){
			if(i.getAttributeValue(Config.INDEX_FILE_ID).equals(indexFile.getIndexName()
															+ Config.INDEX_FILE_EXTENSION)){
				return true;
			}
		}
		return false;
	}
	
	public boolean searchIndexAttr(Element indexFiles, String attrName) {
		List<Element> index = indexFiles.getChildren();
		for(Element i : index){
			if(i.getValue().equals(attrName)){
				return true;
			}
		}
		return false;
	}
	
	public List<String> getIndexFile(IndexFile indexFile) {
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		List<String> files = new ArrayList<String>();
		
		for(Element e : listDatabase){
			
			if(e.getAttributeValue(Config.DATABASE_ID).equals(indexFile.getDatabaseName())){
				
				List<Element> listTable = e.getChildren();
				for(Element t : listTable){
					if(t.getAttributeValue(Config.TABLE_ID).equals(indexFile.getTableName())){
						Element indexFiles = t.getChild(Config.INDEX_FILES_TAG);
						List<Element> index = indexFiles.getChildren();
						for(Element i : index){
							files.add(i.getAttributeValue(Config.INDEX_FILE_ID));
						}
					}
				}
			}
		}
		return files;
	}
	
	public List<String> getIndexAttr(IndexFile indexFile, String indexName) {
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		List<String> attr = new ArrayList<String>();
		
		for(Element e : listDatabase){
			
			if(e.getAttributeValue(Config.DATABASE_ID).equals(indexFile.getDatabaseName())){
				
				List<Element> listTable = e.getChildren();
				for(Element t : listTable){
					if(t.getAttributeValue(Config.TABLE_ID).equals(indexFile.getTableName())){
						Element indexFiles = t.getChild(Config.INDEX_FILES_TAG);
						List<Element> index = indexFiles.getChildren();
						for(Element i : index){
							if(i.getAttributeValue(Config.INDEX_FILE_ID).equals(indexName)){
								Element attrElement = i.getChild(Config.INDEX_ATTRIBUTES_TAG);
								List<Element> attrList = attrElement.
										getChildren(Config.INDEX_ATTRIBUTE_TAG);
								for(Element a : attrList){
									attr.add(a.getValue());
								}
							}
						}
					}
				}
			}
		}
		return attr;
	}
	
	public void deleteIndex(IndexFile indexFile){
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			
			if(e.getAttributeValue(Config.DATABASE_ID).equals(indexFile.getDatabaseName())){
				
				List<Element> listTable = e.getChildren();
				for(Element t : listTable){
					if(t.getAttributeValue(Config.TABLE_ID).equals(indexFile.getTableName())){
						Element indexFiles = t.getChild(Config.INDEX_FILES_TAG);
						List<Element> index = indexFiles.getChildren();
						for(Element i : index){
							if(i.getAttributeValue(Config.INDEX_FILE_ID)
									.equals(indexFile.getIndexName())){
								index.remove(i);
								save(doc);
								return;
							}
						}
					}
				}
			}
		}
	}
}
