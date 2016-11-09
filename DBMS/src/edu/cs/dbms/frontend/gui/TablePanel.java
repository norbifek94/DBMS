package edu.cs.dbms.frontend.gui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import edu.cs.dbms.frontend.controller.TableListener;
import edu.cs.dbms.frontend.util.ConfigFront;

public class TablePanel extends JPanel{

private static final long serialVersionUID = 1L;
	
	private JButton saveButton;
	private JTextField tableName;
	private JLabel label;
	
	public TablePanel(TreeViewPanel treeViewPanel, TreePopup treePopup){
		
		BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
		this.setLayout(layout);
		
		saveButton = new JButton(ConfigFront.TABLE_SAVE_BUTTON);
		tableName = new JTextField();
		label = new JLabel(ConfigFront.TABLE_NAME_LABEL);
		this.setBorder(new EmptyBorder(
							ConfigFront.TABLE_PANEL_TOP_BORDER,
							ConfigFront.TABLE_PANEL_LEFT_BORDER,
							ConfigFront.TABLE_PANEL_BOTTON_BORDER,
							ConfigFront.TABLE_PANEL_RIGHT_BORDER));
		this.add(label);
		this.add(Box.createHorizontalStrut(ConfigFront.SPACE_HORIZONTAL));
		this.add(tableName);
		this.add(Box.createHorizontalStrut(ConfigFront.SPACE_HORIZONTAL));
		this.add(saveButton);
		
		saveButton.addActionListener(new TableListener(treeViewPanel, this, treePopup));
	}
	
	public String getTableName(){
		return tableName.getText();
	}
	
}
