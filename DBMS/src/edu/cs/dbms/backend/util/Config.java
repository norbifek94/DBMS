package edu.cs.dbms.backend.util;

public class Config {

	//XML file
	public static final String XML_NAME = "src/edu/cs/dbms/backend/res/database.xml";
	public static final String XML_PARENT_TAG = "Databases";
	
	//Database
	public static final String DATABASE_TAG = "Database";
	public static final String DATABASE_ID = "databaseName";
	
	//Table
	public static final String TABLE_TAG = "Table";
	public static final String TABLE_ID = "tableName";
	public static final String TABLE_FILE_NAME = "fileName";
	public static final String TABLE_FILE_EXTENSION = ".bin";
	
	//IndexFile
	public static final String INDEX_FILES_TAG = "IndexFiles";
	public static final String INDEX_FILE_TAG = "IndexFile";
	public static final String INDEX_ATTRIBUTES_TAG = "IndexAttributes";
	public static final String INDEX_ATTRIBUTE_TAG = "IAttribute";
	public static final String INDEX_FILE_ID = "indexName";
	public static final String INDEX_FILE_KEY_LEGTH = "keyLeght";
	public static final String INDEX_FILE_IS_UNIQUE = "isUnique";
	public static final String INDEX_FILE_TYPE = "indexType";
	public static final String INDEX_FILE_EXTENSION = ".ind";
	
	//Attribute
	public static final String ATTRIBUTE_TAG = "Attribute";
	public static final String ATTRIBUTE_ID = "attributeName";
	public static final String ATTRIBUTE_TYPE = "type";
	public static final String ATTRIBUTE_LENGTH = "length";
	public static final String ATTRIBUTE_IS_NULL = "isNull";

	//Structure
	public static final String STRUCTURE_TAG = "Structure";
	
	//PrimaryKey
	public static final String PRIMARY_KEY_TAG = "primaryKey";
	public static final String PRIMARY_ATTR_TAG = "pkAttribute";
	
	//UniqueKey
	public static final String UNIQUE_KEY_TAG = "uniqueKey";
	public static final String UNIQUE_ATTR_TAG = "UniqueAttribute";	
	
	//ForeignKey
	public static final String FOREIGN_KEYS_TAG = "foreignKeys";
	public static final String FOREIGN_KEY_TAG = "foreignKey";
	public static final String FOREIGN_ATTR_TAG = "fkAttribute";
	public static final String FOREIGN_KEY_REFERENCES_TAG = "references";
	public static final String FOREIGN_KEY_REF_TABLE_TAG = "refTable";
	public static final String FOREIGN_KEY_REF_ATTRIBUTE_TAG = "refAttribute";
	
}
