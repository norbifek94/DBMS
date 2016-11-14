package edu.cs.dbms.frontend.gui;

import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.cs.dbms.backend.model.Attribute;
import edu.cs.dbms.frontend.util.ConfigFront;

import javax.swing.JLabel;
import java.awt.Button;
import java.util.ArrayList;

import javax.swing.JScrollBar;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NewEntryPanel extends JPanel {
	private JTextField textField;
	private JLabel lblNewLabel;
	private ArrayList<Attribute> list;
	/**
	 * Create the panel.
	 */
	public NewEntryPanel() {
		setLayout(null);
//ToDo =====for test,  get all attributes of the selected table ===		
		Attribute at1 = new Attribute("dbName1","tableName1","attrName1","type1","len1","null1",false,false);
		Attribute at2 = new Attribute("dbName1","tableName1","attrName2","type2","len2","null2",false,false);
		Attribute at3 = new Attribute("dbName1","tableName1","attrName3","type3","len3","null3",false,false);
		Attribute at4 = new Attribute("dbName1","tableName1","attrName4","type4","len4","null4",false,false);
		Attribute at5 = new Attribute("dbName1","tableName1","attrName5","type5","len5","null5",false,false);
		Attribute at6 = new Attribute("dbName1","tableName1","attrName6","type6","len6","null6",false,false);
		Attribute at7 = new Attribute("dbName1","tableName1","attrName7","type7","len7","null7",false,false);
		Attribute at8 = new Attribute("dbName1","tableName1","attrName8","type8","len8","null8",false,false);
		Attribute at9 = new Attribute("dbName1","tableName1","attrName9","type9","len9","null9",false,false);
		
		list = new ArrayList<>();
		list.add(at1);
		list.add(at2);
		list.add(at3);
		list.add(at4);
		list.add(at5);
		list.add(at6);
		list.add(at7);
		list.add(at8);
		list.add(at9);
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
