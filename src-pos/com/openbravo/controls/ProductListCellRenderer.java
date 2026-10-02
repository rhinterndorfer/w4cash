package com.openbravo.controls;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ProductListCellRenderer extends JLabel implements ListCellRenderer<Object[]> {
	
	public ProductListCellRenderer() {
	}
	
	@Override
    public Component getListCellRendererComponent(JList<? extends Object[]> list, Object[] product, int index,
        boolean isSelected, boolean cellHasFocus) {
          
		setFont(list.getFont());
		if(isSelected) {
			setBackground(Color.lightGray);
			setOpaque(true);
		}else {
			setBackground(null);
		}
        setText(String.format("%s [%s]", product[2], product[1]));
         
        return this;
    }
     
}