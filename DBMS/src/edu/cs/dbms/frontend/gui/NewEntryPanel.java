package edu.cs.dbms.frontend.gui;

import javax.swing.JOptionPane;
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

import javax.swing.JButton;

public class NewEntryPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField textField;
	private JLabel lblNewLabel;
	private List<Attribute> list;
	private String db;
	private String table;
	private JTextField text_field_id;
	
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
	
	/**
	 * Create the panel.
	 */
	public NewEntryPanel(TreeViewPanel treeViewPanel, TreePopup treePopup) {
		
		JPanel panel = this;
		setLayout(null);
		//TODO =====for test,  get all attributes of the selected table ===		
		AttributeService attributeService = new AttributeService();
		Attribute attr = treePopup.getAttribute();
		this.db = attr.getDatabaseName();
		this.table = attr.getTableName();
		list = attributeService.getAttribute(attr);
		
		//TODO End		
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
					
//					KeyValueStoring kvs = new KeyValueStoring(db, table, Config.KEY_VALUE_PATH_PK);
//					String key = textFieldList.get(0).getText();
					ArrayList<String> str = new ArrayList<>();
					textFieldList.remove(0);
					
					for(JTextField  t: textFieldList){
						String text = t.getText().toString();
						if(!text.isEmpty()){
							str.add(t.getText());
						}else{
							JOptionPane.showMessageDialog(panel, "Please enter all data!");
							return;
						}
					}
//					kvs.insert(key, str.toString());
					
					Attribute attr = new Attribute.AttributeBuilder()
												  .setDatabaseName(db)
												  .setTableName(table)
												  .creatAttr();
					AttributeService attrService = new AttributeService();
					List<Attribute> attrList = attrService.getAttribute(attr);
//					KeyValueStoring kvs1 = new KeyValueStoring(db, table, Config.KEY_VALUE_PATH_UQ);
					
					for(Attribute a : attrList){
						if(a.isUnique()){
							for(int i = 0; i < labelList.size(); i++){
								if(labelList.get(i).getText().equals(a.getAttrName())){
									System.out.println("Label: " + labelList.get(i).getText() + " Text: " + textFieldList.get(i-1).getText());
//									kvs1.insert(textFieldList.get(i-1).getText(), key);
								}
							}
						}
					}
					
					panel.setVisible(false);
			}
		});
		button.setBounds(356, 268, 70, 22);
		add(button);
		
		JButton btnPrintAll = new JButton("print all");
		btnPrintAll.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
//				KeyValueStoring kvs = new KeyValueStoring(db, table, Config.KEY_VALUE_PATH_PK);
				System.out.println("PrimaryKey:");
//				kvs.getAll();
				System.out.println("UniqueKey:");
//				KeyValueStoring kvs1 = new KeyValueStoring(db, table, Config.KEY_VALUE_PATH_UQ);
//				kvs1.getAll();
			}
		});
		btnPrintAll.setBounds(356, 201, 89, 23);
		add(btnPrintAll);
		
		JButton btnNewButton = new JButton("delete");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
//				KeyValueStoring kvs = new KeyValueStoring(db, table, Config.KEY_VALUE_PATH_UQ);
//				kvs.delete(text_field_id.getText());
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
