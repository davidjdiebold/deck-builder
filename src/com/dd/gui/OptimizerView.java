package com.dd.gui;

import com.dd.builder.ModelCurveBuilder;
import com.dd.model.*;
import com.dd.usc.UseCaseOptimizeManaCurve;
import com.dd.usc.UseCaseOptimizeManaCurveFine;

import javax.swing.*;

import static java.awt.GridBagConstraints.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.List;


public class OptimizerView implements View
{
    private MainFrame _frame;

    private final Data _data;

    private OptimizerParametersView _parameters;
    private CardListView _cards;

    private JProgressBar _progressBar;
    private WebPanel _advertisment;

    private JButton _launchOptimization;
    private JButton _cancelOptimization;

    private JPanel _panel = new JPanel(new GridBagLayout());
    private final Dimension _dim = new Dimension(640,180);

    private UseCaseOptimizeManaCurve _useCase;
    private boolean _isOptimizing = false;

    public OptimizerView(MainFrame frame, final Data data)
    {
        _frame = frame;
        _data = data;
        initialize();
    }

    public void initialize()
    {
        _parameters = new OptimizerParametersView(_data, this);
        _parameters.getComponent().setBorder(BorderFactory.createTitledBorder("Parameters"));

        _cards = new CardListView(_data);
        _cards.getComponent().setBorder(BorderFactory.createTitledBorder("Forced Cards"));

        _launchOptimization = ComponentFactory.createButton("Find !", "search_64.png", 64);
        Font font = _launchOptimization.getFont();                           //To fit with height
        _launchOptimization.setFont(new Font(font.getName(), font.getStyle(), 88));
        _launchOptimization.setSize(_dim);
        _launchOptimization.setPreferredSize(_dim);
        _launchOptimization.setMaximumSize(_dim);
        _launchOptimization.setMinimumSize(_dim);
        _launchOptimization.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                launchOptimization();
            }
        });

        _cancelOptimization = ComponentFactory.createButton(Color.RED, "cancel.png", 64);
        _cancelOptimization.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                _data.getUseCases().remove(_useCase);
                _useCase.cancel();

            }
        });

        _advertisment = new WebPanel(Urls.ADVERTISEMENT, WebPanel.URL_MODE.INTERNET, true);
        _advertisment.initialize();

        _progressBar = new JProgressBar();
        //_progressBar.setMaximumSize(_dim);
        //_progressBar.setMinimumSize(_dim);
        _progressBar.setPreferredSize(_dim);
        _progressBar.setStringPainted(true);
        _progressBar.setBorderPainted(true);
        _progressBar.setDoubleBuffered(true);
        _progressBar.setOpaque(true);
        _progressBar.setSize(_dim);
        _progressBar.setValue(0);
        _progressBar.setString("0 %");

        layout(false);
    }

    private void layout(boolean isOptimizing)
    {
        _isOptimizing = isOptimizing;
        _panel.removeAll();
        _panel.add(_parameters.getComponent(), new GridBagConstraints(0,0,1,1,100,100,CENTER,BOTH,MainFrame.MEDIUM_INSETS,0,0));
        _panel.add(_cards.getComponent(), new GridBagConstraints(1,0,1,1,1,100,CENTER,BOTH,MainFrame.MEDIUM_INSETS,0,0));
        JPanel panel = new JPanel(new GridBagLayout());

        _advertisment.initialize();

        if(isOptimizing)
        {

            panel.setMaximumSize(_dim);
            panel.setMinimumSize(_dim);

            //_advertisment.initialize();
            _progressBar.setMaximumSize(new Dimension(2000,20));
            panel.add(_advertisment.getPanel(), new GridBagConstraints(0,0,1,2,100,100,CENTER,BOTH,new Insets(0,0,0,0),0,0));
            panel.add(_progressBar, new GridBagConstraints(1,0,1,1,1,1,CENTER,BOTH,new Insets(0,0,0,0),0,0));
            panel.add(_cancelOptimization, new GridBagConstraints(1,1,1,1,1,3,CENTER,BOTH,new Insets(0,0,0,0),0,0));

            _panel.add(panel, new GridBagConstraints(0,1,2,1,1,1,CENTER,BOTH,MainFrame.MEDIUM_INSETS,0,0));
        }
        else
        {
            _panel.add(_launchOptimization, new GridBagConstraints(0,1,2,1,1,1,CENTER,BOTH,MainFrame.MEDIUM_INSETS,0,0));
        }

        _panel.revalidate();
        _panel.repaint();

        _advertisment.initialize();

        panel.revalidate();
        panel.repaint();

        _panel.revalidate();
        _panel.repaint();
    }

    private void launchOptimization()
    {
        layout(true);

        SwingWorker<Void,Void> worker = new SwingWorker<Void, Void>()
        {
            @Override
            protected void done()
            {
                _parameters.refresh();
                layout(false);
                if(!_useCase.isCanceled())
                {
                    _frame.onOptimizationEnd();
                }
            }

            @Override
            protected void process(final List<Void> chunks)
            {
                _progressBar.setValue(_useCase.getResults().getAdvancement());
                _progressBar.setString("" + _useCase.getResults().getAdvancement() + " %");
                _progressBar.revalidate();
                _progressBar.repaint();
                _panel.revalidate();
                _panel.repaint();
                _frame.getFrame().getGlassPane().repaint();
            }

            @Override
            protected Void doInBackground() throws Exception
            {
                try
                {
                    Manacurve minCurve = new Manacurve(Double.NaN, 8);
                    Map<Card,Integer> fixedCards = new LinkedHashMap<Card, Integer>();
                    for (Card card : _cards.getFixedCards())
                    {
                        int cost = card.getCapedCost();
                        if (Double.isNaN(minCurve.getCount(cost)))
                        {
                            minCurve.setCount(cost, 1);
                        }
                        else
                        {
                            minCurve.setCount(cost, minCurve.getCount(cost) + 1);
                        }
                        int count = fixedCards.get(card)==null ? 0 : fixedCards.get(card);
                        fixedCards.put(card, count + 1);
                    }

                    ModelCurveBuilder builder = new ModelCurveBuilder(minCurve, _data.getConfiguration());

                    Random random = new Random(0x12345);

                    Library library = new Library(false);
                    for(Card card : _data.listCards())
                    {
                        library.add(card, _data.getConfiguration().getNbMaxSameCardInDeck(card.getName()));
                    }
                    //Des fois que l'utilisateur en demande plus...
                    for(Card card : fixedCards.keySet())
                    {
                        int delta = Math.abs(fixedCards.get(card) - library.getCount(card));
                        library.add(card, delta);
                    }

                    //_useCase = new UseCaseOptimizeManaCurve(library, _cards.getFixedCards(),
                    _useCase = new UseCaseOptimizeManaCurveFine(library, _cards.getFixedCards(),
                            _parameters.getTurn(), builder, random, builder.getModel().buildDomain().size(), new MyProgressBar(), _data.getConfiguration());
                    _useCase.setConfigurationName(_parameters.getConfigurationName());
                    _useCase.setNbGames(_parameters.getGames());
                    _useCase.setHero(_parameters.getHero());
                    _useCase.setConfigurationName(_parameters.getConfigurationName());

                    Date date = new Date();
                    _data.addUseCase(_useCase);

                    Logs.log("ACTION : Launch Optimization");
                    Logs.log(" deck : " + _useCase.getConfigurationName());
                    Logs.log(" #games : " + _useCase.getNbGames());
                    Logs.log(" Hero : " + _useCase.getHero());
                    Logs.log(" #turns : " + _useCase.getNB_MAX_TURNS());

                    _useCase.execute();
                    return null;
                }
                catch (Throwable e)
                {
                    Logs.log(e);
                }
                return null;
            }
        };

        worker.execute();
    }

    public JComponent getComponent()
    {
        return _panel;
    }

    @Override
    public void enter()
    {
        if(!_isOptimizing)
        {
            launchOptimization();
        }
    }

    @Override
    public void search(final String text)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void refresh()
    {
        _cards.refresh();
    }

    public void setDeck(final UseCaseOptimizeManaCurve useCase)
    {
        _cards.setDeck(useCase);
    }

    public void initFocus()
    {
        _parameters.initFocus();
    }

    public UseCaseOptimizeManaCurve getDeck()
    {
        return _useCase;
    }

    class MyProgressBar implements ProgressBar
    {
        @Override
        public void setAdvancement(final int adv)
        {
            Runnable runnable = new Runnable()
            {
                public void run()
                {
                    _progressBar.setValue(adv);
                    _progressBar.setString("" + adv + " %");
                    _progressBar.revalidate();
                    _progressBar.repaint();
                    _panel.revalidate();
                    _panel.repaint();
                    _frame.getFrame().getGlassPane().repaint();

                }
            };
//            if (SwingUtilities.isEventDispatchThread())
//            {
//                runnable.run();
//            }
//            else
//            {
                SwingUtilities.invokeLater(runnable);
            //}
        }
    }
}
