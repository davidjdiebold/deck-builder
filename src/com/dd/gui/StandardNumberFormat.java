package com.dd.gui;

import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class StandardNumberFormat {
    public static int _minDecimalDigits = 2;//Todo configure in BasicComponentConfiguration

    public static int _optionalDecimalDigits = 5;//Todo configure in BasicComponentConfiguration

    public static DecimalFormat getDecimalFormat(int minDecimalDigits, int optionalDecimalDigits) {
        DecimalFormat formateurNombres = (DecimalFormat) DecimalFormat.getNumberInstance();
        String pattern = "0";
        if (minDecimalDigits > 0 || optionalDecimalDigits > 0) {
            pattern += ".";
        }
        char[] decimalPart = new char[minDecimalDigits];
        Arrays.fill(decimalPart, '0');
        pattern += new String(decimalPart);
        char[] optionalDecimalPart = new char[optionalDecimalDigits];
        Arrays.fill(optionalDecimalPart, '#');
        pattern += new String(optionalDecimalPart);

        formateurNombres.applyPattern((pattern));

        return formateurNombres;
    }

    public static DecimalFormat getStandardDecimalFormat() {
        return getDecimalFormat(_minDecimalDigits, _optionalDecimalDigits);
    }

    public static NumberFormat getStandardIntegerFormat() {
        DecimalFormat formateurNombres = (DecimalFormat) DecimalFormat.getIntegerInstance();
        formateurNombres.applyPattern(("0"));

        return formateurNombres;
    }

    public static DecimalFormat getLatitudeFormat() {
        DecimalFormat formateurNombres = (DecimalFormat) DecimalFormat.getNumberInstance();
        formateurNombres.applyPattern(("#0.00000# N;#0.00000# S"));


        return formateurNombres;
    }

    public static DecimalFormat getLongitudeFormat() {
        DecimalFormat formateurNombres = (DecimalFormat) DecimalFormat.getNumberInstance();
        formateurNombres
                .applyPattern(("##0.00000# 'E';##0.00000# W"));// 'E' is between quotes to distinct E as EAST from E as EXPONENT


        return formateurNombres;
    }
}

