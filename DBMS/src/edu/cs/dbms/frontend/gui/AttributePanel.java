package edu.cs.dbms.frontend.gui;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import edu.cs.dbms.frontend.controller.AttributeListener;
import edu.cs.dbms.frontend.util.ConfigFront;

public class AttributePanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private static final String[] typeArray = {"char", "int"};
	
	private JButton saveButton;
	private JTextField textFieldAttributeName;
	private JPanel panelForBox;
	private JPanel panelForCheck;
	
	private JComboBox<String> comboBoxType;
	private JComboBox<Integer> comboBoxLength;
	private JCheckBox checkBoxPrimaryKey;
	private JCheckBox checkBoxUniqueKey;
	private JCheckBox checkBoxIsNull;
	
	private JLabel labelAttributeName;
	private JLabel labelType;
	private JLabel labelLength;
	
	public AttributePanel(TreeViewPanel treeViewPanel, TreePopup treePopup){
			
		SpringLayout springLayout = new SpringLayout();
		panelForBox = new JPanel();
		panelForCheck = new JPanel();
		
		panelForBox.setLayout(springLayout);
		panelForCheck.setLayout(springLayout);
		
		this.setLayout(springLayout);
		
		Integer[] length = new Integer[ConfigFront.ATTRIBUTE_LENGTH_NUM];
		for(int i = 0; i < ConfigFront.ATTRIBUTE_LENGTH_NUM; i++){
			length[i] = i + 1;
		}
		
		saveButton = new JButton(ConfigFront.ATTRIBUTE_SAVE_BUTTON);
		textFieldAttributeName = new JTextField(15);
		comboBoxType = new JComboBox<String>(typeArray);
		comboBoxLength = new JComboBox<Integer>(length);
		checkBoxIsNull = new JCheckBox(ConfigFront.ATTRIBUTE_IS_NULL);
		checkBoxIsNull.setSelected(false);
		checkBoxPrimaryKey = new JCheckBox(ConfigFront.ATTRIBUTE_PRIMARY_KEY);
		checkBoxPrimaryKey.setSelected(false);
		checkBoxUniqueKey = new JCheckBox(ConfigFront.ATTRIBUTE_UNIQUE_KEY);
		checkBoxUniqueKey.setSelected(false);

		labelAttributeName = new JLabel(ConfigFront.ATTRIBUTE_NAME);
		labelType = new JLabel(ConfigFront.ATTRIBUTE_TYPE);
		labelLength = new JLabel(ConfigFront.ATTRIBUTE_LENGTH);
		
		this.setBorder(new EmptyBorder(
							ConfigFront.ATTRIBUTE_PANEL_TOP_BORDER,
							ConfigFront.ATTRIBUTE_PANEL_LEFT_BORDER,
							ConfigFront.ATTRIBUTE_PANEL_BOTTON_BORDER,
							ConfigFront.ATTRIBUTE_PANEL_RIGHT_BORDER));
		panelForBox.add(labelAttributeName);
		panelForBox.add(textFieldAttributeName);
		panelForBox.add(labelType);
		panelForBox.add(comboBoxType);
		panelForBox.add(labelLength);
		panelForBox.add(comboBoxLength);
		panelForCheck.add(checkBoxIsNull);
		panelForCheck.add(checkBoxPrimaryKey);
		panelForCheck.add(checkBoxUniqueKey);
		panelForCheck.add(saveButton);
		
		this.add(panelForBox);
		this.add(panelForCheck);
		SpringUtilities.makeCompactGrid(panelForBox, 3, 2, 6, 10, 3, 10);
		SpringUtilities.makeCompactGrid(panelForCheck, 4, 1, 6, 120, 3, 10);
		
		saveButton.addActionListener(new AttributeListener(treeViewPanel, this, treePopup));
		
	}
	
	public String getAttributeName(){
		return textFieldAttributeName.getText();
	}

	public String getType(){
		return comboBoxType.getSelectedItem().toString();
	}
	
	public String getLength(){
		return comboBoxLength.getSelectedItem().toString();
	}
	
	public boolean isPrimaryKey(){
		return checkBoxPrimaryKey.isSelected();
	}
	
	public boolean isUniqueKey(){
		return checkBoxUniqueKey.isSelected();
	}
	
	public boolean isNull(){
		return checkBoxIsNull.isSelected();
	}
}
