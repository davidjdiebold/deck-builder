package com.dd.gui;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class ImageLoader
{
    public static BufferedImage loadBufferedImage(String name)
    {
        BufferedImage ret = null;
        String path = "gfx/" + name;
        try
        {
            ret = ImageIO.read(
                    ImageLoader.class.getClassLoader().getResource(path));
        }
        catch (Throwable e)
        {
            throw new CorruptedBuildException(path);
        }
        return ret;
    }
}
