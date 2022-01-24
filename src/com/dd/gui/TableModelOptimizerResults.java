package com.dd.gui;


import com.dd.model.Manacurve;
import com.dd.model.Stat;
import com.dd.usc.UseCaseOptimizeManaCurve;

import javax.swing.table.AbstractTableModel;
import java.util.Collection;
import java.util.Set;


public class TableModelOptimizerResults extends AbstractTableModel
{
    private OptimizerResultsView _view;

    public TableModelOptimizerResults(OptimizerResultsView view)
    {
        _view = view;
    }

    @Override
    public int getRowCount()
    {
        UseCaseOptimizeManaCurve usc = _view.getUseCase();
        return usc!=null ? usc.getResults().getResults().size() : 0;
    }

    @Override
    public int getColumnCount()
    {
        return 2;
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex)
    {
        Set<Manacurve> curves = _view.getUseCase().getResults().getResults().keySet();

        int index = curves.size() - rowIndex - 1;

        if(columnIndex==0)
        {
            Manacurve curve = (Manacurve)curves.toArray()[index];
            return curve;
        }
        else if(columnIndex==1)
        {
            Collection<Stat> scores = _view.getUseCase().getResults().getResults().values();
            Stat stat = (Stat) scores.toArray()[index];
            Double val = stat.getScore();
            return "" + Math.round(val * 10.) / 10. + " (" + ((int)stat.getManaNotUSed()) + ")";
        }
        return null;
    }

    @Override
    public String getColumnName(final int column)
    {
        return new String[]{"Mana Curve", "Score"}[column];
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex)
    {
        if(columnIndex==0)
        {
            return Manacurve.class;
        }
        else
        {
            return String.class;
        }
    }
}
