package edu.cs.dbms.frontend.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.cs.dbms.backend.model.Attribute;
import edu.cs.dbms.backend.model.IndexFile;
import edu.cs.dbms.backend.service.DatabaseException;
import edu.cs.dbms.backend.service.IndexFileService;
import edu.cs.dbms.frontend.gui.IndexAttributePanel;
import edu.cs.dbms.frontend.gui.TreePopup;
import edu.cs.dbms.frontend.gui.TreeViewPanel;
import edu.cs.dbms.frontend.util.ConfigFront;

public class IndexAttributeListener implements ActionListener{

	private IndexAttributePanel attributePanel;
	private IndexFileService indexService;
	private TreePopup treePopup;
	private TreeViewPanel treeViewPanel;
	
	public IndexAttributeListener(TreeViewPanel treeViewPanel, 
			IndexAttributePanel attributePanel,TreePopup treePopup) {
		
		this.treeViewPanel = treeViewPanel;
		this.treePopup = treePopup;
		this.attributePanel = attributePanel;
		indexService = new IndexFileService();
		attributePanel.setComboBox(indexService.getIndexFile(treePopup.getIndexFile()));	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(ConfigFront.ADD_TO_INDEX_FILE)){
			
			try{				
				IndexFile file = treePopup.getIndexFile(); 
				file.setIndexName(attributePanel.getIndexFileName());
				Attribute attribute = treePopup.getAttribute();
				indexService.addToIndexFile(attribute.getAttrName(), file);
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePopup.getNode().getParent();
				treeViewPanel.loadTableContent(node);				
			}catch(DatabaseException exp){
				JOptionPane.showMessageDialog(attributePanel, exp);
			}finally{
				attributePanel.setVisible(false);
			}
		}
		
	}
}
