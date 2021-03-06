package edu.cs.dbms.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import edu.cs.dbms.backend.model.Attribute;
import edu.cs.dbms.backend.model.IndexFile;
import edu.cs.dbms.backend.util.Config;

public class AttributeService extends XMLFileService{

	//attributum letehozasa
	public void createAttribute(Attribute attribute) throws DatabaseException{
		
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			
			if(e.getAttributeValue(Config.DATABASE_ID).equals(attribute.getDatabaseName())){
				
				List<Element> listTable = e.getChildren();
				for(Element t : listTable){
					
					if(t.getAttributeValue(Config.TABLE_ID).equals(attribute.getTableName())){
						Element struc = t.getChild(Config.STRUCTURE_TAG);
						if(!searchAttribute(struc, attribute)){
							
							Element attr = new Element(Config.ATTRIBUTE_TAG);
							attr.setAttribute(new org.jdom2.Attribute(
									Config.ATTRIBUTE_ID, attribute.getAttrName()));
							attr.setAttribute(new org.jdom2.Attribute(
									Config.ATTRIBUTE_TYPE, attribute.getType()));
							attr.setAttribute(new org.jdom2.Attribute(
									Config.ATTRIBUTE_LENGTH, attribute.getLength()));
							attr.setAttribute(new org.jdom2.Attribute(
									Config.ATTRIBUTE_IS_NULL, attribute.getIsNull()));
							struc.addContent(attr);
							if(attribute.isPrimary()){
								Element pk = t.getChild(Config.PRIMARY_KEY_TAG);
								Element pkElement = new Element(Config.PRIMARY_ATTR_TAG);
								pkElement.setText(attribute.getAttrName());
								pk.addContent(pkElement);
							}
							if(attribute.isUnique()){
								Element uk = t.getChild(Config.UNIQUE_KEY_TAG);
								Element ukElement = new Element(Config.UNIQUE_ATTR_TAG);
								ukElement.setText(attribute.getAttrName());
								uk.addContent(ukElement);
							}
							save(doc);	
							return;
						}else{
							throw new DatabaseException("Table alredy exist!");
						}
					}
				}
			}
		}
			
	}
	
	public boolean searchAttribute(Element struc, Attribute attribute) {
		List<Element> attr = struc.getChildren();
		for(Element a : attr){
			if(a.getAttributeValue(Config.ATTRIBUTE_ID).equals(attribute.getAttrName())){
				return true;
			}
		}
		return false;
	}
	
	public List<String> getAttributeNames(Attribute attribute){
		List<String> attributeList = new ArrayList<String>();
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			
			if(e.getAttributeValue(Config.DATABASE_ID).equals(attribute.getDatabaseName())){
				
				List<Element> listTable = e.getChildren();
				for(Element t : listTable){
					
					if(t.getAttributeValue(Config.TABLE_ID).equals(attribute.getTableName())){
						Element struc = t.getChild(Config.STRUCTURE_TAG);
						List<Element> attr = struc.getChildren();
						for(Element a : attr){
							attributeList.add(a.getAttributeValue(Config.ATTRIBUTE_ID));
						}
					}
				}
			}
		}
		return attributeList;
	}
	
	public void deleteAttribute(Attribute attribute){
		
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			
			if(e.getAttributeValue(Config.DATABASE_ID).equals(attribute.getDatabaseName())){
				
				List<Element> listTable = e.getChildren();
				for(Element t : listTable){
					
					if(t.getAttributeValue(Config.TABLE_ID).equals(attribute.getTableName())){
						Element struc = t.getChild(Config.STRUCTURE_TAG);
						List<Element> attr = struc.getChildren();
						for(Element a : attr){
							if(a.getAttributeValue(Config.ATTRIBUTE_ID).equals(attribute.getAttrName())){
								attr.remove(a);
								break;
							}
						}
//						Element pk = t.getChild(Config.PRIMARY_KEY_TAG);
						Element uk = t.getChild(Config.UNIQUE_KEY_TAG);
//						List<Element> pkList = pk.getChildren();
						List<Element> ukList = uk.getChildren();
						
							
					
//						for(Element p : pkList){
//							if(p.getValue().equals(attribute.getAttrName())){
//								pkList.remove(p);
//								isPK = true;
//								break;
//							}
//						}
						for(Element u : ukList){
							if(u.getValue().equals(attribute.getAttrName())){
								ukList.remove(u);
								break;
							}
						}
					}
				}
			}
		}
		
		save(doc);
		IndexFile indexFile = new IndexFile.IndexFileBuilder()
				.setDatabaseName(attribute.getDatabaseName())
				.setTableName(attribute.getTableName())
				.setIndexName(attribute.getAttrName() +
						Config.INDEX_FILE_EXTENSION)
				.createIndexFile();
		IndexFileService fileService = new IndexFileService();
		fileService.deleteIndex(indexFile);
