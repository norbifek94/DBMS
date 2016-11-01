package edu.cs.dbms.frontend.ui;

import javax.swing.JFrame;

public class GUI extends JFrame{
	
	public GUI(){
		this.setTitle("DBMS");
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
}
