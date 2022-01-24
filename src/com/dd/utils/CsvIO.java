package com.dd.utils;


/**
 * Created with IntelliJ IDEA.
 * User: ddiebold
 * Date: 1/6/14
 * Time: 10:34 PM
 * To change this template use File | Settings | File Templates.
 */

import com.dd.gui.Logs;

import java.io.*;
import java.util.Collection;


import java.io.*;
import java.util.Collection;
import java.util.Locale;


/**
 * User: ddiebold
 * Date: 24/06/13
 * Time: 14:36
 */
public abstract class CsvIO<T, R>
{

    protected final String _cvsSplitBy;
    protected final String _decimalSeparator;

    protected abstract boolean hasHeader();

    protected abstract String[] buildHeader();

    protected abstract void preFill(T container);

    protected abstract void fill(final String[] tokens, final T elementToFill);

    protected abstract Collection<R> listRecords(T container);

    protected abstract String[] listValues(R record);

    protected CsvIO()
    {
        _cvsSplitBy = ",";
        _decimalSeparator = ".";
    }

    protected void readHeader(T elementToFill, String[] header)
    {
    }

    protected String[] buildHeader(T elementToWrite)
    {
        return buildHeader();
    }

    public void read(String filePath, T elementToFill)
    {
        BufferedReader br = null;
        String line = "";


        preFill(elementToFill);

        try
        {
            FileReader fileReader = new FileReader(new File(filePath));
            br = new BufferedReader(fileReader);
            if (hasHeader())
            {
                line = br.readLine();
                String[] tokens = line.split(_cvsSplitBy);
                readHeader(elementToFill, tokens);
            }

            while ((line = br.readLine()) != null)
            {
                String[] tokens = line.split(_cvsSplitBy);
                fill(tokens, elementToFill);
            }
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    Logs.log(e);
                }
            }
        }
    }


    public void write(String filePath, T elementToWrite)
    {
        //new File(filePath).delete();
        try
        {
            FileWriter writer = new FileWriter(filePath);

            if (hasHeader())
            {
                String[] header = buildHeader(elementToWrite);
                writeLine(writer, header);
            }

            for (R record : listRecords(elementToWrite))
            {
                writeIndex(writer, record);
            }
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void writeIndex(final FileWriter writer, final R record) throws IOException
    {
        String[] tokens = listValues(record);
        writeLine(writer, tokens);
    }

    private void writeLine(final FileWriter writer, final String[] tokens) throws IOException
    {
        for (String value : tokens)
        {
            writer.append(value);
            writer.append(_cvsSplitBy);
        }
        writer.append('\n');
    }


}

