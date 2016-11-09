package edu.cs.dbms.frontend.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.cs.dbms.backend.model.Attribute;
import edu.cs.dbms.backend.service.AttributeService;
import edu.cs.dbms.backend.service.DatabaseException;
import edu.cs.dbms.frontend.gui.AttributePanel;
import edu.cs.dbms.frontend.gui.TreePopup;
import edu.cs.dbms.frontend.gui.TreeViewPanel;
import edu.cs.dbms.frontend.util.ConfigFront;

public class AttributeListener implements ActionListener{

	private TreeViewPanel treeViewPanel;
	private AttributePanel attributePanel;
	private AttributeService attributeService;
	private TreePopup treePopup;

	public AttributeListener(TreeViewPanel treeViewPanel, AttributePanel attributePanel, TreePopup treePopup) {
		
		this.treePopup = treePopup;
		this.treeViewPanel = treeViewPanel;
		this.attributePanel = attributePanel;
		attributeService = new AttributeService();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals(ConfigFront.ATTRIBUTE_SAVE_BUTTON)){
			if(!attributePanel.getAttributeName().isEmpty() &&
					!attributePanel.getType().isEmpty() &&
						!attributePanel.getLength().isEmpty()){
				
				try{				
					Attribute attr = treePopup.getAttribute();
					attr.setAttrName(attributePanel.getAttributeName());
					attr.setType(attributePanel.getType());
					attr.setLength(attributePanel.getLength());
					attr.setPrimary(attributePanel.isPrimaryKey());
					attr.setUnique(attributePanel.isUniqueKey());
					attr.setIsNull(attributePanel.isNull()? "1" : "0");
					
					attributeService.createAttribute(attr);
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePopup.getNode().getParent();
					treeViewPanel.loadTableContent(node);
				
				}catch(DatabaseException exp){
					JOptionPane.showMessageDialog(attributePanel, exp);
				}finally{
					attributePanel.setVisible(false);
				}
			}
		}
	}

}
