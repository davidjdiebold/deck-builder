package com.dd.gui;


import com.dd.model.Manacurve;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;


public class ManacurveRenderer implements TableCellRenderer
{
    protected final String[] axis = new String[]{"0","1","2","3","4","5","6","+"};

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column)
    {
        return new Renderer((Manacurve)value, isSelected, table, row);
    }

    class Renderer extends JPanel
    {
        private final Manacurve _curve;
        private final boolean _selected;
        private final JTable _table;
        private final int _row;

        Renderer(final Manacurve curve, final boolean selected, JTable table, int row)
        {
            _row = row;
            _curve = curve;
            _selected = selected;
            _table = table;
        }

        @Override
        public void paint(final Graphics g)
        {
            Component comp = _table.getDefaultRenderer(Double.class).getTableCellRendererComponent(_table, 0., _selected, false, _row, 1);
            Color backColor = comp.getBackground();
            Color writeColor = _selected ? Color.WHITE : Color.BLACK;

            int heightMargin = 15;
            int widthMargin  =  5;

            double mode = -1;
            for(int i = 0 ; i < _curve.getMaxCost() ; i++)
            {
                mode = Math.max(mode, _curve.getCount(i));
            }

            Font font = new JLabel().getFont();
            font = new Font(font.getName(), font.getStyle(), font.getSize()+2);

            Graphics2D g2d = (Graphics2D)g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            FontRenderContext renderer = g2d.getFontRenderContext();

            int width = Math.min((getWidth()-2*widthMargin) / 8, 20);
            int height= getHeight() - 2*heightMargin;

            g2d.setPaint(backColor);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            for(int i = 0 ; i < 8 ; i++)
            {
                g2d.setPaint(Color.BLUE);
                int thisHeight = (int) (height * _curve.getCount(i) / mode);
                int delta = height - thisHeight;
                g2d.fillRect(widthMargin+i*width, heightMargin+delta, width-widthMargin, thisHeight);

                int deltaX = widthMargin  + i * width + width / 2 - 7;
                int deltaY = heightMargin + height + 13;

                g2d.translate(deltaX, deltaY);
                g2d.setPaint(writeColor);
                GlyphVector gv = font.createGlyphVector(renderer, axis[i]);
                g2d.fill(gv.getGlyphOutline(0));
                g2d.translate(-deltaX, -deltaY);

                if(_curve.getCount(i)>0)
                {
                    deltaY = heightMargin + delta-3;
                    g2d.translate(deltaX, deltaY);
                    g2d.setPaint(writeColor);
                    gv = font.createGlyphVector(renderer, ""+((int)_curve.getCount(i)));
                    g2d.fill(gv.getOutline());
                    g2d.translate(-deltaX, -deltaY);
                }
            }
        }
    }


}
