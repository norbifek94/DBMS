package edu.cs.dbms.frontend.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.cs.dbms.backend.model.Attribute;
import edu.cs.dbms.backend.service.DatabaseException;
import edu.cs.dbms.backend.service.ForeignKeyService;
import edu.cs.dbms.backend.service.TableService;
import edu.cs.dbms.frontend.gui.ForeignKeyPanel;
import edu.cs.dbms.frontend.gui.TreePopup;
import edu.cs.dbms.frontend.gui.TreeViewPanel;
import edu.cs.dbms.frontend.util.ConfigFront;

public class ForeignKeyListener implements ActionListener{

	private ForeignKeyPanel foreignKeyPanel;
	private TreePopup treePopup;
	private TreeViewPanel treeViewPanel;
	private ForeignKeyService foreignKeyService;
	
	public ForeignKeyListener(TreeViewPanel treeViewPanel, 
			ForeignKeyPanel foreignKeyPanel,TreePopup treePopup) {
	
		this.treeViewPanel = treeViewPanel;
		this.treePopup = treePopup;
		this.foreignKeyPanel = foreignKeyPanel;
		foreignKeyService = new ForeignKeyService();
		
		TableService tableService = new TableService();
		foreignKeyPanel.setComboBox(tableService.getTable(treePopup.getTable().getDatabaseName()));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		Attribute attribute = treePopup.getAttribute();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePopup.getNode().getParent();
		DefaultMutableTreeNode brotherNode = node.getNextSibling();
		
		while(brotherNode != null && !brotherNode.toString().
							equals(foreignKeyPanel.getTableName())){
			
			brotherNode = brotherNode.getNextSibling();
		}
		if(brotherNode == null){
			brotherNode = node.getPreviousSibling();
			while(brotherNode != null && !brotherNode.toString().
							equals(foreignKeyPanel.getTableName())){
			
				brotherNode = brotherNode.getPreviousSibling();
			}
		}
		if(e.getActionCommand().equals(ConfigFront.NEW_FOREIGN)){
			try{				
				
				foreignKeyService.addNewForeignKey(attribute, foreignKeyPanel.getTableName());
				treeViewPanel.loadTableContent(brotherNode);
			}catch(DatabaseException exp){
				JOptionPane.showMessageDialog(foreignKeyPanel, exp);
			}finally{
				foreignKeyPanel.setVisible(false);
			}
		}else if(e.getActionCommand().equals(ConfigFront.ADD_TO_FOREIGN_KEY)){
			
			try{				
				foreignKeyService.addToForeignKey(attribute, foreignKeyPanel.getTableName());
				treeViewPanel.loadTableContent(brotherNode);
			}catch(DatabaseException exp){
				JOptionPane.showMessageDialog(foreignKeyPanel, exp);
			}finally{
				foreignKeyPanel.setVisible(false);
			}
		}
		
	}

}
