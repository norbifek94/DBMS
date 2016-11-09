package edu.cs.dbms.frontend.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import edu.cs.dbms.frontend.util.ConfigFront;

public class DbmsFrame extends JFrame{ 

	private static final long serialVersionUID = 1L;
	private TreeViewPanel treeViewPanel;
	
	public DbmsFrame(){
		
		treeViewPanel = new TreeViewPanel(this);
		
		this.setTitle("DBMS");
		this.setPreferredSize(new Dimension(ConfigFront.FRAME_WIDTH,ConfigFront.FRAME_HEIGHT));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.add(treeViewPanel, BorderLayout.WEST);
	}
	
	
	
}
