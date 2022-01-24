package com.dd.gui;

import com.dd.builder.Configuration;
import com.dd.model.CardImpl;
import com.dd.model.Manacurve;
import com.dd.model.Card;
import com.dd.usc.OptimizeCurveResults;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static java.awt.GridBagConstraints.*;


public class MainFrame
{
    public static final Insets BUTTON_GROUP_INSET = new Insets(1,0,20,0);
    public static final Insets FINE_INSETS = new Insets(1,1,1,1);
    public static final Insets MEDIUM_INSETS = new Insets(6,6,6,6);

    private JFrame _frame;

    private Data _data;

    private OptimizerView _optimizer;
    private LibraryView _library;
    private OptimizerResultsView _results;

    private JTextField _searchField;

    private View _currentView = new EmptyView();
    private IMenuBar _menuBar;
    private JPanel _mainPanel = new JPanel(new GridBagLayout());
    private View[] _views;
    private boolean _windowClosedSilenced = false;
    private JPanel _panel;

    public MainFrame()
    {
        Logs.log("ACTION : Opening Application");
        GlobalHotkeyManager singleton = GlobalHotkeyManager.getSingleton();
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(singleton);
        singleton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(final KeyEvent e)
            {
                processKey(e);
            }
        });
    }

    private void processKey(final KeyEvent e)
    {
        //HACK INDEXES
        if( e.getKeyCode() == KeyEvent.VK_TAB && ((e.getModifiers() & (KeyEvent.CTRL_MASK)) != 0) &&
            ((e.getModifiers() & (KeyEvent.SHIFT_MASK)) != 0))
        {
            int index = _menuBar.getCurrentView() - 1;
            index = index < 0 ? 2 : index;
            _menuBar.selectTab(index);
        }
        else if( e.getKeyCode() == KeyEvent.VK_TAB && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0))
        {
            int index = _menuBar.getCurrentView() + 1;
            index = index > 2 ? 0 : index;
            _menuBar.selectTab(index);
            int test = 0;
        }
        else if( e.getKeyCode() == KeyEvent.VK_F && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0))
        {
            _searchField.requestFocusInWindow();
        }
        else if (e.getKeyCode() == KeyEvent.VK_ENTER && !_searchField.hasFocus())
        {
            SwingUtilities.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    getCurrentView().enter();
                }
            });
        }
    }

    public void initialize()
    {

        _frame = new JFrame();
        resizeFullScreen();
        final InfiniteProgressPanel panel = new InfiniteProgressPanel("Loading...");
        _frame.add(panel);
        sizeAndDisplay();
        panel.start();
        _frame.revalidate();
        _frame.repaint();

        SwingWorker<Void,Void> worker = new SwingWorker<Void, Void>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                loadData();
                return null;
            }

            @Override
            protected void done()
            {
                panel.stop();
                buildRibbon();
                toggleFullScreen();
                _menuBar.selectTab(_data.getSettings().getSelectedTab());
            }
        };
        worker.execute();
    }

    private void loadData()
    {
        Configuration configuration = Configuration.MAGIC;
        File file = new File("../data/project");
        if(!file.exists())
        {
            _data = new Data(configuration);
        }
        else
        {
            Kryo kryo = prepareKryo();
            try
            {
                _data = kryo.readObject(new Input(new FileInputStream(file)), Data.class);
            }
            catch (Throwable e)
            {
                Logs.log(e);
                _data = new Data(configuration);
            }
        }
    }

    private void buildRibbon()
    {

        _optimizer = new OptimizerView(this, _data);
        _library = new LibraryView(_data, this);
        _results = new OptimizerResultsView(_data, this);
        _views = new View[]{_optimizer, _results, _library};

//        _menuBar = new MenuBar();
        _menuBar = new TabbedMenuBar(_frame, _data);
        _menuBar.registerTab("logoCog_black_96.png", "logoCog_black_96.png", new ShowView(_optimizer, "Curve Builder"));
        _menuBar.registerTab("scores.png", "scores.png", new ShowView(_results, "Constructed Curves"));
        _menuBar.registerTab("library_gray.png", "library_gray.png", new ShowView(_library, "Library"));
        _menuBar.registerTab("off.png", "off.png", new MenuBarAction()
        {
            @Override
            public void onSelection()
            {
                saveData();
                _frame.setVisible(false);
                _frame.dispose();
            }
        });

        _searchField = new JTextField();
        _searchField.setBorder(null);
        _searchField.setBackground(Color.WHITE);
        _searchField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(final KeyEvent e)
            {
                if(e.getKeyCode()==KeyEvent.VK_ENTER)
                {
                    search();
                }
                updateSearchFieldColor();
            }

            @Override
            public void keyReleased(final KeyEvent e)
            {
                if(e.getKeyCode()==KeyEvent.VK_ENTER)
                {
                    search();
                }
                updateSearchFieldColor();
            }

            @Override
            public void keyTyped(final KeyEvent e)
            {
                if(e.getKeyCode()==KeyEvent.VK_ENTER)
                {
                    search();
                }
                updateSearchFieldColor();
            }
        });
        _panel = new JPanel(new GridBagLayout());
        Dimension size = new Dimension(270,20);
        _panel.setSize(size);
        _panel.setMaximumSize(size);
        _panel.setMinimumSize(size);
        _panel.setPreferredSize(size);
        _panel.setBackground(Color.WHITE);
        _panel.add(_searchField, new GridBagConstraints(0, 0, 1, 1, 100, 1, CENTER, BOTH, FINE_INSETS, 0, 0));
        JLabel label = ComponentFactory.createLabel("");
        label.setIcon(new ImageIcon(ImageLoader.loadBufferedImage("zoom.png")));
        label.setOpaque(false);
        label.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(final MouseEvent e)
            {
                search();
            }
        });
        _panel.add(label, new GridBagConstraints(1, 0, 1, 1, 1, 1, CENTER, NONE, FINE_INSETS, 0, 0));
        _panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        _menuBar.registerComponent(_panel, false);

        JPanel buttons = new JPanel(new GridBagLayout());
        buttons.setBackground(Color.RED);
        JButton comp = new JButton(new ImageIcon(ImageLoader.loadBufferedImage("reduce.png")));
        comp.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                _frame.setExtendedState(JFrame.ICONIFIED);
            }
        });
        comp.setUI(new BasicButtonUI());
        buttons.add(comp, new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
        JButton comp1 = new JButton(new ImageIcon(ImageLoader.loadBufferedImage(("maximize.png"))));
        comp1.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                toggleWindowMode();
            }
        });
        comp1.setUI(new BasicButtonUI());
        buttons.add(comp1, new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
        JButton comp2 = new JButton(new ImageIcon(ImageLoader.loadBufferedImage(("close.png"))));
        comp2.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                quit();
            }
        });
        comp2.setUI(new BasicButtonUI());
        buttons.add(comp2, new GridBagConstraints(2,0,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
        Dimension buttonSize = new Dimension(107, 20);
        buttons.setSize(buttonSize);
        buttons.setPreferredSize(buttonSize);
        buttons.setMaximumSize(buttonSize);
        buttons.setMinimumSize(buttonSize);
        _menuBar.registerComponent(buttons,true);


        _menuBar.initialize();

        _frame.add(_mainPanel);
        myLayout();

        _optimizer.initFocus();
    }

    private void updateSearchFieldColor()
    {
        Color color = _searchField.getText().isEmpty() ? Color.white : Color.orange;
        _panel.setBackground(color);
        _searchField.setBackground(color);
    }

    private void quit()
    {
        if(!_windowClosedSilenced)
        {
            saveData();
            System.exit(0);
        }
    }

    private void search()
    {
        Logs.log("ACTION : searched '" + _searchField.getText() + "'");
        View view1 = getCurrentView();
        view1.search(_searchField.getText().toUpperCase());
    }

    public View getCurrentView()
    {
        int view = _menuBar.getCurrentView();
        return _views[view];
    }

    private void saveData()
    {
        Kryo kryo = prepareKryo();
        try
        {
            FileOutputStream stream = new FileOutputStream("../data/project");
            Output output = new Output(stream);
            kryo.writeObject(output,_data);
            output.close();
            stream.close();
        }
        catch (Throwable e)
        {
            //TODO ?
        }
    }

    private Kryo prepareKryo()
    {
        Kryo kryo = new Kryo();
        kryo.register(Data.class);
        kryo.register(OptimizeCurveResults.class);
        kryo.register(Manacurve.class);
        kryo.register(CardImpl.class);
        return kryo;
    }

    private void sizeAndDisplay()
    {
//        Dimension dim = new Dimension(640, 480);
//        setResizable(false);
//        setPreferredSize(dim);
//        setMaximumSize(dim);
//        setMinimumSize(dim);
//        setSize(dim);
        //toggleFullScreen();
        _frame.setLocationRelativeTo(null);
        _frame.setVisible(true);
    }

    public void myLayout()
    {
        _mainPanel.removeAll();
        _mainPanel.setLayout(new GridBagLayout());
        _mainPanel.add(_menuBar.getComponent(), new GridBagConstraints(0, 0, 1, 1, 1., 1., CENTER, BOTH, new Insets(0,0,0,0), 0, 0));
//        _mainPanel.add(_currentView.getComponent(), new GridBagConstraints(0, 1, 1, 1, 1., 100., CENTER, BOTH, FINE_INSETS, 0, 0));
        _mainPanel.revalidate();
        _mainPanel.repaint();
        _frame.revalidate();
        _frame.repaint();
    }

    public void onOptimizationEnd()
    {
        _results.initialize(_optimizer.getDeck());
        _menuBar.selectTab(1);
        _results.focusTable();
    }

    public JFrame getFrame()
    {
        return _frame;
    }

    public OptimizerView getOptimizerView()
    {
        return _optimizer;
    }

    class ShowView implements MenuBarAction
    {
        private final String _title;
        private final View _view;

        ShowView(final View view, String title)
        {
            _title = title;
            _view = view;
        }

        @Override
        public void onSelection()
        {
            _currentView = _view;
            myLayout();
        }

        public String getTitle()
        {
            return _title;
        }

        public View getView()
        {
            return _view;
        }
    }

    private void toggleFullScreen()
    {
        newFrame(true);
        resizeFullScreen();
    }

    private void resizeFullScreen()
    {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        Dimension size = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();
        _frame.setSize(size);
        _frame.setPreferredSize(size);
        _frame.setMaximumSize(size);
        _frame.setUndecorated(true);
        _frame.setLocationRelativeTo(null);
        _frame.setVisible(true);
    }

    private void toggleWindowMode()
    {
        newFrame(false);

        resizeWindowedMode();
    }

    public void resizeWindowedMode()
    {
        //De quoi contenir 4 r�sultats dans la fenetre des scores sans scrollbar
        Dimension size = new Dimension(870,610);
        _frame.setSize(size);
        _frame.setMinimumSize(size);
        _frame.setPreferredSize(size);
        _frame.revalidate();
        _frame.repaint();
        _frame.setUndecorated(false);
        _frame.setLocationRelativeTo(null);
        _frame.setTitle("Deck Builder");
        _frame.setVisible(true);
        _frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(final WindowEvent e)
            {
                if(e.getComponent().equals(_frame))
                {
                    quit();
                }
            }

            @Override
            public void windowClosed(final WindowEvent e)
            {
                if(e.getComponent().equals(_frame))
                {
                    quit();
                }
            }
        });
        _frame.addWindowStateListener(new WindowStateListener()
        {
            @Override
            public void windowStateChanged(final WindowEvent e)
            {
                if(e.getNewState()==6)
                {
                    _windowClosedSilenced = true;
                    toggleFullScreen();
                    _windowClosedSilenced = false;
                }
            }
        });
        _frame.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized(final ComponentEvent e)
            {
                //_searchField.setVisible(getFrame().getSize().getWidth()>850);
            }
        });
        //TODO update barre
    }

    private void newFrame(boolean undecorated)
    {
        _frame.setVisible(false);
        _frame.dispose();
        _frame = new JFrame();
        _menuBar.setUndecorated(undecorated);
        _menuBar.setFrame(_frame);
        _menuBar.initialize();
        _frame.add(_mainPanel);
        myLayout();
    }
}
