package com.dd.gui;


import com.dd.gui.actions.ActionOpenWebBrowser;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Observable;
import java.util.Set;


public class WebPanel
{
    public enum URL_MODE
    {
        FILESYSTEM
                {
                    @Override
                    public URL createURL(final String path) throws MalformedURLException
                    {
                        return new File(path).toURI().toURL();
                    }
                },
        INTERNET
                {
                    @Override
                    public URL createURL(final String path) throws MalformedURLException
                    {
                        return new URL(path);
                    }
                };

        public abstract URL createURL(String path) throws MalformedURLException;
    }

    private JPanel _panel = new JPanel();
    private WebEngine _webEngine;
    private final URL _url;
    private final boolean _openLinksInExternalBrowser;

    public WebPanel(String path, URL_MODE urlMode, boolean openLinksInExternalBrowser)
    {
        _openLinksInExternalBrowser = openLinksInExternalBrowser;
        _panel.setPreferredSize(new Dimension(1200,150));
        try
        {
            _url = urlMode.createURL(path);
        }
        catch (MalformedURLException e)
        {
            throw new IllegalArgumentException("Invalid URL : " + path);
        }
    }

    public NoArgObservable getHomeClosed()
    {
        return _homeClosed;
    }

    private NoArgObservable _homeClosed = new NoArgObservable();

    public void revalidate()
    {
        _panel.repaint();
        _panel.revalidate();
    }

    public void initialize()
    {
        final JFXPanel fxPanel = new JFXPanel();
        _panel.removeAll();
        _panel.setLayout(new BorderLayout());
        _panel.add(fxPanel, BorderLayout.CENTER);
        _panel.setBackground(Color.DARK_GRAY);
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                final WebView webView = new WebView();
                webView.setPrefWidth(_panel.getWidth());

                webView.getChildrenUnmodifiable().addListener(new ListChangeListener<Node>()
                {
                    @Override
                    public void onChanged(Change<? extends Node> change)
                    {
                        Set<Node> deadSeaScrolls = webView.lookupAll(".scroll-bar");
                        for (Node scroll : deadSeaScrolls)
                        {
                            scroll.setVisible(true);
                        }
                    }
                });
                final Group group = new Group();
                final Scene scene = new Scene(group);
                group.getChildren().add(webView);
                fxPanel.setScene(scene);
                _webEngine = webView.getEngine();

                _webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>()
                {
                    @Override
                    public void changed(ObservableValue<? extends Worker.State> ov, Worker.State t, final Worker.State t1)
                    {
                        onEvent(t1);
                    }
                });
                _webEngine.load(_url.toString());
            }
        });
    }

    protected void onEvent(final Worker.State t1)
    {
        ObservableList<WebHistory.Entry> entries = _webEngine.getHistory().getEntries();
        if (t1== Worker.State.SUCCEEDED && entries.size() > 1)
        {
            String url = entries.get(entries.size() - 1).getUrl();
            new ActionOpenWebBrowser(URL_MODE.INTERNET, url, false).actionPerformed(null);
        }

        if (_webEngine.getHistory().getEntries().size() == 1 && t1 == Worker.State.SCHEDULED)
        {

           _homeClosed.trigger();
        }

        //Liens simples
        if (false) {
                    Document doc = _webEngine.getDocument();
            if(doc==null)
            {
                return;
            }
                    Element el = doc.getElementById("a");
                    NodeList lista = doc.getElementsByTagName("a");
                    for (int i=0; i<lista.getLength(); i++)
                    {

                        NamedNodeMap attributes = lista.item(i).getAttributes();
                        for(int j = 0 ; j < attributes.getLength() ; j++)
                        {
                            org.w3c.dom.Node thisAttribute = attributes.item(j);
                            if(thisAttribute.getNodeName().equals("href"))
                            {
                                final String link = thisAttribute.getNodeValue();
                                if(link.startsWith("http"))
                                {
                                    EventListener listener = new EventListener() {
                                        public void handleEvent(Event ev) {
                                            new ActionOpenWebBrowser(URL_MODE.INTERNET, link, false).actionPerformed(null);
                                        }
                                    };
                                    ((EventTarget)lista.item(i)).addEventListener("click", listener, false);
                                }
                            }
                        }
                    }
                }
    }

    public JPanel getPanel()
    {
        return _panel;
    }
}
