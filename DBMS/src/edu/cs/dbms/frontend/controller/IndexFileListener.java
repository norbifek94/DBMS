package edu.cs.dbms.frontend.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.cs.dbms.backend.key_value.KeyValueStoring;
import edu.cs.dbms.backend.model.Attribute;
import edu.cs.dbms.backend.model.IndexFile;
import edu.cs.dbms.backend.service.AttributeService;
import edu.cs.dbms.backend.service.DatabaseException;
import edu.cs.dbms.backend.service.IndexFileService;
import edu.cs.dbms.backend.util.Config;
import edu.cs.dbms.frontend.gui.IndexFilePanel;
import edu.cs.dbms.frontend.gui.TreePopup;
import edu.cs.dbms.frontend.gui.TreeViewPanel;
import edu.cs.dbms.frontend.util.ConfigFront;

public class IndexFileListener implements ActionListener{

	private TreeViewPanel treeViewPanel;
	private IndexFilePanel indexFilePanel;
	private IndexFileService indexService;
	private TreePopup treePopup;

	public IndexFileListener(TreeViewPanel treeViewPanel, IndexFilePanel indexFilePanel,TreePopup treePopup) {
		this.treePopup = treePopup;
		this.treeViewPanel = treeViewPanel;
		this.indexFilePanel = indexFilePanel;
		indexService = new IndexFileService();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(ConfigFront.INDEX_FILE_SAVE_BUTTON)){
			
			try{				
				IndexFile file = treePopup.getIndexFile(); 
				file.setKeyLength(indexFilePanel.getLength());
				file.setUnique(indexFilePanel.isUnique() ? "1":"0");
				file.setType(indexFilePanel.getType());
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePopup.getNode().getParent();
				indexService.creatIndexFile(file);
				treeViewPanel.loadTableContent(node);
				
				KeyValueStoring kvs = new KeyValueStoring();
				kvs.setPath(Config.KEY_VALUE_PATH_PK + 
						file.getDatabaseName(), file.getTableName());
				Map<String, List<String>> map = kvs.getAll();
				AttributeService attrService = new AttributeService();
				Attribute attr = new Attribute.AttributeBuilder()
											  .setDatabaseName(file.getDatabaseName())
											  .setTableName(file.getTableName())
											  .creatAttr();
				List<String> attrList = attrService.getAttributeNames(attr);
				int i = 0;
				for(i = 0; i < attrList.size(); i++){
					if(attrList.get(i).equals(file.getIndexName())){
						for(Map.Entry<String, List<String>> entry : map.entrySet()) {
							//ha mar vannak datok es utolag csinal index file-t egy attributumra
							if(!entry.getValue().get(i-1).isEmpty()){
								kvs.setPath(Config.KEY_VALUE_PATH_IF + 
										file.getDatabaseName() + "/" + 
										file.getTableName(), file.getIndexName());
								kvs.insertIndexFile(entry.getValue().get(i-1), entry.getKey());
							}
						}
						//ha az attributum letrehozasa utan nem szur 
						//be erteket neki es letrehoz egy index file-t 
						//akkor letrehozom a mappat neki, h kessob tudjam, h 
						//van az attributumnak index file-ja
						kvs.setPath(Config.KEY_VALUE_PATH_IF + 
								file.getDatabaseName() + "/" + 
								file.getTableName(), file.getIndexName());
						kvs.close();
					}
				}
				
				
			}catch(DatabaseException exp){
				JOptionPane.showMessageDialog(indexFilePanel, exp);
			}finally{
				indexFilePanel.setVisible(false);
			}
		}
		
	}

}
