package edu.cs.dbms.backend;

public class Config {

	//XML file
	public static final String XML_NAME = "D://database.xml";
	public static final String XML_PARENT_TAG = "Databases";
	
	//Database
	public static final String DATABASE_SEARCH = "//Databases/Database[@databaseName]";
	public static final String DATABASE_TAG = "Database";
	public static final String DATABASE_ID = "databaseName";
	
	//Table
	public static final String TABLE_SEARCH = "//Databases/Database/Table[@tableName]";
	public static final String TABLE_TAG = "Table";
	public static final String TABLE_ID = "tableName";
	
	//IndexFile
	public static final String INDEX_FILES_TAG = "IndexFiles";
	public static final String INDEX_FILE_TAG = "IndexFile";
	public static final String INDEX_ATTRIBUTES_TAG = "IndexAttributes";
	public static final String INDEX_ATTRIBUTE_TAG = "IAttribute";
	public static final String INDEX_FILE_ID = "indexName";
	public static final String INDEX_FILE_LEGTH = "keyLeght";
	public static final String INDEX_FILE_IS_UNIQUE = "isUnique";
	public static final String INDEX_FILE_TYPE = "indexType";
	
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
	
	//UniqueKEy
	public static final String UNIQUE_KEY_TAG = "uniqueKey";
	public static final String UNIQUE_ATTR_TAG = "UniqueAttribute";	
}
