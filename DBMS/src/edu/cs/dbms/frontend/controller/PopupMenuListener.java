package edu.cs.dbms.frontend.controller;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.cs.dbms.backend.key_value.KeyValueStoring;
import edu.cs.dbms.backend.model.Attribute;
import edu.cs.dbms.backend.model.IndexFile;
import edu.cs.dbms.backend.model.Table;
import edu.cs.dbms.backend.service.AttributeService;
import edu.cs.dbms.backend.service.DatabaseException;
import edu.cs.dbms.backend.service.DatabaseService;
import edu.cs.dbms.backend.service.ForeignKeyService;
import edu.cs.dbms.backend.service.IndexFileService;
import edu.cs.dbms.backend.service.TableService;
import edu.cs.dbms.backend.util.Config;
import edu.cs.dbms.frontend.gui.AttributePanel;
import edu.cs.dbms.frontend.gui.DatabasePanel;
import edu.cs.dbms.frontend.gui.DbmsFrame;
import edu.cs.dbms.frontend.gui.ForeignKeyPanel;
import edu.cs.dbms.frontend.gui.IndexAttributePanel;
import edu.cs.dbms.frontend.gui.IndexFilePanel;
import edu.cs.dbms.frontend.gui.NewEntryPanel;
import edu.cs.dbms.frontend.gui.TablePanel;
import edu.cs.dbms.frontend.gui.TreePopup;
import edu.cs.dbms.frontend.gui.TreeViewPanel;
import edu.cs.dbms.frontend.util.ConfigFront;

public class PopupMenuListener extends MouseAdapter{

	private JMenuItem menuItem;
	private DbmsFrame dbmsFrame;
	private TreeViewPanel treeViewPanel;
	private TreePopup treePopup;
	private DatabaseService databaseService;
	private TableService tableService;
	private JPanel panel;
	
