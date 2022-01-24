package com.dd.gui;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.BitSet;


public class ComponentFactory
{

    public static final int COMBO_HEIGHT = 40;

    public static void adjust(JComboBox combobox)
    {
        setSize(combobox, new Dimension(100, COMBO_HEIGHT));
    }

    public static void setSize(JComponent component, Dimension size)
    {
        component.setPreferredSize(size);
        component.setMaximumSize(size);
        component.setMinimumSize(size);
        component.setSize(size);
    }

    public static ImageButton createButton(Color color, String iconName)
    {
        return createButton(color, iconName, COMBO_HEIGHT);
    }

    public static ImageButton createButton(Color color, String iconName, int size)
    {
        return createButton("", color, iconName, size);
    }

    public static JButton createButton(String name, String iconName, int size)
    {
        ImageIcon icon = new ImageIcon(ImageLoader.loadBufferedImage(iconName).getScaledInstance(size, size, -1));
        JButton ret = new JButton(name, icon);
        return ret;
    }

    public static ImageButton createButton(String name, Color color, String iconName, int size)
    {
        ImageButton res = new ImageButton(color);
        Dimension dim = new Dimension(size, size);
        res.setPreferredSize(dim);
        res.setMaximumSize(dim);
        res.setMinimumSize(dim);
        res.setSize(dim);
        ImageIcon icon = new ImageIcon(ImageLoader.loadBufferedImage(iconName).getScaledInstance(size, size, 0));
        res.setIcon(icon);
        res.setOpaque(false);
        res.setText(name);
        return res;
    }

    public static Image getOverlayedImage(BufferedImage img)
    {
        Color grey = Color.LIGHT_GRAY;
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        Image a = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage ret = new BufferedImage(width, height, Transparency.TRANSLUCENT);
        ret.getGraphics().drawImage(a, 0, 0 , null);
        for(int i = 0 ; i < width ; i++)
        {
            for(int j = 0 ; j < height ; j++)
            {
                int rvb = ret.getRGB(i, j);
                Color oldColor = new Color(rvb);
                int red = (oldColor.getRed() + grey.getRed()) / 2;
                int green = (oldColor.getGreen() + grey.getGreen()) / 2;
                int blue = (oldColor.getBlue() + grey.getBlue()) / 2;

                int alpha = (rvb>>24) & 0xff;
                Color newColor = new Color(red, green, blue, alpha);//, oldColor.getTransparency());
                ret.setRGB(i, j, newColor.getRGB());
            }
        }
        return ret;
    }

    public static Color getGreyedColor(Color color)
    {
        int red = (color.getRed()+Color.LIGHT_GRAY.getRed()) / 2;
        int green= (color.getGreen()+Color.LIGHT_GRAY.getGreen()) / 2;
        int blue= (color.getBlue()+Color.LIGHT_GRAY.getBlue()) / 2;
        return new Color(red,green,blue);
    }

    public static Color getHightlightedColor(Color color, double offset)
    {
        double  mult = 1. + offset;
        int red = (int) (Math.min(color.getRed() * mult, 255));
        int green= (int) (Math.min(color.getGreen() * mult, 255));
        int blue = (int) (Math.min(color.getBlue() * mult, 255));

        return new Color(red,green,blue);
    }

    public static JLabel createLabel(String string)
    {
        JLabel label = new JLabel(string);
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, label.getFont().getSize()));
        return label;
    }

    public static String truncate(final String name)
    {
        int maxLength = 15;
        if(name.length() > maxLength)
        {
            return name.substring(0, maxLength-3) + "...";
        }
        else
        {
            return name;
        }
    }

    public static void nullifyMargin(JButton button)
    {
        //UIDefaults def = new UIDefaults();
        //def.put("Button.contentMargins", new Insets(0,0,0,0));
        //button.putClientProperty("Nimbus.Overrides", def);
    }
}
