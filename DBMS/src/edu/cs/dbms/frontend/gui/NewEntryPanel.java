package edu.cs.dbms.frontend.gui;

import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.cs.dbms.backend.model.Attribute;
import edu.cs.dbms.backend.service.AttributeService;
import edu.cs.dbms.frontend.util.ConfigFront;

import javax.swing.JLabel;
import java.awt.Button;
import java.util.ArrayList;
import java.util.List;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NewEntryPanel extends JPanel {
	private JTextField textField;
	private JLabel lblNewLabel;
	private List<Attribute> list;
	/**
	 * Create the panel.
	 */
	public NewEntryPanel(TreeViewPanel treeViewPanel, TreePopup treePopup) {
		setLayout(null);
//ToDo =====for test,  get all attributes of the selected table ===		
		AttributeService attributeService = new AttributeService();
		Attribute attr = treePopup.getAttribute();
		
		list = attributeService.getAttribute(attr);
		
		
//ToDo End		
		ArrayList<JLabel> labelList = new ArrayList<>();
		ArrayList<JTextField> textFieldList = new ArrayList<>();
		
		
		int Y = 8;
		for(Attribute at : list){
			lblNewLabel = new JLabel(at.getAttrName());
			lblNewLabel.setBounds(100, Y, 60, 14);
			add(lblNewLabel);
			
			textField = new JTextField();
			textField.setBounds(182, Y, 120, 20);
			add(textField);
			textField.setColumns(10);
			labelList.add(lblNewLabel);
			textFieldList.add(textField);
			Y=Y+24;
		}
		
	
		
		Button button = new Button(ConfigFront.SAVE_NEW_ENTRY);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
					// ToDo meg kell nezni hogy a tipusok amiket beirt a felhasznalo megfeleloek e?		
					//insert
			}
		});
		button.setBounds(356, 268, 70, 22);
		add(button);
		
		Button button_1 = new Button(ConfigFront.CANCEL_SAVE);
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
		});
		button_1.setBounds(356, 240, 70, 22);
		add(button_1);

	}

}
