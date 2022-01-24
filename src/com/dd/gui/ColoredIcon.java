package com.dd.gui;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ColoredIcon implements Icon
{
    private Icon delegate;

    public ColoredIcon(Icon delegate) {
        this(delegate, UIManager.getColor("textHighlight"), 0.5F);
    }

    public ColoredIcon(Icon delegate, Color color) {
        this(delegate, color, 0.5F);
    }

    public ColoredIcon(Icon delegate, Color color, float alpha) {
        this.delegate = delegate;
        createMask(color, alpha);
    }

    private BufferedImage mask;

    private void createMask(Color color, float alpha) {
        mask = new BufferedImage(delegate.getIconWidth(), delegate.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gbi = (Graphics2D) mask.getGraphics();
        delegate.paintIcon(new JLabel(), gbi, 0, 0);
        gbi.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, alpha));
        gbi.setColor(color);
        gbi.fillRect(0, 0, mask.getWidth() - 1, mask.getHeight() - 1);
    }

    private boolean colorPainted = true;

    public boolean isColorPainted() {
        return colorPainted;
    }


    public Icon getDelegate() {
        return delegate;
    }

    public void setColorPainted(boolean colorPainted) {
        this.colorPainted = colorPainted;
    }


    public void paintIcon(Component c, Graphics g, int x, int y) {
        delegate.paintIcon(c, g, x, y);
        if (colorPainted)
            g.drawImage(mask, x, y, c);
    }

    public int getIconWidth() {
        return delegate.getIconWidth();
    }

    public int getIconHeight() {
        return delegate.getIconHeight();
    }
}



