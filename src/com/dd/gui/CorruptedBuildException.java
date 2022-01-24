package com.dd.gui;

public class CorruptedBuildException extends RuntimeException
{
    private final String _missingPath;

    public CorruptedBuildException(String missingPath)
    {
        super("Files are missing in the software installation folder. " +
        "Problem should be solved if you re-install the software."
        );
        _missingPath = missingPath;
    }

    public String getMissingPath()
    {
        return _missingPath;
    }
}
