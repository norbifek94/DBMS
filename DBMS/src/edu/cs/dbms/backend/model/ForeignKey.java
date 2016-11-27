package edu.cs.dbms.backend.model;

public class ForeignKey {
	private String databaseName;
	private String tableName;
	private Attribute referens;
	
	public ForeignKey(String databaseName, String tableName, Attribute referens){
		this.databaseName = databaseName;
		this.tableName = tableName;
		this.referens = referens;
	}
	
	public String getDatabaseName() {
		return databaseName;
	}
	public String getTableName() {
		return tableName;
	}
	public Attribute getReferens() {
		return referens;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public void setReferens(Attribute referens) {
		this.referens = referens;
	}

	public static class ForeignKeyBuilder{
		private String databaseName;
		private String tableName;
		private Attribute referens;
		
		public ForeignKeyBuilder setDatabaseName(String databaseName) {
			this.databaseName = databaseName;
			return this;
		}
		public ForeignKeyBuilder setTableName(String tableName) {
			this.tableName = tableName;
			return this;
		}
		public ForeignKeyBuilder setReferens(Attribute referens) {
			this.referens = referens;
			return this;
		}
		
		public ForeignKey createForeignKey(){
			return new ForeignKey(databaseName, tableName, referens);
		}
		
	}
	
}
