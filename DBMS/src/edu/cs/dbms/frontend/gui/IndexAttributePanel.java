package edu.cs.dbms.frontend.gui;

import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import edu.cs.dbms.frontend.controller.IndexAttributeListener;
import edu.cs.dbms.frontend.util.ConfigFront;

public class IndexAttributePanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	private JButton saveButton;
	private JComboBox<String> indexFilesName;
	private JLabel label;
	
	
	public IndexAttributePanel(TreeViewPanel treeViewPanel, TreePopup treePopup){
		
		BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
		this.setLayout(layout);
		saveButton = new JButton(ConfigFront.ADD_TO_INDEX_FILE);
		indexFilesName = new JComboBox<String>();
		label = new JLabel(ConfigFront.DATABASE_NAME_LABEL);
		
		this.setBorder(new EmptyBorder(
							ConfigFront.INDEX_FILE_PANEL_TOP_BORDER,
							ConfigFront.INDEX_FILE_PANEL_LEFT_BORDER,
							ConfigFront.INDEX_FILE_PANEL_BOTTON_BORDER,
							ConfigFront.INDEX_FILE_PANEL_RIGHT_BORDER));
		this.add(label);
		this.add(Box.createHorizontalStrut(ConfigFront.SPACE_HORIZONTAL));
		this.add(indexFilesName);
		this.add(Box.createHorizontalStrut(ConfigFront.SPACE_HORIZONTAL));
		this.add(saveButton);
		
		saveButton.addActionListener(new IndexAttributeListener(treeViewPanel, this, treePopup));
	}
	public void setComboBox(List<String> files){
		
		for(String f : files){
			indexFilesName.addItem(f);
		}
	}
	
	public String getIndexFileName(){
		return indexFilesName.getSelectedItem().toString();
	}
}
