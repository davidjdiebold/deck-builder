package com.dd.gui.actions;


import com.dd.gui.CorruptedBuildException;
import com.dd.gui.RunGUI;

import javax.swing.AbstractAction;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;


/**
 * Created with IntelliJ IDEA.
 * User: ddiebold
 * Date: 1/4/14
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActionReleaseNote extends AbstractAction
{
    private static final String WHAT_S_NEW = "What's new ?";

    public ActionReleaseNote()
    {
        super(WHAT_S_NEW);
    }

    @Override
    public void actionPerformed(final ActionEvent e)
    {
        String releaseNote = "release-note.txt";
        try
        {
            URL url = RunGUI.class.getClassLoader().getResource("com/dd/gui/resources/"+ releaseNote);
            JEditorPane quotePane = new JEditorPane(url);
            quotePane.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(quotePane);

            JFrame frame = new JFrame();
            Dimension size = new Dimension(640,480);
            frame.setPreferredSize(size);
            frame.setSize(size);
            frame.setMinimumSize(size);
            frame.setMaximumSize(size);

            frame.setTitle(WHAT_S_NEW);
            frame.add(scrollPane);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
        catch (IOException e1)
        {
            throw new CorruptedBuildException(releaseNote);
        }
    }
}
