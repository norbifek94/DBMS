package edu.cs.dbms.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import edu.cs.dbms.backend.model.Attribute;
import edu.cs.dbms.backend.model.Table;
import edu.cs.dbms.backend.util.Config;

public class ForeignKeyService extends XMLFileService{

	public void addNewForeignKey(Attribute attribute, String tableName){
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			
			if(e.getAttributeValue(Config.DATABASE_ID).equals(attribute.getDatabaseName())){
				
				List<Element> listTable = e.getChildren();
				for(Element t : listTable){
					
					if(t.getAttributeValue(Config.TABLE_ID).equals(tableName)){
						Element foreignKeys = t.getChild(Config.FOREIGN_KEYS_TAG);
						Element foreignKey = new Element(Config.FOREIGN_KEY_TAG);
						
						if(!searchTable(foreignKeys, attribute.getTableName())){
							Element fkAttr = new Element(Config.FOREIGN_ATTR_TAG);
							fkAttr.setText(attribute.getAttrName());
							Element ref = new Element(Config.FOREIGN_KEY_REFERENCES_TAG);
							Element refTable = new Element(Config.FOREIGN_KEY_REF_TABLE_TAG);
							refTable.setText(attribute.getTableName());
							Element refAttr = new Element(Config.FOREIGN_KEY_REF_ATTRIBUTE_TAG);
							refAttr.setText(attribute.getAttrName());
							ref.addContent(refTable);
							ref.addContent(refAttr);
							foreignKey.addContent(ref);
							foreignKey.addContent(fkAttr);
							foreignKeys.addContent(foreignKey);
							save(doc);
							return;
						}else{
							throw new DatabaseException("Alredy exist this foreign key!");
						}
					}
				}
			}
		}
	}
	
	public void addToForeignKey(Attribute attribute, String tableName){
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			
			if(e.getAttributeValue(Config.DATABASE_ID).equals(attribute.getDatabaseName())){
				
				List<Element> listTable = e.getChildren();
				for(Element t : listTable){
					
					if(t.getAttributeValue(Config.TABLE_ID).equals(tableName)){
						Element foreignKeys = t.getChild(Config.FOREIGN_KEYS_TAG);
						
						if(searchTable(foreignKeys, attribute.getTableName())){
							List<Element> list = foreignKeys.getChildren(Config.FOREIGN_KEY_TAG);
							
							for(Element i : list){
								Element ref = i.getChild(Config.FOREIGN_KEY_REFERENCES_TAG);
								String refTable = ref.getChildText(Config.FOREIGN_KEY_REF_TABLE_TAG);
								
								if(refTable.equals(attribute.getTableName())){
									Element refAttr = new Element(Config.FOREIGN_KEY_REF_ATTRIBUTE_TAG);
									refAttr.setText(attribute.getAttrName());
									ref.addContent(refAttr);
									Element fkAttr = new Element(Config.FOREIGN_ATTR_TAG);
									fkAttr.setText(attribute.getAttrName());
									i.addContent(fkAttr);
									save(doc);
									return;
								}
							}
						}else{
							throw new DatabaseException("Not have references to this table!");
						}
					}
				}
			}
		}
	}
	
	public boolean searchAttr(Element element, String attrName){
		List<Element> list = element.getChildren(Config.FOREIGN_KEY_TAG);
		
		for(Element e : list){
			if(e.getChild(Config.FOREIGN_ATTR_TAG).getValue().equals(attrName)){
				
				return true;
			}
		}
		return false;
	}
	
	public boolean searchTable(Element element, String tableName){
		List<Element> list = element.getChildren(Config.FOREIGN_KEY_TAG);
		
		for(Element e : list){
			Element ref = e.getChild(Config.FOREIGN_KEY_REFERENCES_TAG);
			Element refTable = ref.getChild(Config.FOREIGN_KEY_REF_TABLE_TAG);
			if(refTable.getValue().equals(tableName)){
				return true;
			}
		}
		return false;
	}
	
	public List<String> getForeigTable(Attribute attribute){
		
		List<String> list = new ArrayList<String>();
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			
			if(e.getAttributeValue(Config.DATABASE_ID).equals(attribute.getDatabaseName())){
				
				List<Element> listTable = e.getChildren();
				for(Element t : listTable){
					if(t.getAttributeValue(Config.TABLE_ID).equals(attribute.getTableName())){
						Element foreignKeys = t.getChild(Config.FOREIGN_KEYS_TAG);
						List<Element> keys = foreignKeys.getChildren();
						for(Element k : keys){
							Element ref = k.getChild(Config.FOREIGN_KEY_REFERENCES_TAG);
							list.add(ref.getChildText(Config.FOREIGN_KEY_REF_TABLE_TAG));
						}
					}
				}
			}
		}
		return list;
	}
	
	public List<String> getForeigAttr(Attribute attribute, String tableName){
		
		List<String> list = new ArrayList<String>();
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			
			if(e.getAttributeValue(Config.DATABASE_ID).equals(attribute.getDatabaseName())){
				
				List<Element> listTable = e.getChildren();
				for(Element t : listTable){
					if(t.getAttributeValue(Config.TABLE_ID).equals(attribute.getTableName())){
						Element foreignKeys = t.getChild(Config.FOREIGN_KEYS_TAG);
						List<Element> keys = foreignKeys.getChildren();
						for(Element k : keys){
							Element ref = k.getChild(Config.FOREIGN_KEY_REFERENCES_TAG);
							List<Element> fkAttr = ref.getChildren(Config.FOREIGN_KEY_REF_ATTRIBUTE_TAG);
							Element fkTable = ref.getChild(Config.FOREIGN_KEY_REF_TABLE_TAG);
							if(fkTable.getValue().equals(tableName)){
								for(Element a : fkAttr){
									list.add(a.getValue());
								}
							}
						}
					}
				}
			}
		}
		return list;
	}
	
	public void deleteKey(Attribute attribute, String foreignTableName){
	
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			
			if(e.getAttributeValue(Config.DATABASE_ID).equals(attribute.getDatabaseName())){
				
				List<Element> listTable = e.getChildren();
				for(Element t : listTable){
					if(t.getAttributeValue(Config.TABLE_ID).equals(attribute.getTableName())){
						Element foreignKeys = t.getChild(Config.FOREIGN_KEYS_TAG);
						List<Element> keys = foreignKeys.getChildren();
						for(Element k : keys){
							Element ref = k.getChild(Config.FOREIGN_KEY_REFERENCES_TAG);
							if(ref.getChildText(Config.FOREIGN_KEY_REF_TABLE_TAG).equals(foreignTableName)){
								keys.remove(k);
								save(doc);
								return;
							}
						}
					}
				}
			}
		}
	}
	
	public void deleteAllForignKeyAttr(Attribute attribute){
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			List<Element> listTable = e.getChildren();
			for(Element t : listTable){
				Element fk = t.getChild(Config.FOREIGN_KEYS_TAG);
				List<Element> fkList = fk.getChildren();
				for(Element f : fkList){
					Element ref = f.getChild(Config.FOREIGN_KEY_REFERENCES_TAG);
					Element refTable = ref.getChild(Config.FOREIGN_KEY_REF_TABLE_TAG);
					if(refTable.getText().equals(attribute.getTableName())){
						List<Element> refAttList = ref.getChildren(Config.FOREIGN_KEY_REF_ATTRIBUTE_TAG);
						if(refAttList.size() > 1){
							for(Element refAtt : refAttList){
								if(refAtt.getText().equals(attribute.getAttrName())){
									refAttList.remove(refAtt);
									deleteFkAttr(f, attribute);
								}
							}
						}else{
							fkList.remove(f);
						}
					}
				}
			}
		}
		save(doc);
	}
	
	public void deleteAllForeignKeyTable(Table table){
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			List<Element> listTable = e.getChildren();
			for(Element t : listTable){
				Element fk = t.getChild(Config.FOREIGN_KEYS_TAG);
				List<Element> fkList = fk.getChildren();
				for(Element f : fkList){
					Element ref = f.getChild(Config.FOREIGN_KEY_REFERENCES_TAG);
					Element refTable = ref.getChild(Config.FOREIGN_KEY_REF_TABLE_TAG);
					if(refTable.getText().equals(table.getTableName())){
						fkList.remove(f);
					}
				}
			}
		}
		save(doc);
	}
	
	private void deleteFkAttr(Element element, Attribute attribute){
		List<Element> fkAttr = element.getChildren(Config.FOREIGN_ATTR_TAG);
		for(Element f : fkAttr){
			if(f.getText().equals(attribute.getAttrName())){
				fkAttr.remove(f);
			}
		}
	}
}
