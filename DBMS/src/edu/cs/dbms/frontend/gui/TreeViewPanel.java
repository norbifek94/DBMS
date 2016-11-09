package edu.cs.dbms.frontend.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import edu.cs.dbms.backend.model.Attribute;
import edu.cs.dbms.backend.model.IndexFile;
import edu.cs.dbms.backend.service.AttributeService;
import edu.cs.dbms.backend.service.DatabaseService;
import edu.cs.dbms.backend.service.ForeignKeyService;
import edu.cs.dbms.backend.service.IndexFileService;
import edu.cs.dbms.backend.service.TableService;
import edu.cs.dbms.backend.util.Config;
import edu.cs.dbms.frontend.controller.TreeViewListener;
import edu.cs.dbms.frontend.util.ConfigFront;

public class TreeViewPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JTree treeView;
	private JScrollPane treeScroll;
	private DefaultMutableTreeNode nodeDatabases;
	private DatabaseService databaseService;
	private TableService tableService;

	public TreeViewPanel(DbmsFrame dbmsFrame){
		
		nodeDatabases = new DefaultMutableTreeNode(Config.XML_PARENT_TAG);		

		treeView = new JTree(nodeDatabases);
		treeScroll = new JScrollPane(treeView);
		treeScroll.setPreferredSize(new Dimension(ConfigFront.TREE_WIDTH, ConfigFront.TREE_HEIGHT));

		this.add(treeScroll, BorderLayout.CENTER);
		treeView.addMouseListener(new TreeViewListener(this,treeView, dbmsFrame));
		
		this.databaseService = new DatabaseService();
		this.tableService = new TableService();
		loadDatabase(databaseService.getDatabase());
	}
	
	//dinamikuasn hozzaadni az adatbazisokat
	public void loadDatabase(List<String> databases){
		
		DefaultTreeModel model = (DefaultTreeModel)treeView.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
		root.removeAllChildren();
		for(String databaseName : databases){
			DefaultMutableTreeNode database = new DefaultMutableTreeNode(databaseName);
			root.add(database);
			loadTable(tableService.getTable(databaseName), database);
		}
		model.reload(root);
	}
	
	public void loadTable(List<String> table, DefaultMutableTreeNode node){
		
		DefaultTreeModel model = (DefaultTreeModel)treeView.getModel();
		node.removeAllChildren();
		DefaultMutableTreeNode tables = new DefaultMutableTreeNode(ConfigFront.NODE_TABLES);
		node.add(tables);
		for(String tableName : table){
			DefaultMutableTreeNode tableNode = new DefaultMutableTreeNode(tableName);
			tables.add(tableNode);
			loadTableContent(tableNode);
		}
		model.reload(node);
	}
	
	public void loadTableContent(DefaultMutableTreeNode node){
		
		DefaultTreeModel model = (DefaultTreeModel)treeView.getModel();
		node.removeAllChildren();
		DefaultMutableTreeNode columns = new DefaultMutableTreeNode(ConfigFront.NODE_COLUMNS);
		DefaultMutableTreeNode fk = new DefaultMutableTreeNode(ConfigFront.NODE_FOREIGN_KEY);
		DefaultMutableTreeNode index = new DefaultMutableTreeNode(ConfigFront.NODE_INDEXE);
		node.add(columns);
		node.add(index);
		node.add(fk);
		DefaultMutableTreeNode database = (DefaultMutableTreeNode) node.getParent().getParent();
		
		Attribute attribute = new Attribute.AttributeBuilder()
								 .setDatabaseName(database.toString())
								 .setTableName(node.toString())
								 .creatAttr();
		IndexFile indexFile = new IndexFile.IndexFileBuilder()
								  .setDatabaseName(database.toString())
								  .setTableName(node.toString())
								  .createIndexFile();
		AttributeService attributeService = new AttributeService();
		IndexFileService indexFileService = new IndexFileService();
		ForeignKeyService foreignKeyService = new ForeignKeyService();
		List<String> attrList = attributeService.getAttribute(attribute);		
		for(String attrName : attrList){
			
			DefaultMutableTreeNode attrNode = new DefaultMutableTreeNode(attrName);
			columns.add(attrNode);
		}
		List<String> fkTablesList = foreignKeyService.getForeigTable(attribute);
		for(String t : fkTablesList){
			DefaultMutableTreeNode fkNode = new DefaultMutableTreeNode(t);
			fk.add(fkNode);
			List<String> fkAttr = foreignKeyService.getForeigAttr(attribute, t);
			for(String a : fkAttr){
				DefaultMutableTreeNode fkAttrNode = new DefaultMutableTreeNode(a);
				fkNode.add(fkAttrNode);
			}
		}
	
		List<String> indexList = indexFileService.getIndexFile(indexFile);
		for(String indexName : indexList){
			DefaultMutableTreeNode indexNode = new DefaultMutableTreeNode(indexName);
			index.add(indexNode);
			List<String> indexAttr = indexFileService.getIndexAttr(indexFile, indexName);
			for(String i : indexAttr){
				DefaultMutableTreeNode attrNode = new DefaultMutableTreeNode(i);
				indexNode.add(attrNode);
			}
		} 
		model.reload(node);
	}	
}