//		if(isPK){
//			ForeignKeyService foreignKeyService = new ForeignKeyService();
//			foreignKeyService.deleteAllForignKeyAttr(attribute);
//		}
	}

	public boolean isPrimaryKey(Attribute attribute){
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			
			if(e.getAttributeValue(Config.DATABASE_ID).equals(attribute.getDatabaseName())){
				
				List<Element> listTable = e.getChildren();
				for(Element t : listTable){
					
					if(t.getAttributeValue(Config.TABLE_ID).equals(attribute.getTableName())){
						
						Element pk = t.getChild(Config.PRIMARY_KEY_TAG);	
						List<Element> pkList = pk.getChildren();
						
						for(Element p : pkList){
							if(p.getValue().equals(attribute.getAttrName())){
								return true;
							}
						}
						
					}
				}
			}
		}
		return false;
	}
	
	public boolean isForeignKey(Attribute attribute){
		if(!isPrimaryKey(attribute)){
			return false;
		}
		
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			if(e.getAttributeValue(Config.DATABASE_ID).equals(attribute.getDatabaseName())){
				List<Element> listTable = e.getChildren();
				for(Element t : listTable){
					Element fk = t.getChild(Config.FOREIGN_KEYS_TAG);
					List<Element> fkList = fk.getChildren();
					for(Element f : fkList){
						Element ref = f.getChild(Config.FOREIGN_KEY_REFERENCES_TAG);
						Element refTable = ref.getChild(Config.FOREIGN_KEY_REF_TABLE_TAG);
						if(refTable.getText().equals(attribute.getTableName())){
							List<Element> refAttList = ref.getChildren(Config.FOREIGN_KEY_REF_ATTRIBUTE_TAG);
							for(Element refAtt : refAttList){
								if(refAtt.getText().equals(attribute.getAttrName())){
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public String getForeignKeyLocation(Attribute attribute){
		if(!isPrimaryKey(attribute)){
			return "";
		}
		
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			if(e.getAttributeValue(Config.DATABASE_ID).equals(attribute.getDatabaseName())){
				List<Element> listTable = e.getChildren();
				for(Element t : listTable){
					Element fk = t.getChild(Config.FOREIGN_KEYS_TAG);
					List<Element> fkList = fk.getChildren();
					for(Element f : fkList){
						Element ref = f.getChild(Config.FOREIGN_KEY_REFERENCES_TAG);
						Element refTable = ref.getChild(Config.FOREIGN_KEY_REF_TABLE_TAG);
						if(refTable.getText().equals(attribute.getTableName())){
							List<Element> refAttList = ref.getChildren(Config.FOREIGN_KEY_REF_ATTRIBUTE_TAG);
							for(Element refAtt : refAttList){
								if(refAtt.getText().equals(attribute.getAttrName())){
									return t.getAttributeValue(Config.TABLE_ID);
								}
							}
						}
					}
				}
			}
		}
		return "";
	}
	
	public boolean isUnique(Attribute attribute){
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			
			if(e.getAttributeValue(Config.DATABASE_ID).equals(attribute.getDatabaseName())){
				
				List<Element> listTable = e.getChildren();
				for(Element t : listTable){
					
					if(t.getAttributeValue(Config.TABLE_ID).equals(attribute.getTableName())){
						
						Element uq = t.getChild(Config.UNIQUE_KEY_TAG);	
						List<Element> uqList = uq.getChildren();
						
						for(Element u : uqList){
							if(u.getValue().equals(attribute.getAttrName())){
								return true;
							}
						}
						
					}
				}
			}
		}
		return false;
	}
	
	public List<Attribute> getAttribute(Attribute attribute){
		List<Attribute> attributeList = new ArrayList<Attribute>();
		Document doc = getDocument();
		Element root = doc.getRootElement();
		List<Element> listDatabase = root.getChildren();
		
		for(Element e : listDatabase){
			
			if(e.getAttributeValue(Config.DATABASE_ID).equals(attribute.getDatabaseName())){
				
				List<Element> listTable = e.getChildren();
				for(Element t : listTable){
					
					if(t.getAttributeValue(Config.TABLE_ID).equals(attribute.getTableName())){
						Element struc = t.getChild(Config.STRUCTURE_TAG);
						List<Element> attr = struc.getChildren();
						for(Element a : attr){
							attribute.setAttrName(a.getAttributeValue(Config.ATTRIBUTE_ID));
							Attribute newAttr = new Attribute.AttributeBuilder()
															 .setAttrName(a.getAttributeValue(Config.ATTRIBUTE_ID))
															 .setType(a.getAttributeValue(Config.ATTRIBUTE_TYPE))
															 .setLength(a.getAttributeValue(Config.ATTRIBUTE_LENGTH))
															 .setIsNull(a.getAttributeValue(Config.ATTRIBUTE_IS_NULL))
															 .setDatabaseName(attribute.getDatabaseName())
															 .setTableName(attribute.getTableName())
															 .setPrimary(isPrimaryKey(attribute))
															 .setForeign(isForeignKey(attribute))
															 .setUnique(isUnique(attribute))
															 .creatAttr();
							attributeList.add(newAttr);
						}
					}
				}
			}
		}
		return attributeList;
	}
}
