package com.dd.gui;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

/**
 * A simple customizable icon button.
 */
public class ImageButton extends JButton implements MouseListener
{
    boolean entered = false;
    boolean clicked = false;



    public void setDrawText(boolean drawText) {
        this.drawText = drawText;
    }

    boolean drawText = false;
    private ColoredIcon _colorIcon = null;
    Color overlay = Color.BLUE;

    public ImageButton() {
        super();
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.TOP);
        addMouseListener(this);
        setForeground(Color.WHITE);
        this.overlay = Color.WHITE;
//        setBackground(overlay);
        setFont(getFont().deriveFont(Font.PLAIN,9));

    }

    public ImageButton(final ImageIcon imageIcon)
    {
        this();
        setIcon(imageIcon);
    }

    @Override
    public void setIcon(Icon defaultIcon) {
        super.setIcon(defaultIcon);
        _colorIcon = null;
    }

    public ImageButton(Color overlay) {
        this();
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.TOP);
        this.overlay = overlay;
      //  setBackground(overlay);
        setFont(getFont().deriveFont(Font.PLAIN,9));

    }

    @Override
    public void paintAll(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        int offset = 0;

        if (isOpaque()) {
            if (entered) {
                g.setColor(getForeground());
                offset = 0;
            } else {
                g.setColor(getBackground());
            }
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        double factor = drawText ? 2 / 6.0 : 1/2.0;
        if (getIcon() != null) {
            if (!entered) {
                getIcon().paintIcon(this, g,
                        (clicked ? 1 : offset) + getWidth() / 2 -
                                getIcon().getIconWidth() / 2,
                        (int)((clicked ? 1 : offset) + (getHeight()*factor) -
                                getIcon().getIconHeight() / 2));
            } else {
                if (_colorIcon == null) {
                    _colorIcon = new ColoredIcon(getIcon(), overlay);
                }
                _colorIcon.paintIcon(this, g,
                        (clicked ? 1 : offset) + getWidth() / 2 -
                                getIcon().getIconWidth() / 2,
                        (int)((clicked ? 1 : offset) + (getHeight()*factor) -
                                getIcon().getIconHeight() / 2));
            }

        }
        if ( drawText) {

            Rectangle2D r = g.getFontMetrics().getStringBounds(getText(),g);
//            g.setColor(new Color(96,96,96,128));
//            g.fillRect(0,0,getWidth(),getHeight());
            g.setColor(Color.DARK_GRAY);
            g.drawString(getText(), (int) (getWidth() / 2 - r.getWidth() / 2) ,(int) (getHeight() * 5 / 6 - r.getHeight()/2));
//            g.setColor(Color.DARK_GRAY);
//            g.drawString(getText(), (int) (getWidth() / 2 - r.getWidth() / 2)+2 ,(int) (getHeight() * 5 / 6 - r.getHeight()/2));
            g.setColor(entered ? overlay : getForeground());
            g.drawString(getText(), (int) (getWidth() / 2 - r.getWidth() / 2)+1 , (int) (getHeight() * 5 / 6 - r.getHeight()/2)+1);
        }

    }

    public void mouseEntered(MouseEvent e) {
        entered = true;
    }

    public void mousePressed(MouseEvent e) {
        clicked = true;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
        entered = false;
    }

    public void mouseReleased(MouseEvent e) {
        clicked = false;
    }
}

