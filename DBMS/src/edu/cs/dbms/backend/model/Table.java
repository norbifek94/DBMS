package edu.cs.dbms.backend.model;


public class Table {
	private String databaseName;
	private String tableName;
	private String fileName;
	
	public Table(String databaseName, String tableName, String fileName){
		
		this.databaseName = databaseName;
		this.tableName = tableName;
		this.fileName = fileName;
	}
	
	
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}


	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getDatabaseName() {
		return databaseName;
	}


	public String getTableName() {
		return tableName;
	}


	public String getFileName() {
		return fileName;
	}


	public static class TableBuilder{
		private String databaseName;
		private String tableName;
		private String fileName;
		
		public TableBuilder(){}
				
		public TableBuilder setDatabaseName(String databaseName) {
			this.databaseName = databaseName;
			return this;
		}

		public TableBuilder setTableName(String tableName) {
			this.tableName = tableName;
			return this;
		}

		public TableBuilder setFileName(String fileName) {
			this.fileName = fileName;
			return this;
		}

		public Table creatTable(){
			return new Table(databaseName, tableName, fileName);
		}
	}
}
