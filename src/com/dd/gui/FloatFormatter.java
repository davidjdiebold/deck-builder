package com.dd.gui;


import javax.swing.text.NumberFormatter;
import java.text.ParseException;

public class FloatFormatter extends NumberFormatter
{
    public FloatFormatter(boolean autorizeNegativeValues)
    {
        super(StandardNumberFormat.getStandardDecimalFormat());
        setValueClass(Float.class);

        // This editor only accepts positive numbers
        // No upper boundary is set
        setMinimum(autorizeNegativeValues?Float.MIN_VALUE:0.0f);
        setMaximum(Float.MAX_VALUE);
        // Invalid values can't be entered
        setAllowsInvalid(false);
    }

    public FloatFormatter() {
        this(false);
    }

    public FloatFormatter(int minDecimalDigits, int optionalDecimalDigits) {
        this(minDecimalDigits, optionalDecimalDigits, 0.0f, Float.MAX_VALUE);

    }

    public FloatFormatter(int minDecimalDigits, int optionalDecimalDigits, Float minValue, Float maxValue) {
        super(StandardNumberFormat.getDecimalFormat(minDecimalDigits, optionalDecimalDigits));
        setValueClass(Float.class);

        // This editor only accepts positive numbers
        // No upper boundary is set
        setMinimum(minValue);
        setMaximum(maxValue);

        // Invalid values can't be entered
        setAllowsInvalid(false);
    }


	@Override
    public String valueToString(Object value) throws ParseException
    {
		if (value ==null) {
			return "";
		} else {
			if (value instanceof String) {
				return (String)value;
			}
			return getFormat().format(value);
		}
	}
}
