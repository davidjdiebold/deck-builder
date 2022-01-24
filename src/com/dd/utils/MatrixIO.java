package com.dd.utils;


import org.ejml.simple.SimpleMatrix;

import java.io.FileWriter;
import java.io.IOException;


public class MatrixIO
{

    public void write(String filePath, SimpleMatrix matrix)
    {
        try
        {
            FileWriter writer = new FileWriter(filePath);

            for(int i = 0 ; i < matrix.numCols() ; i++)
            {
                for(int j = 0 ; j < matrix.numRows() ; j++)
                {
                    writer.append(""+matrix.get(j,i));
                    writer.append(";");
                }
                writer.append("\r\n");
            }

            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

}
