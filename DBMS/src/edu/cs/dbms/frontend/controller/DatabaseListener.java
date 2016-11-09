package edu.cs.dbms.frontend.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import edu.cs.dbms.backend.service.DatabaseException;
import edu.cs.dbms.backend.service.DatabaseService;
import edu.cs.dbms.frontend.gui.DatabasePanel;
import edu.cs.dbms.frontend.gui.TreeViewPanel;
import edu.cs.dbms.frontend.util.ConfigFront;

public class DatabaseListener implements ActionListener{
	
	private TreeViewPanel treeViewPanel;
	private DatabasePanel databasePanel;
	private DatabaseService db;
	
	public DatabaseListener(TreeViewPanel treeViewPanel, DatabasePanel databasePanel) {
		this.treeViewPanel = treeViewPanel;
		this.databasePanel = databasePanel;
		this.db = new DatabaseService();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(ConfigFront.DATABASE_SAVE_BUTTON) &&
				!databasePanel.getDatabaseName().isEmpty()){
			try{
				db.createDatabase(databasePanel.getDatabaseName());
				treeViewPanel.loadDatabase(db.getDatabase());
			}catch(DatabaseException exp){
				JOptionPane.showMessageDialog(databasePanel, exp);
			}finally{
				databasePanel.setVisible(false);
			}
		}
	}

}
