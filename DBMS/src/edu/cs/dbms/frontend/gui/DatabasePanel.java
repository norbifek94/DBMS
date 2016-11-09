package edu.cs.dbms.frontend.gui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import edu.cs.dbms.frontend.controller.DatabaseListener;
import edu.cs.dbms.frontend.util.ConfigFront;

public class DatabasePanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private JButton saveButton;
	private JTextField databaseName;
	private JLabel label;
	
	public DatabasePanel(TreeViewPanel treeViewPanel){
		BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
		this.setLayout(layout);
		
		saveButton = new JButton(ConfigFront.DATABASE_SAVE_BUTTON);
		databaseName = new JTextField(15);
		label = new JLabel(ConfigFront.DATABASE_NAME_LABEL);
		this.setBorder(new EmptyBorder(
							ConfigFront.DATABASE_PANEL_TOP_BORDER,
							ConfigFront.DATABASE_PANEL_LEFT_BORDER,
							ConfigFront.DATABASE_PANEL_BOTTON_BORDER,
							ConfigFront.DATABASE_PANEL_RIGHT_BORDER));
		this.add(label);
		this.add(Box.createHorizontalStrut(ConfigFront.SPACE_HORIZONTAL));
		this.add(databaseName);
		this.add(Box.createHorizontalStrut(ConfigFront.SPACE_HORIZONTAL));
		this.add(saveButton);
		saveButton.addActionListener(new DatabaseListener(treeViewPanel, this));
	}

	public String getDatabaseName(){
		return databaseName.getText();
	}
	
}
