package edu.cs.dbms.frontend.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.cs.dbms.backend.model.Table;
import edu.cs.dbms.backend.service.DatabaseException;
import edu.cs.dbms.backend.service.TableService;
import edu.cs.dbms.frontend.gui.TablePanel;
import edu.cs.dbms.frontend.gui.TreePopup;
import edu.cs.dbms.frontend.gui.TreeViewPanel;
import edu.cs.dbms.frontend.util.ConfigFront;

public class TableListener implements ActionListener{
	
	private TreeViewPanel treeViewPanel;
	private TablePanel tablePanel;
	private TableService tableService;
	private TreePopup treePopup;
	
	public TableListener(TreeViewPanel treeViewPanel, TablePanel tablePanel, TreePopup treePopup){
		
		this.treeViewPanel = treeViewPanel;
		this.tablePanel = tablePanel;
		this.tableService = new TableService();
		this.treePopup = treePopup;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals(ConfigFront.TABLE_SAVE_BUTTON) &&
				!tablePanel.getTableName().isEmpty()){
			try{
				Table table = treePopup.getTable();
				table.setTableName(tablePanel.getTableName());
				table.setFileName(tablePanel.getTableName());
				tableService.createTable(table);
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePopup.getNode();
				treeViewPanel.loadTable(tableService.getTable(table.getDatabaseName()), node);
			}catch(DatabaseException exp){
				JOptionPane.showMessageDialog(tablePanel, exp);
			}finally{
				tablePanel.setVisible(false);
			}
		}
	}

}
