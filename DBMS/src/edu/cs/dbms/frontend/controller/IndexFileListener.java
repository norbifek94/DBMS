package edu.cs.dbms.frontend.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.cs.dbms.backend.model.IndexFile;
import edu.cs.dbms.backend.service.DatabaseException;
import edu.cs.dbms.backend.service.IndexFileService;
import edu.cs.dbms.frontend.gui.IndexFilePanel;
import edu.cs.dbms.frontend.gui.TreePopup;
import edu.cs.dbms.frontend.gui.TreeViewPanel;
import edu.cs.dbms.frontend.util.ConfigFront;

public class IndexFileListener implements ActionListener{

	private TreeViewPanel treeViewPanel;
	private IndexFilePanel indexFilePanel;
	private IndexFileService indexService;
	private TreePopup treePopup;

	public IndexFileListener(TreeViewPanel treeViewPanel, IndexFilePanel indexFilePanel,TreePopup treePopup) {
		this.treePopup = treePopup;
		this.treeViewPanel = treeViewPanel;
		this.indexFilePanel = indexFilePanel;
		indexService = new IndexFileService();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(ConfigFront.INDEX_FILE_SAVE_BUTTON)){
			
			try{				
				IndexFile file = treePopup.getIndexFile(); 
				file.setKeyLength(indexFilePanel.getLength());
				file.setUnique(indexFilePanel.isUnique() ? "1":"0");
				file.setType(indexFilePanel.getType());
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePopup.getNode().getParent();
				indexService.creatIndexFile(file);
				treeViewPanel.loadTableContent(node);
			}catch(DatabaseException exp){
				JOptionPane.showMessageDialog(indexFilePanel, exp);
			}finally{
				indexFilePanel.setVisible(false);
			}
		}
		
	}

}
