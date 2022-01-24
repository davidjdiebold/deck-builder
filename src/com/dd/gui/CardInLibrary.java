package com.dd.gui;


import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.GlyphVector;


public class CardInLibrary extends JButton implements Hoverable
{
    private final int _cost;

    private double _highlight;

    public CardInLibrary(final int cost)
    {
        _cost = cost;
        setBorder(null);
        setFocusPainted(false);
        setContentAreaFilled(false);

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(final MouseEvent e)
            {
                new HighlightTicker(CardInLibrary.this).start();
            }
        });
    }

    @Override
    public void paint(final Graphics g)
    {

        Graphics2D pen = (Graphics2D) g;
        pen.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        pen.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int marginLeft = 8;
        int margin = 5;
        int radius = 10;
        int imageSize = 32;
        pen.setPaint(ComponentFactory.getHightlightedColor(Palette.F_VIOLET, _highlight));
        pen.fillRoundRect(marginLeft, margin, getWidth()-2*marginLeft, getHeight()-2*margin, radius, radius);
        pen.setPaint(Color.BLACK);
        pen.drawRoundRect(marginLeft, margin, getWidth()-2*marginLeft, getHeight()-2*margin, radius, radius);
        pen.drawImage(CardInList._mana, 0, 0, imageSize, imageSize, null);

        Font font = new JLabel().getFont();
        Font f2 = new Font(font.getName(), Font.BOLD, font.getSize() + 5);
        GlyphVector gv = f2.createGlyphVector(pen.getFontRenderContext(), "" + _cost);
        int deltaX = imageSize / 2 - 5 * gv.getNumGlyphs();
        int deltaY = imageSize / 2 + 6;
        pen.translate(deltaX, deltaY);
        pen.setPaint(Color.WHITE);
        pen.fill(gv.getOutline());
        pen.translate(-deltaX, -deltaY);

        super.paint(g);
    }

    @Override
    public void setHighLight(final double highlight)
    {
        _highlight = highlight;
        repaint();
    }
}
