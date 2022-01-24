package com.dd.gui;


/**
 * Created with IntelliJ IDEA.
 * User: ddiebold
 * Date: 1/3/14
 * Time: 9:18 PM
 * To change this template use File | Settings | File Templates.
 */

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.event.EventListenerList;
import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GlobalHotkeyManager extends EventQueue
{

    private static final GlobalHotkeyManager instance = new GlobalHotkeyManager();

    private final InputMap keyStrokes = new InputMap();
    private final ActionMap actions = new ActionMap();
    private final EventListenerList _keyListeners = new EventListenerList();

    private boolean CTRL_DOWN = false;
    private boolean ALT_DOWN = false;
    private boolean ALTGR_DOWN = false;
    private boolean SHIFT_DOWN = false;

    private GlobalHotkeyManager()
    {
    } // One is enough - singleton

    public static GlobalHotkeyManager getSingleton()
    {
        return instance;
    }



    public void addKeyListener(KeyListener keyListener)
    {
        _keyListeners.add(KeyListener.class, keyListener);
    }


    public void removeKeyListener(KeyListener keyListener)
    {
        _keyListeners.remove(KeyListener.class, keyListener);
    }


    public InputMap getInputMap()
    {
        return keyStrokes;
    }


    public ActionMap getActionMap()
    {
        return actions;
    }


    public boolean isControlDown()
    {
        return CTRL_DOWN;
    }


    public boolean isShiftDown()
    {
        return SHIFT_DOWN;
    }


    public boolean isAltDown()
    {
        return ALT_DOWN;
    }


    public boolean isAltGraphDown()
    {
        return ALTGR_DOWN;
    }

    @Override
    protected void dispatchEvent(AWTEvent event)
    {
        if (event instanceof KeyEvent)
        {
            KeyEvent keyEvent = (KeyEvent) event;
            CTRL_DOWN = keyEvent.isControlDown();
            ALT_DOWN = keyEvent.isAltDown();
            ALTGR_DOWN = keyEvent.isAltGraphDown();
            SHIFT_DOWN = keyEvent.isShiftDown();

            // dispatch keyEvents to keyListeners
            for (KeyListener keyListener : _keyListeners.getListeners(KeyListener.class))
            {
                if (keyEvent.getID() == KeyEvent.KEY_PRESSED)
                {
                    keyListener.keyPressed(keyEvent);
                }
                else if (keyEvent.getID() == KeyEvent.KEY_RELEASED)
                {
                    keyListener.keyReleased(keyEvent);
                }
                else if (keyEvent.getID() == KeyEvent.KEY_TYPED)
                {
                    keyListener.keyTyped(keyEvent);
                }

            }

            // KeyStroke.getKeyStrokeForEvent converts an ordinary KeyEvent
            // to a keystroke, as stored in the InputMap.  Keep in mind that
            // NumPad keystrokes are different to ordinary keys, i.e. if you
            // are listening to
            KeyStroke ks = KeyStroke.getKeyStrokeForEvent(keyEvent);


            String actionKey = (String) keyStrokes.get(ks);
            if (actionKey != null)
            {


                Action action = actions.get(actionKey);

                if (action != null && action.isEnabled())
                {
                    // I'm not sure about the parameters
                    action.actionPerformed(
                            new ActionEvent(event.getSource(), event.getID(),
                                    actionKey, (keyEvent).getModifiers()));
                    return; // consume event
                }
            }
        }

        super.dispatchEvent(event);
    }
}


