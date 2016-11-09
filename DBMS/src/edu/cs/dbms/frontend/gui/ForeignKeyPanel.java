package edu.cs.dbms.frontend.gui;

import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import edu.cs.dbms.frontend.controller.ForeignKeyListener;
import edu.cs.dbms.frontend.util.ConfigFront;

public class ForeignKeyPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	private JButton saveButton;
	private JComboBox<String> tableName;
	private JLabel label;

	private String actTableName;
	
	public ForeignKeyPanel(TreeViewPanel treeViewPanel, TreePopup treePopup, String button){
		BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
		this.setLayout(layout);

		saveButton = new JButton(button);
		tableName = new JComboBox<String>();
		label = new JLabel(ConfigFront.TABLE_NAME_LABEL);
		
		this.setBorder(new EmptyBorder(
							ConfigFront.INDEX_FILE_PANEL_TOP_BORDER,
							ConfigFront.INDEX_FILE_PANEL_LEFT_BORDER,
							ConfigFront.INDEX_FILE_PANEL_BOTTON_BORDER,
							ConfigFront.INDEX_FILE_PANEL_RIGHT_BORDER));
		this.add(label);
		this.add(Box.createHorizontalStrut(ConfigFront.SPACE_HORIZONTAL));
		this.add(tableName);
		this.add(Box.createHorizontalStrut(ConfigFront.SPACE_HORIZONTAL));
		this.add(saveButton);
		actTableName = treePopup.getAttribute().getTableName();
		saveButton.addActionListener(new ForeignKeyListener(treeViewPanel, this, treePopup));
	}
	public void setComboBox(List<String> files){

		for(String f : files){
			if(!f.equals(actTableName)){
				tableName.addItem(f);
			}
		}
	}
	
	public String getTableName(){
		return tableName.getSelectedItem().toString();
	}

}
