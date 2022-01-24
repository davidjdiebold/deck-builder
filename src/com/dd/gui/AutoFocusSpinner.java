package com.dd.gui;


import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AutoFocusSpinner
{
    public static final SelectOnFocusGainedHandler SHARED_INSTANCE = new SelectOnFocusGainedHandler();

    public static void installFocusListener(JSpinner spinner) {

        JComponent spinnerEditor = spinner.getEditor();

        if (spinnerEditor != null) {

            // This is me spending a few days trying to make this work and
            // eventually throwing a hissy fit and just grabbing all the
            // JTextComponent components contained within the editor....
            List<JTextComponent> lstChildren = findAllChildren(spinner, JTextComponent.class);
            if (lstChildren != null && lstChildren.size() > 0) {

                JTextComponent editor = lstChildren.get(0);
                editor.addFocusListener(SHARED_INSTANCE);

            }
        }
    }

    public static <T extends Component> List<T> findAllChildren(JComponent component, Class<T> clazz) {

        List<T> lstChildren = new ArrayList<T>(5);
        for (Component comp : component.getComponents()) {

            if (clazz.isInstance(comp)) {

                lstChildren.add((T) comp);

            } else if (comp instanceof JComponent) {

                lstChildren.addAll(findAllChildren((JComponent) comp, clazz));

            }

        }

        return Collections.unmodifiableList(lstChildren);

    }

    public static class SelectOnFocusGainedHandler extends FocusAdapter
    {

        @Override
        public void focusGained(FocusEvent e) {

            Component comp = e.getComponent();
            if (comp instanceof JTextComponent) {
                final JTextComponent textComponent = (JTextComponent) comp;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(25);
                        } catch (InterruptedException ex) {
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                textComponent.selectAll();
                            }
                        });
                    }
                }).start();
            }
        }
    }
}