	public PopupMenuListener(DbmsFrame dbmsFrame, TreeViewPanel treeViewPanel,
							TreePopup treePopup, JMenuItem menuItem) {
		this.dbmsFrame = dbmsFrame;
		this.menuItem = menuItem;
		this.treeViewPanel = treeViewPanel;
		this.treePopup = treePopup;
		this.databaseService = new DatabaseService();
		this.tableService = new TableService();
		this.panel = null;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		DefaultMutableTreeNode node;
		Attribute attr;
		IndexFile indexFile;
		
		AttributeService attributeService = new AttributeService();
		IndexFileService indexFileService = new IndexFileService();
		ForeignKeyService foreignKeyService = new ForeignKeyService();
				
		if(SwingUtilities.isLeftMouseButton(e)){
			switch(menuItem.getText()){
				case ConfigFront.NEW_DATABASE:	
					panel = new DatabasePanel(treeViewPanel);
					dbmsFrame.add(panel, BorderLayout.CENTER);
					dbmsFrame.pack();
					break;
					
				case ConfigFront.DROP_DATABASE:
					try{
						//torlom rekurzivan a mappat mindenhonnan
						KeyValueStoring.deleteFile(
								new File(Config.KEY_VALUE_PATH_PK + 
										treePopup.getDatabaseName() + "/"));
						KeyValueStoring.deleteFile(
								new File(Config.KEY_VALUE_PATH_UQ + 
										treePopup.getDatabaseName() + "/"));
						TableService tbs = new TableService();
						List<String> tables = tbs.getTable(treePopup.getDatabaseName());
						for(int i = 0; i < tables.size(); i++){
							KeyValueStoring.deleteFile(
									new File(Config.KEY_VALUE_PATH_FK + 
											treePopup.getDatabaseName() + "_" + tables.get(i)));
						}
						
						databaseService.dropDatabase(treePopup.getDatabaseName());
						treeViewPanel.loadDatabase(databaseService.getDatabase());
					}catch(DatabaseException exp){
						JOptionPane.showMessageDialog(dbmsFrame, exp);
					}
					break;
					
				case ConfigFront.NEW_TABLE:
					panel = new TablePanel(treeViewPanel, treePopup);	
					dbmsFrame.add(panel, BorderLayout.CENTER);
					dbmsFrame.pack();
					break;
					
				case ConfigFront.DROP_TABLE:
					try{
						//rekurzivan torloma  mappakat
						Table table = treePopup.getTable();
						TableService tbs = new TableService();
						List<String> tables = tbs.getTable(table.getDatabaseName());
						for(int i = 0; i < tables.size(); i++){
							if(KeyValueStoring.fileExists(
									new File(Config.KEY_VALUE_PATH_FK + 
											table.getDatabaseName() + "_" 
											+ tables.get(i) + "/" + table.getTableName()))){
								JOptionPane.showMessageDialog(dbmsFrame, "Drop failde, because of dependecy!");
								return;
							}
						}
						KeyValueStoring.deleteFile(
								new File(Config.KEY_VALUE_PATH_PK + 
										table.getDatabaseName() + "/" + table.getTableName()));
						KeyValueStoring.deleteFile(
								new File(Config.KEY_VALUE_PATH_UQ + 
										table.getDatabaseName() + "/" + table.getTableName()));	
						KeyValueStoring.deleteFile(
								new File(Config.KEY_VALUE_PATH_FK + 
										table.getDatabaseName() + "_" + table.getTableName()));
					
						
						node = (DefaultMutableTreeNode) treePopup.getNode().getParent();
						tableService.dropTable(table);
						treeViewPanel.loadTable(tableService.getTable(table.getDatabaseName()),node);
					}catch (DatabaseException exp) {
						JOptionPane.showMessageDialog(dbmsFrame, exp);
					}
					break;
				case ConfigFront.NEW_ATTRIBUTE:
					panel = new AttributePanel(treeViewPanel, treePopup);
					dbmsFrame.add(panel, BorderLayout.CENTER);
					dbmsFrame.pack();
					break;
				case ConfigFront.NEW_ENTRY:
					panel = new NewEntryPanel(treeViewPanel, treePopup);
					dbmsFrame.add(panel, BorderLayout.CENTER);
					dbmsFrame.pack();
					break;
				case ConfigFront.DELETE_ATTRIBUTE:
					//torlom a mappakat rekurzivan unique es  index file kozul
					//torlom a primary-value file-bol az ertekekit
					attr = treePopup.getAttribute();					

					if(attributeService.isPrimaryKey(attr)){
						JOptionPane.showMessageDialog(dbmsFrame, "Can't delete primary key!");
						return;
					}
					
					if(attributeService.isUnique(attr)){
						KeyValueStoring.deleteFile(
								new File(Config.KEY_VALUE_PATH_UQ + 
										attr.getDatabaseName() + "/" + 
										attr.getTableName() + "/" + attr.getAttrName()));
					}
					List<Attribute> attrList = attributeService.getAttribute(attr);
					for(int i = 1; i < attrList.size(); i++){
						if(attrList.get(i).getAttrName().equals(attr.getAttrName())){
							KeyValueStoring kvs = new KeyValueStoring();
							kvs.setPath(Config.KEY_VALUE_PATH_PK + 
									attr.getDatabaseName(), attr.getTableName());
							kvs.delteAttribute(i);
						}
					}
					if(KeyValueStoring.fileExists(new File(
							Config.KEY_VALUE_PATH_IF + attr.getDatabaseName() + "/" + 
								attr.getTableName() + "/" + attr.getAttrName()))){
						KeyValueStoring.deleteFile(new File(
								Config.KEY_VALUE_PATH_IF + attr.getDatabaseName() + "/" + 
									attr.getTableName() + "/" + attr.getAttrName()));
					}
					attributeService.deleteAttribute(attr);
					treeViewPanel.loadTableContent((DefaultMutableTreeNode)treePopup.getNode().getParent());
					break;
				case ConfigFront.NEW_INDEX_FILE:
					panel = new IndexFilePanel(treeViewPanel, treePopup);
					dbmsFrame.add(panel, BorderLayout.CENTER);
					dbmsFrame.pack();
					break;
				case ConfigFront.ADD_TO_INDEX_FILE:
					panel = new IndexAttributePanel(treeViewPanel, treePopup);
					dbmsFrame.add(panel, BorderLayout.CENTER);
					dbmsFrame.pack();
					break;
				case ConfigFront.DELETE_INDEX_FILE:
					//torlom a k-v file-t is
					indexFile = treePopup.getIndexFile();
					node = (DefaultMutableTreeNode) treePopup.getNode().getParent();
					indexFileService.deleteIndex(indexFile);
					treeViewPanel.loadTableContent(node);
					if(KeyValueStoring.fileExists(
							new File(Config.KEY_VALUE_PATH_IF + 
									indexFile.getDatabaseName() + "/" + 
									indexFile.getTableName() + "/" + indexFile.getIndexName()))){
						KeyValueStoring.deleteFile(
								new File(Config.KEY_VALUE_PATH_IF + 
										indexFile.getDatabaseName() + "/" + 
										indexFile.getTableName() + "/" + indexFile.getIndexName()));
					}
					break;
				case ConfigFront.NEW_FOREIGN:
					panel = new ForeignKeyPanel(treeViewPanel, treePopup, ConfigFront.NEW_FOREIGN);
					dbmsFrame.add(panel, BorderLayout.CENTER);
					dbmsFrame.pack();
					break;
//				case ConfigFront.ADD_TO_FOREIGN_KEY:
//					panel = new ForeignKeyPanel(treeViewPanel, treePopup, ConfigFront.ADD_TO_FOREIGN_KEY);
//					dbmsFrame.add(panel, BorderLayout.CENTER);
//					dbmsFrame.pack();
//					break;
				case ConfigFront.DELETE_FOREIGN_KEY:
					//torlom a k-v file-t is
					attr = treePopup.getAttribute();
					KeyValueStoring.deleteFile(
							new File(Config.KEY_VALUE_PATH_FK + 
									attr.getDatabaseName() + "_" + attr.getTableName() + "/" + treePopup.getForeignNode().toString()));
					node = (DefaultMutableTreeNode) treePopup.getNode().getParent();
					foreignKeyService.deleteKey(attr, treePopup.getForeignNode().toString());
					treeViewPanel.loadTableContent(node);
					break;
			}
		}
	}
	
	public void setVisiblePanel(){
		if(panel != null){
			panel.setVisible(false);
		}
	}
}
