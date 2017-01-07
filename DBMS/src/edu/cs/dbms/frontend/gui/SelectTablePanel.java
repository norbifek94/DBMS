package edu.cs.dbms.frontend.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import edu.cs.dbms.backend.key_value.KeyValueStoring;
import edu.cs.dbms.backend.model.Attribute;
import edu.cs.dbms.backend.service.AttributeService;
import edu.cs.dbms.backend.service.DatabaseException;
import edu.cs.dbms.backend.service.ForeignKeyService;
import edu.cs.dbms.backend.util.Config;
import edu.cs.dbms.frontend.util.ConfigFront;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectTablePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private List<String> columnsName;
    private JScrollPane scrollPane;
    private JTable table;
    private JPanel tablePanel;
    private JPanel selectPanel;
    private DefaultTableModel tableModel;
    private JTextArea selectQueryText;
    private JButton selectButton;
    private String databaseName;
    
    public SelectTablePanel(String databaseName){
    	
    	this.databaseName = databaseName;
    	
        columnsName = new ArrayList<String>();
        
        tableModel  = new DefaultTableModel() {
            @Override
            public int getColumnCount() {
                return columnsName.size();
            }

            @Override
            public String getColumnName(int index) {
                return columnsName.get(index);
            }
        };
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        table.getTableHeader().setReorderingAllowed(false);
        
        tablePanel = new JPanel();
        tablePanel.add(scrollPane);
        
        selectPanel = new JPanel(new FlowLayout());
        selectQueryText = new JTextArea(5, 34);
        JScrollPane selectScroll = new JScrollPane (selectQueryText, 
        		   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        selectButton = new JButton("Select");
        selectPanel.add(selectScroll);
        selectPanel.add(selectButton);
 
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(selectPanel);
        
        selectButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String query = selectQueryText.getText();
				query = query.replace("\n", "").replace("\t", "").replaceAll("\\s+", " ");
				String[] queryList = query.split("\\s*(=>|,|\\s)\\s*");
				List<String> columnName = new ArrayList<String>();
				List<String> tableName = new ArrayList<String>();
				List<List<String>> condition = new ArrayList<List<String>>();
				
				int i = 0;
				if(!queryList[i].equalsIgnoreCase("select") || queryList.length < 4){
					JOptionPane.showMessageDialog(selectPanel,"Syntax error1!");
					return;
				}
				i++;
				
				while( i < queryList.length && !queryList[i].equalsIgnoreCase("from")){
					columnName.add(queryList[i]);
					i++;
				}
				if(queryList.length == i){
					JOptionPane.showMessageDialog(selectPanel,"Syntax error2!");
					return;
				}
				if(!queryList[i].equalsIgnoreCase("from")){
					JOptionPane.showMessageDialog(selectPanel,"Syntax error2!");
					return;
				}
				i++;
				
				while( i < queryList.length && !queryList[i].equalsIgnoreCase("where")){
					if(!KeyValueStoring.fileExists(
							new File(Config.KEY_VALUE_PATH_PK + 
									databaseName + "/" + queryList[i]))){
						JOptionPane.showMessageDialog(selectPanel, "Wrong table name! (" + queryList[i] +")");
						return;
					}
					tableName.add(queryList[i]);
					i++;
				}
				if(queryList.length != i){
					if(!queryList[i].equalsIgnoreCase("where") || queryList.length == i+1){
						JOptionPane.showMessageDialog(selectPanel,"Syntax error3!");
						return;
					}
					i++;
					
					int countAnd = 0;
					while( i < queryList.length){
						List<String> c = new ArrayList<String>();
						while(i < queryList.length && !queryList[i].equalsIgnoreCase("and")){
							c.add(queryList[i]);
							i++;
						}
						i++;
						if(c.size() != 3){
							JOptionPane.showMessageDialog(selectPanel,"Syntax error4!");
							return;
						}
						if(!c.get(1).equals(">") && !c.get(1).equals("<") &&
								!c.get(1).equals(">=") && !c.get(1).equals("<=") &&
								!c.get(1).equals("==") && !c.get(1).equals("!=")){
							JOptionPane.showMessageDialog(selectPanel,"Syntax error5!");
							return;
						}
						if(!c.get(1).equals("==") && !c.get(1).equals("!=")){
							if(!c.get(2).matches("^[-+]?\\d+(\\.\\d+)?$")){
								JOptionPane.showMessageDialog(selectPanel,"Syntax error8!");
								return;
							}
						}
						countAnd++;
						condition.add(c);
					}
					if(condition.size() == countAnd + 1){
						JOptionPane.showMessageDialog(selectPanel,"Syntax error6!");
						return;
					}
				}
				
				if(tableName.size() == 1){
					Attribute attr = new Attribute.AttributeBuilder()
											.setDatabaseName(databaseName)
											.setTableName(tableName.get(0))
											.creatAttr();
					AttributeService attrService = new AttributeService();
					List<String> listAttr = attrService.getAttributeNames(attr);
					KeyValueStoring kvs = new KeyValueStoring();
					kvs.setPath(Config.KEY_VALUE_PATH_PK + databaseName, tableName.get(0));
					
					if(columnName.size() == 1){
						 if(columnName.get(0).equals("*")){
							 try{
								 setSelectTable(kvs.select(listAttr, condition), listAttr, listAttr);
							 }catch(DatabaseException ee){
								 JOptionPane.showMessageDialog(selectPanel,"Something wrong, please try again later!");
							 }
							 return;
						 }
					}
					for(String s : columnName){
						if(!listAttr.contains(s)){
							JOptionPane.showMessageDialog(selectPanel,"Wrong attribute name! (" + s +")");
							return;
						}
					}
					for(int j = 0; j < condition.size(); j++){
						if(!listAttr.contains(condition.get(j).get(0))){
							JOptionPane.showMessageDialog(selectPanel,"Wrong attribute name in condition! (" + condition.get(j).get(0) +")");
							return;
						}
					}
					try{
						 setSelectTable(kvs.select(listAttr, condition), listAttr, columnName);
					 }catch(DatabaseException ee){
						 JOptionPane.showMessageDialog(selectPanel,"Something wrong, please try again later!");
					 }
					
				}else if(tableName.size() > 1){
					
				}
				
//				System.out.println(columnName);
//				System.out.println(tableName);
//				System.out.println(condition);
				
			}
		});
    }
    
    private void setSelectTable(Map<String, List<String>> values, List<String> attrList, List<String> columns){
    	tableModel.setRowCount(0);
    	tableModel.setColumnCount(0);
    	
    	columnsName.clear();
    	int i;
    	for(i = 0; i < columns.size(); i++){
    		columnsName.add(columns.get(i));
    	}
    	tableModel.setColumnCount(i);
    	repaint();
    	
    	for(Map.Entry<String, List<String>> entry : values.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            Object obj[] = new Object[columns.size()];
        
            for(int j = 0; j < columns.size(); j++){
            	int index = attrList.indexOf(columns.get(j));
            
            	if(index == 0){
            		obj[j] = key;
            	}else{
            		if(value.size() >= index){
            			obj[j]= value.get(index - 1);
            		}else{
            			obj[j]= "";
            		}
            	}
            }
            tableModel.addRow(obj);
            
        }
    }
    


}
