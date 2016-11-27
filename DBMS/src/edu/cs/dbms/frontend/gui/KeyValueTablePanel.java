package edu.cs.dbms.frontend.gui;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import edu.cs.dbms.backend.key_value.KeyValueStoring;
import edu.cs.dbms.backend.model.Attribute;
import edu.cs.dbms.backend.model.ForeignKey;
import edu.cs.dbms.backend.service.AttributeService;
import edu.cs.dbms.backend.service.DatabaseException;
import edu.cs.dbms.backend.service.TableService;
import edu.cs.dbms.backend.util.Config;
import edu.cs.dbms.frontend.util.ConfigFront;

public class KeyValueTablePanel extends JPanel{

	private static final long serialVersionUID = 1L;

    private JScrollPane scrollPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private int tableRowCount;
    private List<Attribute> attrList;
    private List<ForeignKey> fkList;
    private KeyValueTablePanel panel;
    private String databaseName;
    private String tableName;
    
    public KeyValueTablePanel(List<Attribute> attrList, List<ForeignKey> fkList){
    	
    	this.attrList = attrList;
    	this.fkList = fkList;
    	this.panel = this;
    	this.databaseName = attrList.get(0).getDatabaseName();
    	this.tableName = attrList.get(0).getTableName();
    	
        //jtable model
    	//egy tablanal osszes attributumat es hozza tartozo kulso kucsok nevet megjelenit
    	tableModel  = new DefaultTableModel() {
			@Override
            public int getColumnCount() {return attrList.size() + fkList.size();}
            @Override
            public String getColumnName(int index) {
            	if(index < fkList.size()){
            		return fkList.get(index).getReferens().getTableName() + 
            				"." + fkList.get(index).getReferens().getAttrName();
            	}
            	return attrList.get(index - fkList.size()).getAttrName();
            }
        };

        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        table.getTableHeader().setReorderingAllowed(false); 
        //uj sor menetese
        JButton save = new JButton(ConfigFront.SAVE_NEW_ENTRY);
        //egy adot sor update
        JButton update = new JButton(ConfigFront.UPDATE_ENTRY);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        buttonPanel.add(update);
        buttonPanel.add(save);
        
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(buttonPanel);
                
        //betoltom az adatokat
		try{
			KeyValueStoring kvs = new KeyValueStoring();
			kvs.setPath(Config.KEY_VALUE_PATH_PK + databaseName, tableName);
			panel.setTable(kvs.getAll());
		}catch (DatabaseException exp) {
			JOptionPane.showMessageDialog(panel, exp);
		}
		
		//a tablahoz szukseges listener
        table.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mousePressed(MouseEvent e) {
        		
                //jobb click
        		if(SwingUtilities.isRightMouseButton(e)){
        			//uj elem beszurasa
        			JPopupMenu menu = new JPopupMenu();
        			JMenuItem itemAddNew = new JMenuItem("Add new");
        			menu.add(itemAddNew);
        			KeyValueStoring kvs = new KeyValueStoring();
        			
        			//ha van kijelolt sor akkor torles is lehetseges
        			if(table.getSelectedRowCount() > 0){
        				JMenuItem itemDelet = new JMenuItem("Delete");
        				menu.add(new JSeparator());
        				menu.add(itemDelet);

        				itemDelet.addMouseListener(new MouseAdapter() {
            				@Override
            				public void mousePressed(MouseEvent e) {
            					try{
            						delete();
            						kvs.setPath(Config.KEY_VALUE_PATH_PK + databaseName, tableName);
            						setTable(kvs.getAll());
            					}catch(DatabaseException exp){
            						JOptionPane.showMessageDialog(panel, exp.getMessage());
            					}
            				}
    					});
        			}
        			//ures sor hozzaadasa a tablahoz
        			itemAddNew.addMouseListener(new MouseAdapter() {
        				@Override
        				public void mousePressed(MouseEvent e) {
        					newRow();
        				}
					});
        			menu.show(e.getComponent(), e.getX(), e.getY());
        		}
        	}
		});
        
        //uj elem mentese
        save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
        
        //uppdate eseten ha kivan jelolve egy sor akkor popup window jelenjen meg
        update.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
        
    }

    //feltoltok minden sort a neki megfelelo ertekkel
    public void setTable(Map<String, List<String>> values){
        tableModel.setRowCount(0);
        table.repaint();
        if(values.isEmpty()){
        	newRow();
        	tableRowCount = 0;
			return;
        }
        
        for(Map.Entry<String, List<String>> entry : values.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            Object obj[] = new Object[tableModel.getColumnCount()];
            int foreignSize = fkList.size();

            for(int i = 0; i < attrList.size(); i++){
            	if(attrList.get(i).isPrimary()){
            		obj[i + foreignSize] = key;
            	}
            }
            
            KeyValueStoring kvs = new KeyValueStoring();
            for(int i = 0; i < foreignSize; i++){
            	kvs.setPath(Config.KEY_VALUE_PATH_FK + fkList.get(i).getDatabaseName() + 
            			"_" + fkList.get(i).getTableName(), fkList.get(i).getReferens().getTableName());
            	obj[i] = kvs.getForeignKeyByPrimary(key);
            }
            //System.out.println("SIZE: " + value.size() + " " + obj.length + " " + tableModel.getColumnCount());
            if(!value.get(0).equals("")){
	            for(int i = 0; i < value.size(); i++){
	            	 //System.out.println("SIZE: " + i + " " + value.get(i));
	            	
            		obj[i + foreignSize+1] = value.get(i);
	            	
	            }
            }
            tableModel.addRow(obj);
        }
        tableRowCount = tableModel.getRowCount();
    }
    
    //uj sor eseten letrehozom az ures cellakat es ha kulso kulcs akkor combobox-t
    private void newRow(){
    	Object obj[] = new Object[fkList.size() + attrList.size()];
		tableModel.addRow(obj);
		for(int i = 0; i < fkList.size(); i++){
			TableColumn foreignKeyColumn = table.getColumnModel().getColumn(i);
			foreignKeyColumn.setCellEditor(new DefaultCellEditor(setForeignComboBox(i)));
		}
		table.repaint();
    } 
    
    //feltoltom minden sor comboboxjat valid kulso kulcs ertekekkel
    private JComboBox<String> setForeignComboBox(int i){
    	
    	KeyValueStoring kvs = new KeyValueStoring();
		JComboBox<String> comboBox = new JComboBox<String>();
		
		kvs.setPath(Config.KEY_VALUE_PATH_FK + fkList.get(i).getDatabaseName() + 
    			"_" + fkList.get(i).getTableName(), fkList.get(i).getReferens().getTableName());
		
		List<String> keys = kvs.getForeignKeysByTable();
		
		for(int j = 0; j < keys.size(); j++){
			comboBox.addItem(keys.get(j));
		}
		return comboBox;
    }
     
    //egy sor torlese a tablabol
    private void delete(){
    	
		KeyValueStoring kvs = new KeyValueStoring();
		int rows[] = table.getSelectedRows();
		
		for(int r = 0; r < rows.length; r++){
			String key = "";
			//attributumok torlese(unique es rimary key)
			for(int i = 0; i < attrList.size(); i++){
				if(attrList.get(i).isPrimary()){
					
					key = table.getValueAt(rows[r],i + fkList.size()).toString();
					TableService tbService = new TableService();
					List<String> tbList = tbService.getTable(databaseName);
					
					for(int j = 0; j < tbList.size(); j++){
						if(!tbList.get(j).equals(tableName)){
							kvs.setPath(Config.KEY_VALUE_PATH_FK + databaseName + "_" + tbList.get(j), tableName );
							if(kvs.isForeignKey(key)){
								throw new DatabaseException("Delete failed!");
							}
						}
					}	
					try{
						kvs.setPath(Config.KEY_VALUE_PATH_PK + databaseName, tableName);
						kvs.delete(table.getValueAt(rows[r],i + fkList.size()).toString());
					}catch(Exception exp){
						throw new DatabaseException("Delete failed!");
					}
				}
				//egyedi kulcsok kozul vao torles
				if(attrList.get(i).isUnique() && !attrList.get(i).isPrimary()){
					try{
						
						kvs.setPath(Config.KEY_VALUE_PATH_UQ + databaseName + "/" + tableName, 
								attrList.get(i).getAttrName());
						kvs.delete(table.getValueAt(rows[r],i + fkList.size()).toString());
					}catch(Exception exp){
						throw new DatabaseException("Delete failed!");
					}
				}
				//index file kozul torles
				if(KeyValueStoring.fileExists(new File(
						Config.KEY_VALUE_PATH_IF + databaseName + "/" + tableName + 
							"/" + attrList.get(i).getAttrName()))){
					kvs.setPath(Config.KEY_VALUE_PATH_IF + databaseName + "/" + tableName, 
									attrList.get(i).getAttrName());
					kvs.deletePrimaryFromIndexFile(key);
					
				}
			}
			//foreign key torlese
			for(int j = 0; j < fkList.size(); j++){
				
				try{
					kvs.setPath(Config.KEY_VALUE_PATH_FK + fkList.get(j).getDatabaseName() + 
	            			"_" + fkList.get(j).getTableName(), fkList.get(j).getReferens().getTableName());
					kvs.deletePrimaryFromForeign(key);
				}catch(Exception exp){
					throw new DatabaseException("Delete failed!");
				}
			}
		}
    }
    
    private void deleteUniqueKey(){
    	    	
		KeyValueStoring kvs = new KeyValueStoring();
		int rows[] = table.getSelectedRows();
		
		for(int r = 0; r < rows.length; r++){
			//attributumok torlese(unique es rimary key)
			for(int i = 0; i < attrList.size(); i++){
				if(attrList.get(i).isUnique() && !attrList.get(i).isPrimary()){
					try{
						kvs.setPath(Config.KEY_VALUE_PATH_UQ + databaseName + "/" + tableName, 
								attrList.get(i).getAttrName());
						kvs.delete(table.getValueAt(rows[r],i + fkList.size()).toString());
					}catch(Exception exp){
						System.out.println(exp.getMessage());
					}
				}
			}
		}    	    
    }
        
    private void save(){
    	
    	//tableRowCount==>az eddig meglevo sorok szama
		for(int i = tableRowCount; i < tableModel.getRowCount(); i++){
			String key = new String("");
			Map<String, List<String>> unique = new HashMap<String, List<String>>();
			Map<String, List<String>> indexFile = new HashMap<String, List<String>>();
			List<String> values = new ArrayList<String>();
			List<String> foreign = new ArrayList<String>();
			
			//kiveszem mindegyik attributum erteket majd elmentem
			for(int j = 0; j < tableModel.getColumnCount(); j++){
				//ha ures cella van akkor kilep
				if(table.getValueAt(i, j) == null){
					JOptionPane.showMessageDialog(panel, "Please enter all data!");
					return;
				}
				//kimentem a kulso kulcs ertekeket
				if(fkList.size() > 0 && fkList.size() > j){
					foreign.add(table.getValueAt(i, j).toString());
				}
				//ha nem kulso kulcs akkor attributum
				if(j >= fkList.size()){
					
					//elsodleges kulcs?
					if(attrList.get(j - fkList.size()).isPrimary()){
						key = table.getValueAt(i, j).toString();
						
					//kulomben atlagos ertek
					}else{
						values.add(table.getValueAt(i, j).toString());
					}
					
					//ha egyedi akkor kulon szedem oket
					if(!attrList.get(j- fkList.size()).isPrimary() && attrList.get(j- fkList.size()).isUnique()){
						List<String> v = new ArrayList<String>();
						v.add(key);
						v.add(table.getValueAt(i, j).toString());
						unique.put(attrList.get(j- fkList.size()).getAttrName(),v);
					}
					
					//ha index file-ja van
					if(KeyValueStoring.fileExists(
							new File(Config.KEY_VALUE_PATH_IF + 
									attrList.get(j- fkList.size()).getDatabaseName() + "/" + 
									attrList.get(j- fkList.size()).getTableName() + "/" + 
									attrList.get(j- fkList.size()).getAttrName()))){
						List<String> v = new ArrayList<String>();
						v.add(key);
						v.add(table.getValueAt(i, j).toString());
						indexFile.put(attrList.get(j- fkList.size()).getAttrName(),v);
					}
				}
			}
			//elmentem a sort
			if(!key.isEmpty()){
				try{
					saveUniqueKey(unique);
					savePrimaryKey(key, values);
					saveForeignKeyValue(foreign, key);
					saveForeignKey(key);
					saveIndexFile(key, indexFile);
				}catch(DatabaseException exp){
					JOptionPane.showMessageDialog(panel, exp.getMessage());
				}
			}
		}
    }
    
    public void saveIndexFile(String primaryKey, Map<String, List<String>> indexKeys){
    	KeyValueStoring kvs = new KeyValueStoring();
    	//elmentem unique szerint
		for(Map.Entry<String, List<String>> entry : indexKeys.entrySet()) {
			System.out.println("BENT");
			String attrName = entry.getKey();
			List<String> keyAndPrimary = entry.getValue();
			kvs.setPath(Config.KEY_VALUE_PATH_IF + 
					databaseName + "/" + tableName, attrName);
			kvs.insertIndexFile(keyAndPrimary.get(1), keyAndPrimary.get(0));
		}
    }
    
    public void savePrimaryKey(String primaryKey, List<String> values) throws DatabaseException{
    	
    	KeyValueStoring kvs = new KeyValueStoring();
		//elmentem primary szerint
		try{
			kvs.setPath(Config.KEY_VALUE_PATH_PK + databaseName, tableName);
			kvs.insert(primaryKey, values.toString());
			tableRowCount++;
			
		}catch(DatabaseException exp){
			try{
				kvs.setPath(Config.KEY_VALUE_PATH_PK + databaseName, tableName);
				panel.setTable(kvs.getAll());
			}catch (DatabaseException exp2) {
			}
			try{
				deleteUniqueKey();
			}catch(DatabaseException exp2){}
			throw new DatabaseException(exp.getMessage());
		}
    }
    
    //foreign a kulcs es a primary a value
    public void saveForeignKeyValue(List<String> foreign, String primaryKey) throws DatabaseException{
    	
    	KeyValueStoring kvs = new KeyValueStoring();
    	for(int f = 0; f < foreign.size(); f++){
			try{
				kvs.setPath(Config.KEY_VALUE_PATH_FK + fkList.get(f).getDatabaseName() +
						"_" + fkList.get(f).getTableName(), fkList.get(f).getReferens().getTableName());
				kvs.insertPrimaryInForeign(foreign.get(f), primaryKey);
			}catch(DatabaseException exp){
				try{
					deleteUniqueKey();
				}catch(DatabaseException exp2){}
			}
		}
    }
   
    public void saveForeignKey(String primaryKey) throws DatabaseException{
    	//ha valahol kulso kulcskent van beallitva az elsodleges kulcs akkor elmentem
		//a kulcsokulcsokhoz is
    	for(int j = 0; j < attrList.size(); j++){
			if(attrList.get(j).isForignKey()){
				AttributeService attributeService = new AttributeService();
				String table = attributeService.getForeignKeyLocation(attrList.get(j));
				KeyValueStoring kvs = new KeyValueStoring();
				//elmentem primary keyt kulsokulcsnak ahol referencia
				try{
					kvs.setPath(Config.KEY_VALUE_PATH_FK + attrList.get(j).getDatabaseName() +
							"_" + table, attrList.get(j).getTableName());
					kvs.insert(primaryKey, "");
				}catch(DatabaseException exp){
					try{
						deleteUniqueKey();
					}catch(DatabaseException exp2){}
				}
			}
    	}
    }
    
    public void saveUniqueKey(Map<String, List<String>> unique) throws DatabaseException{
    	KeyValueStoring kvs = new KeyValueStoring();
    	//elmentem unique szerint
		for(Map.Entry<String, List<String>> entry : unique.entrySet()) {
			List<String> v = entry.getValue();
			try{
				kvs.setPath(Config.KEY_VALUE_PATH_UQ + databaseName + "/" + tableName, entry.getKey());
				System.out.println(v.get(1) + "   " +v.get(0));
				kvs.insert(v.get(1), v.get(0));
			}catch(DatabaseException exp){
				try{
					deleteUniqueKey();
				}catch(DatabaseException e){}
				throw new DatabaseException(exp.getMessage());
			}
		}
    }
    
    private void update(){
    	//csak egy sort lehet updatelni egyszerre
    	if(table.getSelectedRowCount() == 1 ){
			//a popup window
			JDialog updateWindow= new JDialog();
			//a labelek es input field -ek panele
			JPanel updatePanel = new JPanel();
			//fieldek listaja
			List<JTextField> updateFields = new ArrayList<JTextField>();
			List<JComboBox<String>> updateComboBoxs = new ArrayList<JComboBox<String>>();
			JButton updateSave = new JButton(ConfigFront.SAVE_NEW_ENTRY);
			
			//alap beallitasok
			updatePanel.setLayout(new SpringLayout());
			updateWindow.setLayout(new BorderLayout());
			updateWindow.setPreferredSize(new Dimension(ConfigFront.FRAME_WIDTH / 2,ConfigFront.FRAME_HEIGHT / 2));
			updateWindow.setResizable(false);
			updateWindow.pack();
			updateWindow.setLocationRelativeTo(null);
			updateWindow.setVisible(true);
			updateWindow.add(updatePanel, BorderLayout.CENTER);
			updateWindow.add(updateSave, BorderLayout.PAGE_END);
			
			//labelek, fieldek es ertekek betoltese
			for(int i = 0; i < tableModel.getColumnCount(); i++){
				JLabel label;
				if(i < fkList.size()){
					label = new JLabel(fkList.get(i).getReferens().getTableName() + 
            				"." + fkList.get(i).getReferens().getAttrName() + ": ");
					JComboBox<String> comboBox = setForeignComboBox(i);
					if(table.getValueAt(table.getSelectedRow(), i) == null){
						comboBox.setSelectedIndex(0);
					}else{
						comboBox.setSelectedItem(table.getValueAt(table.getSelectedRow(), i).toString());
					}
					updatePanel.add(label);
					updatePanel.add(comboBox);
					updateComboBoxs.add(comboBox);
            	}else{
            		label = new JLabel(attrList.get(i - fkList.size()).getAttrName() + ": ");
            		JTextField field = new JTextField();
					if(table.getValueAt(table.getSelectedRow(), i) != null){
						field.setText(table.getValueAt(table.getSelectedRow(), i).toString());
					}
					updatePanel.add(label);
					updatePanel.add(field);
					updateFields.add(field);
            	}
			}
			//elrendezes a labeleknek es filedeknek
			SpringUtilities.makeCompactGrid(updatePanel, tableModel.getColumnCount(), 2, 100, 10, 20, 10);
			JScrollPane updateScroll = new JScrollPane(updatePanel,
	                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			updateWindow.add(updateScroll);
			
			//mentesi button
			updateSave.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean primary = false;		//elsodleges kulcs valtozott
					boolean unique = false;			//egyedi kulcs valtozott
					boolean foreign = false;		//kulso kulcs valtozott
					boolean changeValue = false;	//altalanos attributum erteke valtozott
					String key = null;
					String oldPrimaryKey = null;
					List<String> valuesList = new ArrayList<String>();
					List<String> foreignList = new ArrayList<String>();
					Map<String, List<String>> uniqueList = new HashMap<String, List<String>>();
					Map<String, List<String>> indexFile = new HashMap<String, List<String>>();
					
					//minden erteket szetvalogatok listagba es hasitotablakba
					//megjegyzem h a fontosabb attributumok valtoztake
					//(primary key, unique key, foreign key, index file key)
					//es ennek megfeleloen updatelem a k-v file-kat
					for(int i = fkList.size(); i < tableModel.getColumnCount(); i++){
						String valueCell;
						if(table.getValueAt(table.getSelectedRow(), i) == null){
							valueCell = "";
						}else{
							valueCell = table.getValueAt(table.getSelectedRow(), i).toString();
						}
						int j = i - fkList.size();
						
						if(attrList.get(j).isPrimary()){
							if(!valueCell.equals(updateFields.get(j).getText())){
								primary = true;
							}
							oldPrimaryKey = valueCell;
							key = updateFields.get(j).getText();
						}else if(attrList.get(j).isUnique()){
							if(!valueCell.equals(updateFields.get(j).getText())){
								unique= true;
							}
							List<String> u = new ArrayList<String>();
							u.add(key);
							u.add(updateFields.get(j).getText());
							u.add(valueCell);
							uniqueList.put(attrList.get(j).getAttrName(), u);
							valuesList.add(updateFields.get(j).getText());
						}else{
							boolean done = false;
							if(primary){
								if(KeyValueStoring.fileExists(new File(
										Config.KEY_VALUE_PATH_IF + databaseName + "/" + tableName + 
											"/" + attrList.get(j).getAttrName()))){
									
									List<String> list = new ArrayList<String>();
									list.add(updateFields.get(j).getText());
									list.add(valueCell);
									list.add(key);
									list.add(oldPrimaryKey);
									indexFile.put(attrList.get(j).getAttrName(), list);
									done = true;
								}
							}
							if(!valueCell.equals(updateFields.get(j).getText())){
								changeValue= true;
								if(!done){
									if(KeyValueStoring.fileExists(new File(
											Config.KEY_VALUE_PATH_IF + databaseName + "/" + tableName + 
												"/" + attrList.get(j).getAttrName()))){
										
										List<String> list = new ArrayList<String>();
										list.add(updateFields.get(j).getText());
										list.add(valueCell);
										list.add(key);
										list.add(oldPrimaryKey);
										indexFile.put(attrList.get(j).getAttrName(), list);
									}
								}
							}
							
							valuesList.add(updateFields.get(j).getText());
						}	

					}
					
					for(int i = 0; i < fkList.size(); i++){
						String valueCell;
						if(table.getValueAt(table.getSelectedRow(), i) == null){
							valueCell = "";
						}else{
							valueCell = table.getValueAt(table.getSelectedRow(), i).toString();
						}
						
						JComboBox<String> value = updateComboBoxs.get(i);
						if(!valueCell.equals(value.getSelectedItem())){
							foreign = true;
						}
						foreignList.add(value.getSelectedItem().toString());
					}
					
					if(primary){
						try{
							delete();
							saveUniqueKey(uniqueList);
							savePrimaryKey(key, valuesList);
							saveForeignKeyValue(foreignList, key);
							saveForeignKey(key);
							KeyValueStoring kvs = new KeyValueStoring();
							for(Entry<String, List<String>> entry : indexFile.entrySet()){
								kvs.setPath(Config.KEY_VALUE_PATH_IF + databaseName + "/" +
												tableName, entry.getKey());
								
								kvs.updateIndexFile(entry.getValue().get(0), 
												entry.getValue().get(1), 
												entry.getValue().get(2),
												entry.getValue().get(3));
							}
						}catch(DatabaseException exp){
							JOptionPane.showMessageDialog(updateWindow, "Update failed!" + exp.getMessage());
						}finally{
							updateWindow.setVisible(false);
						}
						relode();
						return;
					}
					if(unique){
						try{
							KeyValueStoring kvs = new KeyValueStoring();
							for(Entry<String, List<String>> entry : uniqueList.entrySet()){
								if(!entry.getValue().get(0).isEmpty()){
									kvs.setPath(Config.KEY_VALUE_PATH_UQ + databaseName + "/" + tableName, 
											entry.getKey());
									try{
										kvs.delete(entry.getValue().get(2));
									}catch(DatabaseException exp){
										System.out.println("UNIQUE DELETE: " + exp.getMessage());
									}
									
								}
							}
							saveUniqueKey(uniqueList);
							kvs.setPath(Config.KEY_VALUE_PATH_PK + databaseName, tableName);
							kvs.updateValue(key, valuesList.toString());
						}catch(DatabaseException exp){
							JOptionPane.showMessageDialog(updateWindow, "Update failed!" + exp.getMessage());
						}finally{
							updateWindow.setVisible(false);
						}
					}
					if(foreign){
						KeyValueStoring kvs = new KeyValueStoring();
						for(int f = 0; f < fkList.size(); f++){
							try{
								kvs.setPath(Config.KEY_VALUE_PATH_FK + fkList.get(f).getDatabaseName() +
										"_" + fkList.get(f).getTableName(), fkList.get(f).getReferens().getTableName());
								kvs.deletePrimaryFromForeign(key);
								kvs.setPath(Config.KEY_VALUE_PATH_FK + fkList.get(f).getDatabaseName() +
										"_" + fkList.get(f).getTableName(), fkList.get(f).getReferens().getTableName());
								kvs.insertPrimaryInForeign(foreignList.get(f),key);
							}catch(DatabaseException exp){
								JOptionPane.showMessageDialog(updateWindow, "Update failed!" + exp.getMessage());
							}finally{
								updateWindow.setVisible(false);
							}
						}
					}	
					
					if(changeValue){
						KeyValueStoring kvs = new KeyValueStoring();
						for(Entry<String, List<String>> entry : indexFile.entrySet()){
							kvs.setPath(Config.KEY_VALUE_PATH_IF + databaseName + "/" +
											tableName, entry.getKey());
							try{
								kvs.updateIndexFile(entry.getValue().get(0), 
												entry.getValue().get(1), 
												entry.getValue().get(2),
												entry.getValue().get(3));
							}catch (DatabaseException e2) {
								System.out.println("UPDTA INDEX: " + e2.getMessage());
							}
						}
						try{
							kvs.setPath(Config.KEY_VALUE_PATH_PK + databaseName, tableName);
							kvs.updateValue(key, valuesList.toString());
						}catch(DatabaseException exp){
							JOptionPane.showMessageDialog(updateWindow, "Update failed!" + exp.getMessage());
						}finally{
							updateWindow.setVisible(false);
						}
					}
					relode();
					
				}
			});
		
    	}else{
			JOptionPane.showMessageDialog(panel, "Please select a row!");
		}
    }
    //update utan ujra toleteni atabla ertekeit
    private void relode(){
		try{		
			KeyValueStoring kvs = new KeyValueStoring();
			kvs.setPath(Config.KEY_VALUE_PATH_PK + databaseName, tableName);
			panel.setTable(kvs.getAll());
		}catch(DatabaseException exp){
			JOptionPane.showMessageDialog(panel, "MI A FASZ");
		}
    }
}