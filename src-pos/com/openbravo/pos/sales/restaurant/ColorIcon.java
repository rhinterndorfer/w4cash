package com.openbravo.pos.sales.restaurant;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.Icon;

//

public class ColorIcon implements Icon
{
  private int iWidth;
  private int iHeight;

  private Color  color;

  //---------------------------------------------------------------------------

  public ColorIcon()
  {
    this(32, 16);
  }

  //---------------------------------------------------------------------------

  public ColorIcon(int width, int height)
  {
    this(width, height, Color.black);
  }

  //---------------------------------------------------------------------------

  public ColorIcon(int width, int height, Color c)
  {
    iWidth  = width;
    iHeight = height;

    color   = c;
  }

  //---------------------------------------------------------------------------

  public void setColor(Color c)
  {
    color = c;
  }

  //---------------------------------------------------------------------------

  public Color getColor()
  {
    return color;
  }

  //---------------------------------------------------------------------------

  
  //---------------------------------------------------------------------------
  //---
  //--- Icon interface methods
  //---
  //---------------------------------------------------------------------------

  public int getIconWidth()
  {
    return iWidth;
  }

  //---------------------------------------------------------------------------

  public int getIconHeight()
  {
    return iHeight;
  }

  //---------------------------------------------------------------------------

  public void paintIcon(Component c, Graphics g, int x, int y)
  {
    g.setColor(color);
    g.drawRect(x,y,iWidth, iHeight);
    g.fillRect(x,y,iWidth, iHeight);
  }
}
