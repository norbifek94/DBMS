package edu.cs.dbms.frontend.controller;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.cs.dbms.backend.model.Attribute;
import edu.cs.dbms.backend.model.IndexFile;
import edu.cs.dbms.backend.model.Table;
import edu.cs.dbms.backend.service.AttributeService;
import edu.cs.dbms.backend.service.DatabaseException;
import edu.cs.dbms.backend.service.DatabaseService;
import edu.cs.dbms.backend.service.ForeignKeyService;
import edu.cs.dbms.backend.service.IndexFileService;
import edu.cs.dbms.backend.service.TableService;
import edu.cs.dbms.frontend.gui.AttributePanel;
import edu.cs.dbms.frontend.gui.DatabasePanel;
import edu.cs.dbms.frontend.gui.DbmsFrame;
import edu.cs.dbms.frontend.gui.ForeignKeyPanel;
import edu.cs.dbms.frontend.gui.IndexAttributePanel;
import edu.cs.dbms.frontend.gui.IndexFilePanel;
import edu.cs.dbms.frontend.gui.TablePanel;
import edu.cs.dbms.frontend.gui.TreePopup;
import edu.cs.dbms.frontend.gui.TreeViewPanel;
import edu.cs.dbms.frontend.util.ConfigFront;

public class PopupMenuListener implements MouseListener{

	private JMenuItem menuItem;
	private DbmsFrame dbmsFrame;
	private TreeViewPanel treeViewPanel;
	private TreePopup treePopup;
	private DatabaseService databaseService;
	private TableService tableService;
	
	
	public PopupMenuListener(DbmsFrame dbmsFrame, TreeViewPanel treeViewPanel,
							TreePopup treePopup, JMenuItem menuItem) {
		this.dbmsFrame = dbmsFrame;
		this.menuItem = menuItem;
		this.treeViewPanel = treeViewPanel;
		this.treePopup = treePopup;
		this.databaseService = new DatabaseService();
		this.tableService = new TableService();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		DefaultMutableTreeNode node;
		Attribute attr;
		IndexFile indexFile;
		JPanel panel = null;
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
						Table table = treePopup.getTable();
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
				case ConfigFront.DELETE_ATTRIBUTE:
					attr = treePopup.getAttribute();					
					Object[] array = treePopup.getNode().getPath();
					attributeService.deleteAttribute(attr);
					if(attributeService.isPk()){
						treeViewPanel.loadTable(tableService.getTable(attr.getDatabaseName()),
								(DefaultMutableTreeNode) array[1]);
					}else{
						treeViewPanel.loadTableContent((DefaultMutableTreeNode)treePopup.getNode().getParent());
					}
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
					indexFile = treePopup.getIndexFile();
					node = (DefaultMutableTreeNode) treePopup.getNode().getParent();
					indexFileService.deleteIndex(indexFile);
					treeViewPanel.loadTableContent(node);
					break;
				case ConfigFront.NEW_FOREIGN:
					panel = new ForeignKeyPanel(treeViewPanel, treePopup, ConfigFront.NEW_FOREIGN);
					dbmsFrame.add(panel, BorderLayout.CENTER);
					dbmsFrame.pack();
					break;
				case ConfigFront.ADD_TO_FOREIGN_KEY:
					panel = new ForeignKeyPanel(treeViewPanel, treePopup, ConfigFront.ADD_TO_FOREIGN_KEY);
					dbmsFrame.add(panel, BorderLayout.CENTER);
					dbmsFrame.pack();
					break;
				case ConfigFront.DELETE_FOREIGN_KEY:
					attr = treePopup.getAttribute();
					node = (DefaultMutableTreeNode) treePopup.getNode().getParent();
					foreignKeyService.deleteKey(attr, treePopup.getForeignNode().toString());
					treeViewPanel.loadTableContent(node);
					break;
			}
		}
	}
}
