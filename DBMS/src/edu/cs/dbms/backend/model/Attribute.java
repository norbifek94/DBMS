package edu.cs.dbms.backend.model;

import java.util.List;

public class Attribute {
	private String databaseName;
	private String tableName;
	private String attrName;
	private String type;
	private String length;
	private String isNull;
	private boolean primary = false;
	private boolean unique = false;
	private boolean foreignKey = false;
	private List<Attribute> foreignKeys;
	
	public Attribute(String databaseName, String tableName, String attrName, 
			String type, String length, String isNull,
			boolean primary, boolean unique, boolean foreignKey, List<Attribute> foreignKeys){
		
		this.databaseName = databaseName;
		this.tableName = tableName;
		this.attrName = attrName;
		this.type = type;
		this.length = length;
		this.isNull = isNull;
		this.primary = primary;
		this.unique = unique;
		this.foreignKey = foreignKey;
		this.setForeignKeys(foreignKeys);
	}
	
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public void setIsNull(String isNull) {
		this.isNull = isNull;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	public void setUnique(boolean unique) {
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
	
	public boolean isForignKey() {
		return foreignKey;
	}

	public void setForeignKey(boolean foreignKey) {
		this.foreignKey = foreignKey;
	}

	public List<Attribute> getForeignKeys() {
		return foreignKeys;
	}

	public void setForeignKeys(List<Attribute> foreignKeys) {
		this.foreignKeys = foreignKeys;
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
		private boolean foreignKey;
		private List<Attribute> foreignKeys;
		
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
		
		public AttributeBuilder setForeign(boolean foreignKey){
			this.foreignKey = foreignKey;
			return this;
		}
		
		public AttributeBuilder setForeignKeys(List<Attribute> foreignKeys){
			this.foreignKeys = foreignKeys;
			return this;
		}
		
		public Attribute creatAttr(){
			return new Attribute(databaseName, tableName, attrName, type, length, isNull, primary, unique, foreignKey, foreignKeys);
		}
	}
}
