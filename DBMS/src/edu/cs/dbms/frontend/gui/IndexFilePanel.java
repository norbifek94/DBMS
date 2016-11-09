package edu.cs.dbms.frontend.gui;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import edu.cs.dbms.frontend.controller.IndexFileListener;
import edu.cs.dbms.frontend.util.ConfigFront;

public class IndexFilePanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private static final String[] typeArray = {"BTree"};
	
	private JButton saveButton;
	private JPanel panelForBox;
	private JPanel panelForCheck;
	
	private JComboBox<String> comboBoxType;
	private JComboBox<Integer> comboBoxLength;
	private JCheckBox checkBoxIsUnique;
	
	private JLabel labelKeyLength;
	private JLabel labelType;
	
	public IndexFilePanel(TreeViewPanel treeViewPanel, TreePopup treePopup){

		SpringLayout springLayout = new SpringLayout();
		panelForBox = new JPanel();
		panelForCheck = new JPanel();
		
		panelForBox.setLayout(springLayout);
		panelForCheck.setLayout(springLayout);
		
		this.setLayout(springLayout);
		
		Integer[] length = new Integer[ConfigFront.INDEX_FILE_LENGTH_NUM];
		for(int i = 0; i < ConfigFront.INDEX_FILE_LENGTH_NUM; i++){
			length[i] = i + 1;
		}
		
		saveButton = new JButton(ConfigFront.INDEX_FILE_SAVE_BUTTON);
		comboBoxType = new JComboBox<String>(typeArray);
		comboBoxLength = new JComboBox<Integer>(length);
		checkBoxIsUnique = new JCheckBox(ConfigFront.INDEX_FILE_UNIQUE);
		checkBoxIsUnique.setSelected(false);

		labelType = new JLabel(ConfigFront.INDEX_FILE_TYPE);
		labelKeyLength = new JLabel(ConfigFront.INDEX_FILE_KEY_LENGTH);

		this.setBorder(new EmptyBorder(
							ConfigFront.INDEX_FILE_PANEL_TOP_BORDER,
							ConfigFront.INDEX_FILE_PANEL_LEFT_BORDER,
							ConfigFront.INDEX_FILE_PANEL_BOTTON_BORDER,
							ConfigFront.INDEX_FILE_PANEL_RIGHT_BORDER));
		
		panelForBox.add(labelType);
		panelForBox.add(comboBoxType);
		panelForBox.add(labelKeyLength);
		panelForBox.add(comboBoxLength);
		panelForCheck.add(checkBoxIsUnique);
		panelForCheck.add(saveButton);
		
		this.add(panelForBox);
		this.add(panelForCheck);
		SpringUtilities.makeCompactGrid(panelForBox, 2, 2, 6, 60, 3, 10);
		SpringUtilities.makeCompactGrid(panelForCheck, 2, 1, 6, 130, 3, 10);
		
		saveButton.addActionListener(new IndexFileListener(treeViewPanel, this, treePopup));
		
	}
	
	public String getType(){
		return comboBoxType.getSelectedItem().toString();
	}
	
	public String getLength(){
		return comboBoxLength.getSelectedItem().toString();
	}
	
	public boolean isUnique(){
		return checkBoxIsUnique.isSelected();
	}

}
