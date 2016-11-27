package edu.cs.dbms.frontend.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.cs.dbms.backend.key_value.KeyValueStoring;
import edu.cs.dbms.backend.model.Attribute;
import edu.cs.dbms.backend.service.DatabaseException;
import edu.cs.dbms.backend.service.ForeignKeyService;
import edu.cs.dbms.backend.service.TableService;
import edu.cs.dbms.backend.util.Config;
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
			//letrehozom filet a megfelelo path-el es feltoltom a 
			//key-kel amelyek a referencia tablaba primary key-ek
			try{				
				KeyValueStoring kvs = new KeyValueStoring();
				kvs.setPath(Config.KEY_VALUE_PATH_PK + 
						attribute.getDatabaseName(), attribute.getTableName());
				Map<String, List<String>> map = kvs.getAll();
				
				for(Map.Entry<String, List<String>> entry : map.entrySet()) {
					kvs.setPath(Config.KEY_VALUE_PATH_FK + 
							attribute.getDatabaseName() + "_" + 
							foreignKeyPanel.getTableName(), attribute.getTableName());
					kvs.insert(entry.getKey(), "");
				}
				
				foreignKeyService.addNewForeignKey(attribute, foreignKeyPanel.getTableName());
				treeViewPanel.loadTableContent(brotherNode);
			}catch(DatabaseException exp){
				JOptionPane.showMessageDialog(foreignKeyPanel, exp);
			}finally{
				foreignKeyPanel.setVisible(false);
			}
		}
//		}else if(e.getActionCommand().equals(ConfigFront.ADD_TO_FOREIGN_KEY)){
//			
//			try{				
//				foreignKeyService.addToForeignKey(attribute, foreignKeyPanel.getTableName());
//				treeViewPanel.loadTableContent(brotherNode);
//			}catch(DatabaseException exp){
//				JOptionPane.showMessageDialog(foreignKeyPanel, exp);
//			}finally{
//				foreignKeyPanel.setVisible(false);
//			}
//		}
		
	}

}
