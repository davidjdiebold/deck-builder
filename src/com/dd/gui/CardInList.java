package com.dd.gui;


import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class CardInList extends JPanel implements Hoverable
{
    public final static BufferedImage _mana;
    public final static Image _greyedMana;

    static
    {
        _mana = ImageLoader.loadBufferedImage("manabig2.png");
        _greyedMana = ComponentFactory.getOverlayedImage(_mana);
    }

    private final int _manaCost;
    private final String _name;
    private final int _amount;
    private final boolean _readonly;

    private double _highLight = 0;

    public CardInList(final String name, final int manaCost, final int amount, final boolean readonly)
    {

        _name = name;
        _manaCost = manaCost;
        _amount = amount;
        _readonly = readonly;

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(final MouseEvent e)
            {
                new HighlightTicker(CardInList.this).start();
            }
        });
    }

    @Override
    public void paint(final Graphics g)
    {
        super.paint(g);
        Graphics2D pen = (Graphics2D)g;
        pen.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        pen.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int radius = 10;
        pen.setPaint(_readonly ? ComponentFactory.getGreyedColor(Palette.F_VIOLET) :
                ComponentFactory.getHightlightedColor(Palette.F_VIOLET, _highLight));
        pen.fillRoundRect(getHeight() / 2, 0, getWidth() - getHeight(), getHeight() - 1, radius, radius);
        pen.setPaint(Color.BLACK);
        pen.drawRoundRect(getHeight()/2, 0, getWidth()-getHeight(), getHeight()-1, radius, radius);
        pen.drawImage(_readonly ? _greyedMana : _mana, 0, 0, getHeight(), getHeight(), null);

        int deltaX = getHeight() / 2 -5;
        int deltaY = getHeight() / 2 +6;
        pen.translate(deltaX, deltaY);
        pen.setPaint(Color.WHITE);
        Font font = new JLabel().getFont();
        Font f2 = new Font(font.getName(), Font.BOLD, font.getSize()+5);
        GlyphVector gv = f2.createGlyphVector(pen.getFontRenderContext(), "" + _manaCost);
        pen.fill(gv.getGlyphOutline(0));
        pen.translate(-deltaX, -deltaY);

        deltaX = getHeight() + 5;
        deltaY = getHeight() / 2 +6;
        pen.translate(deltaX, deltaY);
        pen.setPaint(Color.BLACK);
        gv = f2.createGlyphVector(pen.getFontRenderContext(), ComponentFactory.truncate(_name));
        pen.fill(gv.getOutline());
        pen.translate(-deltaX, -deltaY);

        deltaX = getWidth() - getHeight() / 2 -25;
        deltaY = getHeight() / 2 +6;
        pen.translate(deltaX, deltaY);
        pen.setPaint(Color.BLACK);
        gv = f2.createGlyphVector(pen.getFontRenderContext(), "x" + _amount);
        pen.fill(gv.getOutline());
        pen.translate(-deltaX, -deltaY);
    }

    @Override
    public void setHighLight(final double highlight)
    {
        _highLight = highlight;
        repaint();
    }
}
