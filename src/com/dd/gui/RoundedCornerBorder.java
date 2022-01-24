package com.dd.gui;


import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;


/**
 * Created with IntelliJ IDEA.
 * User: ddiebold
 * Date: 1/1/14
 * Time: 10:11 PM
 * To change this template use File | Settings | File Templates.
 */
class RoundedCornerBorder extends AbstractBorder
{
  @Override public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    Graphics2D g2 = (Graphics2D)g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    int r = (height-1) / 2;
    RoundRectangle2D round = new RoundRectangle2D.Float(x, y, width-1, height-1, r, r);
    Container parent = c.getParent();
    if(parent!=null) {
      g2.setColor(parent.getBackground());
      Area corner = new Area(new Rectangle2D.Float(x, y, width, height));
      corner.subtract(new Area(round));
      g2.fill(corner);
    }
    g2.setColor(Color.GRAY);
    g2.draw(round);
    g2.dispose();
  }
  @Override public Insets getBorderInsets(Component c) {
    return new Insets(2, 4, 2, 4);
  }
  @Override public Insets getBorderInsets(Component c, Insets insets) {
    insets.left = insets.right = 4;
    insets.top = insets.bottom = 2;
    return insets;
  }
}
