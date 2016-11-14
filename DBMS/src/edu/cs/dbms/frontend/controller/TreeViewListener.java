package edu.cs.dbms.frontend.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import edu.cs.dbms.backend.model.Attribute;
import edu.cs.dbms.backend.model.IndexFile;
import edu.cs.dbms.backend.model.Table;
import edu.cs.dbms.backend.service.AttributeService;
import edu.cs.dbms.frontend.gui.DbmsFrame;
import edu.cs.dbms.frontend.gui.TreePopup;
import edu.cs.dbms.frontend.gui.TreeViewPanel;
import edu.cs.dbms.frontend.util.ConfigFront;

public class TreeViewListener implements MouseListener{

	private TreeViewPanel treeViewPanel;
	private JTree treeView;
	private TreePopup popupMenu;
	private DbmsFrame frame;
	
	public TreeViewListener(TreeViewPanel treeViewPanel, JTree treeView, DbmsFrame frame){
		
		this.treeViewPanel = treeViewPanel;
		this.treeView = treeView;
		this.frame = frame;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		//az adott melysegig az elemek neve, beleertve a az aktualisat is
		TreePath selPath = treeView.getPathForLocation(e.getX(), e.getY());
		
		//double click
		if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e) && selPath != null) {
			
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
        //jobb click
		if(SwingUtilities.isRightMouseButton(e)){
			//az adott melysegig az elemek neve, beleertve a az aktualisat is
			TreePath selPath = treeView.getPathForLocation(e.getX(), e.getY());
			//megkapjuk az aktualis melyseget, ebbol tudjuk h mire katintott (1-tol indul)
			Object[] array = null;
			try{
	        	array = selPath.getPath();
	        }catch(NullPointerException exp){
	        	return;
	        }
			int depth = array.length;
	        Table table;
	        Attribute attribute;
	        DefaultMutableTreeNode node;
	        IndexFile indexFile;
	        AttributeService attributeService = new AttributeService();
	        
	        removePanels();
	        popupMenu = new TreePopup(frame, treeViewPanel);
	        
			if(selPath != null){
				switch (depth){
					case 1:
						popupMenu.addNewMenuItem(ConfigFront.NEW_DATABASE);
						popupMenu.show(e.getComponent(), e.getX(), e.getY());
						break;
					case 2:
						popupMenu.addNewMenuItem(ConfigFront.DROP_DATABASE);

						popupMenu.setDatabaseName(array[depth-1].toString());
						table = new Table.TableBuilder()
						   .setDatabaseName(array[depth-1].toString())
						   .creatTable();
						node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
						popupMenu.setTable(table);
						popupMenu.setNode(node);
						popupMenu.show(e.getComponent(), e.getX(), e.getY());
						break;
					case 3:
						popupMenu.addNewMenuItem(ConfigFront.NEW_TABLE);
						table = new Table.TableBuilder()
						   .setDatabaseName(array[depth-2].toString())
						   .creatTable();
						node = (DefaultMutableTreeNode) array[depth-2];
						popupMenu.setTable(table);
						popupMenu.setNode(node);
						popupMenu.show(e.getComponent(), e.getX(), e.getY());
						break;
					case 4:
						popupMenu.addNewMenuItem(ConfigFront.DROP_TABLE);
						node = (DefaultMutableTreeNode) array[depth-2];
						table = new Table.TableBuilder()
											   .setDatabaseName(array[depth-3].toString())
											   .setTableName(array[depth-1].toString())
											   .creatTable();
						popupMenu.setTable(table);
						popupMenu.setNode(node);
						popupMenu.show(e.getComponent(), e.getX(), e.getY());
						break;
					case 5:
						
						popupMenu.addNewMenuItem(ConfigFront.NEW_ATTRIBUTE);
						popupMenu.addNewMenuItem(ConfigFront.NEW_ENTRY);
						
						node = (DefaultMutableTreeNode) array[depth-1];
						attribute = new Attribute.AttributeBuilder()
												 .setDatabaseName(array[depth-4].toString())
												 .setTableName(array[depth-2].toString())
												 .creatAttr();
						popupMenu.setAttribute(attribute);
						popupMenu.setNode(node);
						popupMenu.show(e.getComponent(), e.getX(), e.getY());
						
						break;
					case 6:
						node = (DefaultMutableTreeNode) array[depth-2];
						indexFile = new IndexFile.IndexFileBuilder()
												.setIndexName(array[depth-1].toString())
												.setDatabaseName(array[depth-5].toString())
												.setTableName(array[depth-3].toString())
												.createIndexFile();
						attribute = new Attribute.AttributeBuilder()
											 .setDatabaseName(array[depth-5].toString())
											 .setTableName(array[depth-3].toString())
											 .setAttrName(array[depth-1].toString())
											 .creatAttr();
						table = new Table.TableBuilder()
										 .setDatabaseName(array[depth-5].toString())
										 .creatTable();
						popupMenu.setTable(table);
						popupMenu.setIndexFile(indexFile);
						popupMenu.setAttribute(attribute);
						popupMenu.setNode(node);
						
						if(array[depth-2].toString().equals(ConfigFront.NODE_COLUMNS)){
							popupMenu.addNewMenuItem(ConfigFront.DELETE_ATTRIBUTE);
							popupMenu.addNewMenuItem(ConfigFront.NEW_INDEX_FILE);
							popupMenu.addNewMenuItem(ConfigFront.ADD_TO_INDEX_FILE);
							if(attributeService.isPrimaryKey(attribute)){
								popupMenu.addNewMenuItem(ConfigFront.NEW_FOREIGN);
								popupMenu.addNewMenuItem(ConfigFront.ADD_TO_FOREIGN_KEY);
							}
							popupMenu.show(e.getComponent(), e.getX(), e.getY());
						}else if(array[depth-2].toString().equals(ConfigFront.NODE_INDEXE)){
							popupMenu.addNewMenuItem(ConfigFront.DELETE_INDEX_FILE);
							popupMenu.show(e.getComponent(), e.getX(), e.getY());
						}else if(array[depth-2].toString().equals(ConfigFront.NODE_FOREIGN_KEY)){
							popupMenu.setForeignNode((DefaultMutableTreeNode) array[depth-1]);
							popupMenu.addNewMenuItem(ConfigFront.DELETE_FOREIGN_KEY);
							popupMenu.show(e.getComponent(), e.getX(), e.getY());
						}
						
						break;
				}
			}
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	private void removePanels(){
		if(popupMenu!= null){
        	List<PopupMenuListener> list = popupMenu.getListenerList();
        	for(PopupMenuListener listener : list){
				listener.setVisiblePanel();
			}
        }
	}
	
}
