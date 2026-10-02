// Original source from: https://www.logicbig.com/tutorials/java-swing/combo-box-filter.html

package com.openbravo.controls;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

public class ProductComboRenderer extends DefaultListCellRenderer {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3881999361102206233L;
	public static final Color background = new Color(225, 240, 255);
    private static final Color defaultBackground = (Color) UIManager.get("List.background");
    private static final Color defaultForeground = (Color) UIManager.get("List.foreground");
    private Supplier<String> highlightTextSupplier;

    public ProductComboRenderer(Supplier<String> highlightTextSupplier) {
        this.highlightTextSupplier = highlightTextSupplier;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        Object[] product = (Object[]) value;
        if (product == null) {
            return this;
        }
        String text = getEmployeeDisplayText(product);
        text = HtmlHighlighter.highlightText(text, highlightTextSupplier.get());
        this.setText(text);
        if (!isSelected) {
            this.setBackground(index % 2 == 0 ? background : defaultBackground);
        }
        this.setForeground(defaultForeground);
        return this;
    }

    public static String getEmployeeDisplayText(Object[] product) {
        if (product == null) {
            return "";
        }
        // product[2] => Name
        // product[1] => Code
        // product[0] => Id
        return String.format("%s [%s]", product[2], product[1]);
    }
    
    public static boolean productFilter(Object[] product, String textToFilter) {
        if (textToFilter.isEmpty()) {
            return true;
        }
        return getEmployeeDisplayText(product).toLowerCase()
                                  .contains(textToFilter.toLowerCase());
    }
}