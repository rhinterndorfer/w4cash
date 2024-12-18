// Original source from: https://www.logicbig.com/tutorials/java-swing/combo-box-filter.html

package com.openbravo.controls;

import java.util.regex.Pattern;

public class HtmlHighlighter {
    private static final String HighLightTemplate = "<span style='background:yellow;'>$1</span>";

    public static String highlightText(String text, String textToHighlight) {
        if(textToHighlight.length()==0){
            return text;
        }

        try {
            text = text.replaceAll("(?i)(" + Pattern.quote(textToHighlight) + ")", HighLightTemplate);
        } catch (Exception e) {
            return text;
        }
        return "<html>" + text + "</html>";
    }
}