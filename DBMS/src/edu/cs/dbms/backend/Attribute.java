package edu.cs.dbms.backend;

public class Attribute {
	private String databaseName;
	private String tableName;
	private String attrName;
	private String type;
	private String length;
	private String isNull;
	private boolean primary;
	private boolean unique;
	
	public Attribute(String databaseName, String tableName, String attrName, 
			String type, String length, String isNull,
			boolean primary, boolean unique){
		
		this.databaseName = databaseName;
		this.tableName = tableName;
		this.attrName = attrName;
		this.type = type;
		this.length = length;
		this.isNull = isNull;
		this.primary = primary;
		this.unique = unique;
	}
	
	public String getDatabaseName() {
		return databaseName;
	}

	public String getTableName() {
		return tableName;
	}

	public String getAttrName() {
		return attrName;
	}

	public String getType() {
		return type;
	}

	public String getLength() {
		return length;
	}

	public String getIsNull() {
		return isNull;
	}

	public boolean isPrimary() {
		return primary;
	}

	public boolean isUnique() {
		return unique;
	}
	
	public static class AttributeBuilder{
		private String databaseName;
		private String tableName;
		private String attrName;
		private String type;
		private String length;
		private String isNull;
		private boolean primary;
		private boolean unique;
		
		public AttributeBuilder(){}
		
		public AttributeBuilder setDatabaseName(String databaseName) {
			this.databaseName = databaseName;
			return this;
		}

		public AttributeBuilder setTableName(String tableName) {
			this.tableName = tableName;
			return this;
		}

		public AttributeBuilder setAttrName(String attrName) {
			this.attrName = attrName;
			return this;
		}

		public AttributeBuilder setType(String type) {
			this.type = type;
			return this;
		}

		public AttributeBuilder setLength(String length) {
			this.length = length;
			return this;
		}

		public AttributeBuilder setIsNull(String isNull) {
			this.isNull = isNull;
			return this;
		}

		public AttributeBuilder setPrimary(boolean primary) {
			this.primary = primary;
			return this;
		}

		public AttributeBuilder setUnique(boolean unique) {
			this.unique = unique;
			return this;
		}		
		
		public Attribute creatAttr(){
			return new Attribute(databaseName, tableName, attrName, type, length, isNull, primary, unique);
		}
	}
}
