package edu.cs.dbms.frontend.gui;

import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.cs.dbms.backend.KeyValue.KeyValueStoring;
import edu.cs.dbms.backend.model.Attribute;
import edu.cs.dbms.backend.service.AttributeService;
import edu.cs.dbms.frontend.util.ConfigFront;

import javax.swing.JLabel;
import java.awt.Button;
import java.util.ArrayList;
import java.util.List;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

public class NewEntryPanel extends JPanel {
	private JTextField textField;
	private JLabel lblNewLabel;
	private List<Attribute> list;
	private String db;
	public String getDb() {
		return db;
	}
	public void setDb(String db) {
		this.db = db;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	private String table;
	private JTextField text_field_id;
	/**
	 * Create the panel.
	 */
	public NewEntryPanel(TreeViewPanel treeViewPanel, TreePopup treePopup) {
		setLayout(null);
//ToDo =====for test,  get all attributes of the selected table ===		
		AttributeService attributeService = new AttributeService();
		Attribute attr = treePopup.getAttribute();
		this.db = attr.getDatabaseName();
		this.table = attr.getTableName();
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
					KeyValueStoring kvs = new KeyValueStoring(db, table);
					String key = textFieldList.get(0).getText();
					ArrayList<String> str = new ArrayList<>();
					textFieldList.remove(0);
					for(JTextField  t: textFieldList){
						str.add(t.getText());
					}
					kvs.insert(key, str.toString());
					setVisible(false);
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
		
		JButton btnPrintAll = new JButton("print all");
		btnPrintAll.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				KeyValueStoring kvs = new KeyValueStoring(db, table);
				kvs.getAll();
			}
		});
		btnPrintAll.setBounds(356, 201, 89, 23);
		add(btnPrintAll);
		
		JButton btnNewButton = new JButton("delete");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				KeyValueStoring kvs = new KeyValueStoring(db, table);
				kvs.delete(text_field_id.getText());
			}
		});
		btnNewButton.setBounds(10, 267, 89, 23);
		add(btnNewButton);
		
		text_field_id = new JTextField();
		text_field_id.setBounds(10, 240, 86, 20);
		add(text_field_id);
		text_field_id.setColumns(10);

	}
	public void saveButton(){
		
	}
	public void cancelButton(){
		
	}
}
