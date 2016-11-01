package edu.cs.dbms.backend;

public class IndexFile {
	
	private String indexName;
	private String keyLength;
	private String unique;
	private String type;
	private String databaseName;
	private String tableName;
	
	public IndexFile(String indexName, String keyLength,
			String unique, String type, String databaseName, String tableName){
		this.indexName = indexName;
		this.keyLength = keyLength;
		this.unique = unique;
		this.type = type;
		this.databaseName = databaseName;
		this.tableName = tableName;
	}
	

	public String getIndexName() {
		return indexName;
	}


	public String getKeyLength() {
		return keyLength;
	}


	public String getUnique() {
		return unique;
	}


	public String getType() {
		return type;
	}


	public String getDatabaseName() {
		return databaseName;
	}


	public String getTableName() {
		return tableName;
	}


	public static class IndexFileBuilder{
		private String indexName;
		private String keyLength;
		private String unique;
		private String type;
		private String databaseName;
		private String tableName;
		
		public IndexFileBuilder setIndexName(String indexName) {
			this.indexName = indexName;
			return this;
		}
		
		public IndexFileBuilder setKeyLength(String keyLength) {
			this.keyLength = keyLength;
			return this;
		}
		
		public IndexFileBuilder setUnique(String unique) {
			this.unique = unique;
			return this;
		}
		
		public IndexFileBuilder setType(String type) {
			this.type = type;
			return this;
		}
		
		public IndexFileBuilder setDatabaseName(String databaseName) {
			this.databaseName = databaseName;
			return this;
		}

		public IndexFileBuilder setTableName(String tableName) {
			this.tableName = tableName;
			return this;
		}

		public IndexFile createIndexFile(){
			return new IndexFile(indexName, keyLength, unique, type, databaseName, tableName);
		}
		
	}
}
