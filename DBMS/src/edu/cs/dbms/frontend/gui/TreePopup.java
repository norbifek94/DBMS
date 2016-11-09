package edu.cs.dbms.frontend.gui;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.cs.dbms.backend.model.Attribute;
import edu.cs.dbms.backend.model.IndexFile;
import edu.cs.dbms.backend.model.Table;
import edu.cs.dbms.frontend.controller.PopupMenuListener;

public class TreePopup extends JPopupMenu{

	private static final long serialVersionUID = 1L;
	
	private DbmsFrame dbmsFrame;
	private TreeViewPanel treeViewPanel;
	private String databaseName;
	private Table table;
	private DefaultMutableTreeNode node;
	private Attribute attribute;
	private IndexFile indexFile;
	private DefaultMutableTreeNode foreignNode;
	
	public TreePopup(DbmsFrame dbmsFrame, TreeViewPanel treeViewPanel){
		this.dbmsFrame = dbmsFrame;
		this.treeViewPanel = treeViewPanel;
	}
	
	public void addNewMenuItem(String name){
		JMenuItem item = new JMenuItem(name);
		this.add(item);
		this.add(new JSeparator());
		
		item.addMouseListener(new PopupMenuListener(dbmsFrame, treeViewPanel, this, item));
	}
	
	public void setDatabaseName(String databaseName){
		this.databaseName = databaseName;
	}
	
	public String getDatabaseName(){
		return databaseName;
	}
	
	public void setTable(Table table){
		this.table = table;
	}
	
	public Table getTable(){
		return table;
	}
	
	public void setNode(DefaultMutableTreeNode node){
		this.node = node;
	}
	
	public DefaultMutableTreeNode getNode(){
		return node;
	}
	
	public Attribute getAttribute(){
		return attribute;
	}
	
	public void setAttribute(Attribute attribute){
		this.attribute = attribute;
	}

	public IndexFile getIndexFile() {
		return indexFile;
	}

	public void setIndexFile(IndexFile indexFile) {
		this.indexFile = indexFile;
	}

	public DefaultMutableTreeNode getForeignNode() {
		return foreignNode;
	}

	public void setForeignNode(DefaultMutableTreeNode foreignNode) {
		this.foreignNode = foreignNode;
	}
	
	
	
}
